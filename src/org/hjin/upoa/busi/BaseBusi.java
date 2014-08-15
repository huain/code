package org.hjin.upoa.busi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.hjin.upoa.util.net.AsyncRunner;
import org.hjin.upoa.util.net.MyHttpException;
import org.hjin.upoa.util.net.MyParameters;
import org.hjin.upoa.util.net.RequestListener;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BaseBusi implements RequestListener{
	
	protected final String TAG = "BaseBusi";
	
	public final static int SHOWMESSAGE = 0;
	/**
	 * post请求方式
	 */
	public static final String HTTPMETHOD_POST = "POST";
	/**
	 * get请求方式
	 */
	public static final String HTTPMETHOD_GET = "GET";
	
	protected Handler mHandler;
	
	protected Activity mActivity;
	
	protected BaseBusi(){
		
	}
	
	/**
	 * 
	 * @param handler
	 */
	public BaseBusi(Handler handler){
		this.mHandler = handler;
	}
	
	public BaseBusi(Activity activity,Handler handler){
		this.mHandler = handler;
		this.mActivity = activity;
	}
	
	protected void request( final int flag, final String url, final MyParameters params,
			final String httpMethod,RequestListener listener) {
		AsyncRunner.request(flag,url, params, httpMethod, listener);
	}
	
	protected void request4Binary( final int flag, final String url, final MyParameters params,
			final String httpMethod,RequestListener listener) {
		AsyncRunner.request4Binary(flag,url, params, httpMethod, listener);
	}

	@Override
	public void onComplete(String response, int flag) {
		
	}

	@Override
	public void onComplete4binary(ByteArrayOutputStream responseOS, int flag) {
		
	}

	@Override
	public void onIOException(IOException e, int flag) {
		Log.d(TAG, "***onIOException");
		Message msg = mHandler.obtainMessage();
		msg.getData().putString("message", "网络连接异常，请检查您的网络！");
		msg.sendToTarget();
		e.printStackTrace();
	}

	@Override
	public void onError(MyHttpException e, int flag) {
		Log.d(TAG, "***onError");
		Message msg = mHandler.obtainMessage();
		msg.getData().putString("message", "请求失败，失败代码："+ e.getStatusCode()+"!");
		msg.sendToTarget();
		e.printStackTrace();
	}
	
	protected void setNoDataMsg(Message msg){
		msg.what = SHOWMESSAGE;
		msg.getData().putString("message", "没有数据！");
	}
	
	

}
