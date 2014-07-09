package org.hjin.upoa.ui;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.BaseBusi;
import org.hjin.upoa.busi.LoginBusi;
import org.hjin.upoa.busi.LoginBusi.LoginListener;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.ui.view.ValidateDialogFragment;
import org.hjin.upoa.ui.view.ValidateDialogFragment.IValidateDialogFragment;
import org.hjin.upoa.util.Utility;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements LoginListener,IValidateDialogFragment {
	
	private final String TAG = "LoginActivity";
	
	private String mUsername;
	
	private String mPassword;
	
	private ValidateDialogFragment mVdf;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 0:{
				Bundle data = msg.getData();
				if(null != data && !Utility.isBlank(data.getString("message"))){
					String message = data.getString("message");
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				}
			}break;
			default :break;
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
//		getActionBar().hide();
		
		EditText et_username = (EditText)findViewById(R.id.set_login_username);
		EditText et_password = (EditText)findViewById(R.id.set_login_password);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		boolean issaveUsername = sp.getBoolean("setting_item_usernamesaveauto", false);
		boolean issavePassword = sp.getBoolean("setting_item_passwordsaveauto", false);
		if(issaveUsername){
			et_username.setText(sp.getString("login_username", ""));
		}
		if(issavePassword){
			et_password.setText(sp.getString("login_password", ""));
		}
	}
	
	
	


	public void LoginBtnOnClick(View view){
		mVdf = ValidateDialogFragment.newInstance();
	    showDialog(mVdf, "validate");
		EditText et_username = (EditText)findViewById(R.id.set_login_username);
		EditText et_password = (EditText)findViewById(R.id.set_login_password);
		mUsername = et_username.getText().toString();
		mPassword = et_password.getText().toString();
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor e = sp.edit();
		e.putString("login_username", mUsername);
		e.putString("login_password", mPassword);
		e.commit();
		
		AppConstants.loginname = mUsername;
		
		LoginBusi lbs = new LoginBusi(this.getApplicationContext());
		lbs.loginBefore(mUsername, mPassword, this);
	}
	
	public void Login(String validateCode){
		LoginBusi lbs = new LoginBusi(this.getApplicationContext());
		lbs.login(mUsername, mPassword, validateCode, this);
	}
	
	// IValidateDialogFragment的接口实现
	@Override
	public void doPositiveClick(Bundle data){
		Message msg = new Message();
		msg.what = ValidateDialogFragment.END_INPUT;
		mVdf.mHandler.sendMessage(msg);
		
		LoginBusi lbs = new LoginBusi(this.getApplicationContext());
		lbs.login(mUsername, mPassword, data.getString("valiedateCode"), this);
		
	}
	
	
	// IValidateDialogFragment的接口实现
	@Override
	public void doNegativeClick(Bundle data) {
		removeDialogByTag("validate");
	}

	// LoginListener实现
	@Override
	public void onStartLogin() {
		Log.v(TAG, "===onStartLogin");
	}

	// LoginListener实现
	@Override
	public void onValidateCode(ByteArrayOutputStream bos) {
		byte[] picContent = bos.toByteArray();
		Log.v(TAG, picContent.toString());
		
		Message msg = new Message();
		msg.what = ValidateDialogFragment.START_VALIDATE;
		Bundle data = new Bundle();
		data.putByteArray("validateCodePic", picContent);
		msg.setData(data);
		mVdf.mHandler.sendMessage(msg);
	}
	
	@Override
	public void onValidateCodeError() {
		AppConstants.sLt = "";
		// TODO Auto-generated method stub
		if(mVdf != null){
			removeDialogByTag("validate");
		}
		Message msg = mHandler.obtainMessage();
		msg.what = 0;
		msg.getData().putString("message", "验证失败，检查用户名和密码后请重试！");
		msg.sendToTarget();
	}





	// LoginListener实现
	@Override
	public void onCompleteLogin() {
		Log.v(TAG, "===onCompleteLogin");
		if(mVdf != null){
			removeDialogByTag("validate");
		}
		Intent i = new Intent();
		i.setClass(getApplicationContext(), MainActivity.class);
		startActivity(i);
	}

	// LoginListener实现
	@Override
	public void onServerException() {
		AppConstants.sLt = "";
		if(mVdf != null){
			removeDialogByTag("validate");
		}
		Message msg = mHandler.obtainMessage();
		msg.what = 0;
		msg.getData().putString("message", "登录失败，请重试！");
		msg.sendToTarget();
	}

	// LoginListener实现
	@Override
	public void onNetException() {
		AppConstants.sLt = "";
		// TODO Auto-generated method stub
		if(mVdf != null){
			removeDialogByTag("validate");
		}
		Message msg = mHandler.obtainMessage();
		msg.what = 0;
		msg.getData().putString("message", "网络连接异常，请检查您的网络！");
		msg.sendToTarget();
	}
	
	/**
	 * 根据tag，移除dialog
	 * @param tag
	 */
	private void removeDialogByTag(String tag){
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag(tag);
	    if (prev != null) {
	    	DialogFragment vdf = (DialogFragment)prev;
	 	    vdf.dismiss();
	        ft.remove(prev);
	    }
	    
	}
	
	/**
	 * 显示dialog
	 * @param df
	 * @param tag
	 */
	private void showDialog(DialogFragment df,String tag){
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag(tag);
	    if (prev != null) {
	    	ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    df.setCancelable(false);
	    df.show(ft, tag);
	}
	
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK){    
	    	exitBy2Click();      //调用双击退出函数  
	    }  
	    return false;  
	}
    
    /** 
	 * 双击退出函数 
	 */  
	private static Boolean isExit = false;  
	  
	private void exitBy2Click() {  
	    Timer tExit = null;  
	    if (isExit == false) {  
	        isExit = true; // 准备退出  
	        Toast.makeText(this, "再按一次退出到桌面", Toast.LENGTH_SHORT).show();  
	        tExit = new Timer();  
	        tExit.schedule(new TimerTask() {  
	            @Override  
	            public void run() {  
	                isExit = false; // 取消退出  
	            }  
	        }, 1000); // 如果1秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  
	  
	    } else {  
//	        finish();  
//	        System.exit(0);  
	    	Intent home = new Intent(Intent.ACTION_MAIN);  
	        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	        home.addCategory(Intent.CATEGORY_HOME);  
	        startActivity(home);
	    }  
	}

}
