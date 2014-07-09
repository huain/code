package org.hjin.upoa.ui;

import org.hjin.upoa.busi.BaseBusi;
import org.hjin.upoa.util.Utility;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class BaseHandler extends Handler {
	
	protected Context mContext;
	
	public BaseHandler(Context context){
		mContext = context;
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch(msg.what){
		case BaseBusi.SHOWMESSAGE:{
			Bundle data = msg.getData();
			if(null != data && !Utility.isBlank(data.getString("message"))){
				String message = data.getString("message");
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
			}
		}break;
		default :break;
		}
	}
	
	

}
