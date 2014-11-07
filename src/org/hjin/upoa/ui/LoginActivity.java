package org.hjin.upoa.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.LoginBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.ui.view.ProgressDialogFragment;
import org.hjin.upoa.util.Utility;

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
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	
	private final String TAG = "LoginActivity";
	
	private String mUsername;
	
	private String mPassword;
	
	private LinearLayout mLoginLinearLayout;
	
	private DialogFragment mPdf;
	
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
			case LoginBusi.ERROR:{
				if(mPdf != null){
					removeDialogByTag("validate");
				}
				Bundle data = msg.getData();
				if(null != data && !Utility.isBlank(data.getString("message"))){
					String message = data.getString("message");
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				}
			}break;
			case LoginBusi.SYSERROR:{
				if(mPdf != null){
					removeDialogByTag("validate");
				}
				Bundle data = msg.getData();
				if(null != data && !Utility.isBlank(data.getString("message"))){
					String message = data.getString("message");
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				}
			}break;
			case LoginBusi.SUCCESS:{
				if(mPdf != null){
					removeDialogByTag("validate");
				}
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MainActivity.class);
				startActivity(i);
			}break;
			default :break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mLoginLinearLayout = (LinearLayout)findViewById(R.id.login_linearlayout);
		
		EditText et_username = (EditText)findViewById(R.id.set_login_username);
		EditText et_password = (EditText)findViewById(R.id.set_login_password);
		
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		boolean issave = sp.getBoolean("setting_item_saveauto", false);
		boolean islogin = sp.getBoolean("setting_item_loginauto", false);
		if(islogin){
			mLoginLinearLayout.setVisibility(View.INVISIBLE);
			login();
		}else{
			mLoginLinearLayout.setVisibility(View.VISIBLE);
		}
		if(issave){
			et_username.setText(sp.getString("login_username", ""));
			et_password.setText(sp.getString("login_password", ""));
		}
	}
	
	
	


	public void LoginBtnOnClick(View view){
		mPdf = ProgressDialogFragment.newInstance("正在登录，请稍后……");
	    showDialog(mPdf, "validate");
	    login();
	}
	
	private void login(){
		EditText et_username = (EditText)findViewById(R.id.set_login_username);
		EditText et_password = (EditText)findViewById(R.id.set_login_password);
		mUsername = et_username.getText().toString();
		mPassword = et_password.getText().toString();
		
		mUsername = "huangjin";
		mPassword = "hj1025";
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor e = sp.edit();
		e.putString("login_username", mUsername);
		e.putString("login_password", mPassword);
		e.commit();
		
		AppConstants.loginname = mUsername;
		
		LoginBusi lbs = new LoginBusi(getApplicationContext(),mHandler);
		lbs.login(mUsername, mPassword);
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
