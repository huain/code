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
	
	/**msgcode:业务错误*/
	public final static int ERROR = 400;
	/**msgcode:系统错误*/
	public final static int SYSERROR = 500;
	/**msgcode:登录成功*/
	public final static int SUCCESS = 200;
	
	
	
	public LoginBusi(Context context, Handler handler) {
		super(context,handler);
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
		getUserHeaderInfo();
		getUserInfo();
	}
	
	private void getCookieStore(){
		MyParameters params = new MyParameters();
		request(GET_COOKIESTORE, AppConstants.sReq_IndexOnLineSum, params, HTTPMETHOD_GET, this);
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
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
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
				msg.sendToTarget();
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
