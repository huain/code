package org.hjin.upoa.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.util.net.AsyncRunner;
import org.hjin.upoa.util.net.MyHttpException;
import org.hjin.upoa.util.net.MyParameters;
import org.hjin.upoa.util.net.RequestListener;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class OnLineService extends IntentService implements RequestListener {
	
	private final String TAG = "OnLineService";
	
	private OnLineService mThis;
	
	private final Timer timer = new Timer();
	
	private MyParameters mParams;
	
	
	private TimerTask tt = new TimerTask() {
		@Override
		public void run() {
			Log.d(TAG, "run");
			AsyncRunner.request(1, AppConstants.sReq_GetOnLine, mParams,"POST", mThis);
		}
	};
	
	public OnLineService() {
		super("onLineCount");
		this.mThis = this;
		mParams = new MyParameters();
		mParams.add("header_referer", AppConstants.sReq_GetOnLine_Referer);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//timer.schedule(tt,0,120000);
		
	}
	
	

	@Override
	public void onComplete(String response, int flag) {
		Log.d(TAG, "===ok:当前时间："+System.currentTimeMillis()+" 当前人数："+response);
		AppConstants.onlinesum = response;
	}

	@Override
	public void onComplete4binary(ByteArrayOutputStream responseOS, int flag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIOException(IOException e, int flag) {
		// TODO Auto-generated method stub
		Log.d(TAG, "===error:IOException:当前时间："+System.currentTimeMillis());
	}

	@Override
	public void onError(MyHttpException e, int flag) {
		// TODO Auto-generated method stub
		Log.d(TAG, "===error:MyHttpException:当前时间："+System.currentTimeMillis());
	}
	
	

}
