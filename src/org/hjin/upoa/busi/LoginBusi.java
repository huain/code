package org.hjin.upoa.busi;

import java.io.IOException;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.util.net.MyHttpException;
import org.hjin.upoa.util.net.MyParameters;

import android.os.Handler;
import android.os.Message;
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
	
	/**msgcode:业务错误*/
	public final static int ERROR = 400;
	/**msgcode:系统错误*/
	public final static int SYSERROR = 500;
	/**msgcode:登录成功*/
	public final static int SUCCESS = 200;
	
	
	
	public LoginBusi(Handler handler){
		this.mHandler = handler;
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
		
	}
	
	private void getCookieStore(){
		MyParameters params = new MyParameters();
		request(GET_COOKIESTORE, AppConstants.sReq_IndexOnLineSum, params, HTTPMETHOD_GET, this);
	}
	
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
				Message msg = mHandler.obtainMessage();
				msg.what = SUCCESS;
				msg.sendToTarget();
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
