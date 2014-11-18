package org.hjin.upoa.busi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.util.net.AsyncRunner;
import org.hjin.upoa.util.net.MyHttpException;
import org.hjin.upoa.util.net.MyParameters;
import org.hjin.upoa.util.net.RequestListener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * ҵ�������
 * @author Administrator
 *
 */
public class BaseBusi implements RequestListener{
	
	protected final String TAG = "BaseBusi";
	
	public final static int SHOWMESSAGE = 0x0000;
	
	public final static int SHOWPROGRESS = 0x0001;
	
	public final static int HIDDENPROGRESS = 0x0002;
	
	/**
	 * post����ʽ
	 */
	public static final String HTTPMETHOD_POST = "POST";
	/**
	 * get����ʽ
	 */
	public static final String HTTPMETHOD_GET = "GET";
	
	protected Handler mHandler;
	
	protected Context mContext;
	
	protected BaseBusi(){
		
	}
	
	/**
	 * 
	 * @param handler
	 */
	public BaseBusi(Handler handler){
		this.mHandler = handler;
	}
	
	public BaseBusi(Context context,Handler handler){
		this.mHandler = handler;
		this.mContext = context;
	}
	
	/**
	 * �첽����
	 * @param flag
	 * @param url
	 * @param params
	 * @param httpMethod
	 * @param listener
	 */
	protected void request( int flag, String url, MyParameters params,
			String httpMethod,RequestListener listener) {
		if(params == null){
			params = new MyParameters();
		}
		params.add("token", AppConstants.token);
		AsyncRunner.request(flag,url, params, httpMethod, listener);
	}
	
	/**
	 * �첽����
	 * @param flag
	 * @param url
	 * @param params
	 * @param httpMethod
	 * @param listener
	 */
	protected void request4Binary( int flag, String url, MyParameters params,
			String httpMethod,RequestListener listener) {
		if(params == null){
			params = new MyParameters();
		}
		params.add("token", AppConstants.token);
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
		msg.what = SHOWMESSAGE;
		msg.getData().putString("message", "���������쳣�������������磡");
		msg.sendToTarget();
		e.printStackTrace();
	}

	@Override
	public void onError(MyHttpException e, int flag) {
		Log.d(TAG, "***onError");
		Message msg = mHandler.obtainMessage();
		msg.what = SHOWMESSAGE;
		msg.getData().putString("message", "����ʧ�ܣ�ʧ�ܴ��룺"+ e.getStatusCode()+"!");
		msg.sendToTarget();
		e.printStackTrace();
	}
	
	protected void setNoDataMsg(Message msg){
		msg.what = SHOWMESSAGE;
		msg.getData().putString("message", "û�����ݣ�");
	}
	
}
