package org.hjin.upoa.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.hjin.upoa.busi.MainBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.util.net.AsyncRunner;
import org.hjin.upoa.util.net.MyHttpException;
import org.hjin.upoa.util.net.MyParameters;
import org.hjin.upoa.util.net.RequestListener;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class OnLineService extends Service {
	
	private final String TAG = "OnLineService";
	
	private Handler mHandler;
	
	private Binder mBinder = new OnLineBinder();
	
	private boolean mFlag;
	
	private final Timer timer = new Timer();
	
	private MyParameters mParams;
	
	private RequestListener listener = new OnLineListener();
	
	
	private TimerTask tt = new TimerTask() {
		@Override
		public void run() {
			if(mFlag){
				Log.d(TAG, "run");
				AsyncRunner.request(1, AppConstants.sReq_GetOnLine, mParams,"POST", listener);
			}
		}
	};
	
	public OnLineService() {
		Log.d(TAG, "==OnLineService1");
		mParams = new MyParameters();
		mParams.add("header_referer", AppConstants.sReq_GetOnLine_Referer);
		timer.schedule(tt,2000,60000);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		mFlag = true;
		return mBinder;
	}
	
	
	

	@Override
	public void onDestroy() {
		mFlag = false;
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		mFlag = false;
		return super.onUnbind(intent);
	}

	public void setmHandler(Handler mHandler) {
		Log.d(TAG, "==OnLineService:setmHandler");
		this.mHandler = mHandler;
	}
	
	public class OnLineBinder extends Binder {
		public OnLineService getOnLineService(){
			Log.d(TAG, "==OnLineService:getOnLineService");
			return OnLineService.this;
		}
	}

	private class OnLineListener implements RequestListener{
		@Override
		public void onComplete(String response, int flag) {
			Log.d(TAG, "===ok:当前时间："+System.currentTimeMillis()+" 当前人数："+response);
			if(mHandler != null){
				Message msg = mHandler.obtainMessage();
				msg.what = MainBusi.GETONLINESUM;
				int sum = 0;
				try {
					sum = Integer.parseInt(response);
				} catch (NumberFormatException e) {
					
				}
				// 设置是否人数变化，人数变化震动
				if(sum == AppConstants.onlinesum){
					msg.arg2 = 0;
				}else{
					msg.arg2 = 1;
				}
				msg.arg1 = sum;
				AppConstants.onlinesum = sum;
				msg.sendToTarget();
			}
			
		}

		@Override
		public void onComplete4binary(ByteArrayOutputStream responseOS, int flag) {
			
		}

		@Override
		public void onIOException(IOException e, int flag) {
			Log.d(TAG, "===error:IOException:当前时间："+System.currentTimeMillis());
		}

		@Override
		public void onError(MyHttpException e, int flag) {
			Log.d(TAG, "===error:MyHttpException:当前时间："+System.currentTimeMillis());
		}
	}

	
	
	

}
