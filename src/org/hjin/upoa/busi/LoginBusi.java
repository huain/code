package org.hjin.upoa.busi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.util.Utility;
import org.hjin.upoa.util.net.MyHttpException;
import org.hjin.upoa.util.net.MyParameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ultrapower.android.ca2.Des3;
import com.ultrapower.android.ca2.RASCipherUtil;
import com.ultrapower.android.ca2.SubTokenBean;
import com.ultrapower.android.ca2.TokenBean;

/**
 * 登录业务处理
 * 
 * @author Administrator
 * 
 */
public class LoginBusi extends BaseBusi {
	
	private final String TAG = "LoginBusi";
	
	private final int STEP1 = 0x0101;
	
	private final int STEP2 = 0x0102;
	
	private final int GET_COOKIESTORE = 0x0103;
	
	private final int GET_USERINFO = 0x0104;
	
	private final int GET_USERHEADERINFO = 0x0105;
	/** flag：获取login界面的背景图片信息*/
	public final static int GET_LOGINPIC_INFO = 0x0106;
	/** flag：获取login界面的背景图片*/
	public final static int GET_LOGINPIC = 0x0107;
	
	public final static int GET_VERSION_INFO = 0x0108;
	
	/**msgcode:业务错误*/
	public final static int ERROR = 400;
	/**msgcode:系统错误*/
	public final static int SYSERROR = 500;
	/**msgcode:登录成功*/
	public final static int SUCCESS = 200;
	
	
	private int mLoginPicVersionTemp;
	private String mLoginPicLinkTemp;
	
	/**传到主页面，标志是否有更新，0：代表无更新，1：代表有更新*/
	private int mIsHasUpdate;
	
	SharedPreferences sp;
	
	
	
	public LoginBusi(Context context, Handler handler) {
		super(context,handler);
		sp = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
	}

	public void login(String username, String password){
		try {
			password = RASCipherUtil.RSA_android_publickey(password);
//			String url = "http://me.ultrapower.com.cn:38097/OpenCA/getBootToken?clientId="+ username + "&encodePass=" + password;
			String url = "http://me.ultrapower.com.cn:38097/OpenCA/getBootToken";
			MyParameters params = new MyParameters();
			params.add("clientId", username);
			params.add("encodePass", password);
			request(STEP1, url, params, HTTPMETHOD_GET, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void login2(String bootToken){
		Log.d(TAG, "***:bootToken"+bootToken);
//		String url = "http://me.ultrapower.com.cn:38097/OpenCA/getSubToken?appKey=PMOBG&bootToken="+ bootToken;
		String url = "http://me.ultrapower.com.cn:38097/OpenCA/getSubToken";
		MyParameters params = new MyParameters();
		params.add("appKey", "PMOBG");
		params.add("bootToken", bootToken);
		request(STEP2, url, params, HTTPMETHOD_GET, this);
	}
	
	/**
	 * 登录后初始化操作
	 */
	private void init(){
		if(sp.getInt("version", 0) == 0){
			getVersionInfo();
		}
		getLoginPicInfo();
		getUserHeaderInfo();
		getUserInfo();
	}
	
	private void getCookieStore(){
		MyParameters params = new MyParameters();
		request(GET_COOKIESTORE, AppConstants.sReq_IndexOnLineSum, params, HTTPMETHOD_GET, this);
	}
	
	private void getLoginPicInfo(){
		request(GET_LOGINPIC_INFO, AppConstants.sUrl_Login_Pic_Info, null, HTTPMETHOD_GET, this);
	}
	
	public void getVersionInfo(){
		request(GET_VERSION_INFO, AppConstants.sUrl_Login_Info, null, HTTPMETHOD_GET, this);
	}
	
	private void getLoginPic(String url){
		request4Binary(GET_LOGINPIC, url, null, HTTPMETHOD_GET, this);
	}
	
	/**
	 * 取得用户的基本信息
	 */
	public void getUserInfo(){
		MyParameters params = new MyParameters();
		params.add("usercode", "");
		params.add("header_referer", AppConstants.sReq_IndexUserInfo_Referer);
		request(GET_USERINFO, AppConstants.sReq_IndexUserInfo_Referer, params, HTTPMETHOD_POST, this);
	}
	
	/**
	 * 取得用户头像
	 */
	public void getUserHeaderInfo(){
		MyParameters params = new MyParameters();
		request4Binary(GET_USERHEADERINFO, AppConstants.sReq_IndexUserHeaderInfo+AppConstants.loginname+"_head_160.jpg", params, HTTPMETHOD_POST, this);
	};
	
	private void getTodayCalendarInfo(){
		
	}
	
	
	
	@Override
	public void onComplete(String response, int flag) {
		switch(flag){
		case STEP1:{
			String output;
			try {
				output = Des3.decode(response);
				Log.d(TAG, "***:Login:"+output);
				TokenBean token = JSON.parseObject(output, TokenBean.class);
				if (!token.getState().equals("0") && token.getTokenCode().equals("null")){
					Message msg = mHandler.obtainMessage();
					msg.what = ERROR;
					msg.getData().putString("message", "用户名和密码不匹配，请重新登录！");
					msg.sendToTarget();
				}else{
					this.login2(token.getTokenCode());
				}
			} catch (Exception e) {
				Message msg = mHandler.obtainMessage();
				msg.what = SYSERROR;
				msg.getData().putString("message", "系统错误！");
				msg.sendToTarget();
				e.printStackTrace();
			}
		}break;
		case STEP2:{
			String output2;
			try {
				output2 = Des3.decode(response);
				SubTokenBean subTokenBean = JSON.parseObject(output2,SubTokenBean.class);
				String subToken = subTokenBean.getSubToken();
				AppConstants.token = subToken;
				Log.d(TAG, "***:subToken"+subToken);
				getCookieStore();
			} catch (Exception e) {
				Message msg = mHandler.obtainMessage();
				msg.what = SYSERROR;
				msg.getData().putString("message", "系统错误！");
				msg.sendToTarget();
				e.printStackTrace();
			}
		}break;
		case GET_COOKIESTORE:{
			init();
		}break;
		case GET_USERINFO:{
			JSONObject jo = null;
			try {
				JSONArray js = new JSONArray(response);
				if(null != js && js.length()>0){
					jo = js.getJSONObject(0);
				}
				if(null != jo){
					Editor e = sp.edit();
					e.putString("fullname", jo.getString("F_USERNAME"));
					e.putString("userdep", jo.getString("F_DEPTNAME"));
					e.putString("userpost", jo.getString("F_DICNAME"));
					e.commit();
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			} finally{
				Message msg = mHandler.obtainMessage();
				msg.what = SUCCESS;
				msg.arg1 = mIsHasUpdate;
				msg.sendToTarget();
			}
			
		}break;
		case GET_LOGINPIC_INFO:{
			JSONObject jo = null;
			try {
				jo = new JSONObject(response);
				if(null != jo){
					int version = sp.getInt("LoginPicVersion", 0);
					Log.d(TAG, "============"+version);
					if(jo.getInt("version") == version){
						
					}else{
						String link = jo.getString("link");
						if(link != null && !"".equals(link)){
							mLoginPicVersionTemp = jo.getInt("version");
							getLoginPic(link);
							Log.d(TAG, "============"+link);
						}
					}
					
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}break;
		case GET_VERSION_INFO:{
			JSONObject jo = null;
			try {
				jo = new JSONObject(response);
				Log.d(TAG, "==="+response);
				if(null != jo){
					Log.d(TAG, "===success");
					Message msg = mHandler.obtainMessage();
					msg.what = GET_VERSION_INFO;
					PackageInfo packInfo;
					int version = 0;
					try {
						packInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
						version = packInfo.versionCode;
					} catch (NameNotFoundException e) {
					}
					Log.d(TAG, "============"+version);
					if(jo.getInt("version") == version){
						msg.arg1 =0;
					}else{
						SharedPreferences.Editor editor = sp.edit();
						editor.putInt("isHasUpdate", 1);
						editor.putInt("version", jo.getInt("version"));
						editor.putString("versionName", jo.getString("versionName"));
						editor.putString("link", jo.getString("link"));
						editor.putString("updateDesc", jo.getString("updateDesc"));
						editor.commit();
						msg.arg1 = 1;
					}
					msg.sendToTarget();
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			} 
		}break;
		default:break;
		}
		
	}
	
	


	@Override
	public void onComplete4binary(ByteArrayOutputStream responseOS, int flag) {
		switch(flag){
		case GET_USERHEADERINFO:{
			Utility.saveFile(AppConstants.sReq_IndexUserHeaderInfo+AppConstants.loginname+"_head_160.jpg", 
					responseOS.toByteArray());
		}break;
		case GET_LOGINPIC:{
			Utility.saveFile("login_v"+mLoginPicVersionTemp+".jpg", 
					responseOS.toByteArray());
			Editor e = sp.edit();
			e.putInt("LoginPicVersion", mLoginPicVersionTemp);
			e.putString("mLoginPicLinkTemp", mLoginPicLinkTemp);
			e.commit();
			Log.d(TAG, "==================================================mLoginPicLinkTemp");
		}break;
		default:break;
		}
	}

	@Override
	public void onIOException(IOException e, int flag) {
		super.onIOException(e, flag);
	}


	@Override
	public void onError(MyHttpException e, int flag) {
		super.onError(e, flag);
	}

}
