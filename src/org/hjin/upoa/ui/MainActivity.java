package org.hjin.upoa.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.MainBusi;
import org.hjin.upoa.busi.SecretaryBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.Secretary;
import org.hjin.upoa.service.OnLineService;
import org.hjin.upoa.util.Utility;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	
	private final String TAG = "MainActivity";
	
	private SecretaryBusi mSecretaryBusi;
	
	private ImageView mHeader;
	
	private TextView mFullname;
	
	private TextView mPost;
	
	private TextView mDep;
	
	private TextView mSecretaryNum;
	
	private TextView mOnLineNum;
	
//	private ListView mWaitDealList;
//	
//	private TextView mWaitDealList_None;

    private Handler mHandler = new BaseHandler(this){
    	public void handleMessage(Message msg) {
    		Log.d(TAG, "===handleMessage:magwhat:"+ msg.what);
    		super.handleMessage(msg);
			Bundle data = msg.getData();
    		switch(msg.what){
    		case MainBusi.GETUSERHEADERINFO_F:{}break;
//    		case MainBusi.GETWAITDEALINFO:{
//    			@SuppressWarnings("unchecked")
//				List<Map<String,String>> list = (List<Map<String,String>>)data.getSerializable("result");
//				if(null != list && list.size()>0){
//					mWaitDealList_None.setVisibility(View.GONE);
//					ListAdapter aa = new SimpleAdapter(getApplicationContext(), list, R.layout.index_waitdeallist_item, new String[]{"index","dealName","dealTime","dealStatus"}, new int[]{R.id.index_waitdeallist_index,R.id.index_waitdeallist_title,R.id.index_waitdeallist_time,R.id.index_waitdeallist_status});
//					mWaitDealList.setAdapter(aa);
//				}else{
//					mWaitDealList_None.setVisibility(View.VISIBLE);
//				}
//    		}break;
    		case MainBusi.GETWAITDEALSUM:{}break;
	    	case SecretaryBusi.GET_TASK_COUNT:{
	    		Log.d(TAG, "======GET_TASK_COUNT");
	    		if(msg.arg1 == 1){
	    			AppConstants.sSecretary = (Secretary)data.getSerializable("data");
	    			
	    			if(AppConstants.sSecretary != null && AppConstants.sSecretary.getAllCount()>0){
	    				mSecretaryNum.setText(AppConstants.sSecretary.getAllCount()+"");
	    				mSecretaryNum.setVisibility(View.VISIBLE);
	    			}else{
	    				mSecretaryNum.setVisibility(View.INVISIBLE);
	    			}
	    		}
	    	}break;
	    	case MainBusi.GETONLINESUM:{
	    		mOnLineNum.setText(""+msg.arg1);
	    	}break;
			}
    	};
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), OnLineService.class);
        
        bindService(intent, new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				
			}
			
			@Override
			public void onServiceConnected(ComponentName arg0, IBinder binder) {
				OnLineService.OnLineBinder localBinder = (OnLineService.OnLineBinder)binder;
				localBinder.getOnLineService().setmHandler(mHandler);
			}
		}, 1);
//        startService(intent);
        
//        getActionBar().setDisplayHomeAsUpEnabled(false);
//        getActionBar().setTitle("111");
        
        mHeader = (ImageView)findViewById(R.id.index_header);
		mFullname = (TextView)findViewById(R.id.index_fullname);
		mPost = (TextView)findViewById(R.id.index_post);
		mDep = (TextView)findViewById(R.id.index_dep);
		
		mSecretaryNum = (TextView)findViewById(R.id.index_secretary_num_tv);
		mOnLineNum = (TextView)findViewById(R.id.index_online_num_tv);
		mOnLineNum.setText(""+AppConstants.onlinesum);
		
//		mWaitDealList = (ListView)findViewById(R.id.index_waitdeallist);
//		mWaitDealList_None = (TextView)findViewById(R.id.index_waitdeallist_none);
		
		mHeader.setImageDrawable(Utility.getDrawableFromSD(
				AppConstants.sReq_IndexUserHeaderInfo+AppConstants.loginname+"_head_160.jpg"));
		
		SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		mFullname.setText(sp.getString("fullname", "未知姓名"));
		mPost.setText(sp.getString("userpost", ""));
		mDep.setText(sp.getString("userdep", ""));
		
		
		
		mSecretaryBusi = new SecretaryBusi(mHandler);
		mSecretaryBusi.getTaskCount();
    }
    
    
    
    /**
     * 按钮点击
     * @param v
     */
    public void indexOnClick(View v){
    	switch(v.getId()){
    	case R.id.index_daily_btn:{
    		Intent intent = new Intent();
    		intent.setClass(getApplicationContext(), DailyActivity.class);
    		startActivity(intent);
    	}break;
    	case R.id.index_smallnote_btn:{
    		Intent intent = new Intent();
    		intent.setClass(getApplicationContext(), SmallNoteActivity.class);
    		startActivity(intent);
    	}break;
    	case R.id.index_info_btn:{
    		Intent intent = new Intent();
    		intent.setClass(getApplicationContext(), InfoListActivity.class);
    		startActivity(intent);
    	}break;
    	case R.id.index_secretary_btn:{
    		Intent intent = new Intent();
    		intent.setClass(getApplicationContext(), SecretaryActivity.class);
    		startActivity(intent);
    	}break;
    	case R.id.index_setting_btn:{
    		Intent intent = new Intent();
    		intent.setClass(getApplicationContext(), SettingActivity.class);
    		startActivity(intent);
    	}break;
    	}
    }


    @Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK){ 
	    	exitBy2Click();
	    }  
	    return false;  
	}
    
    
	private static Boolean isExit = false;  
	
	/** 
	 * 双击退出函数 
	 */  
	private void exitBy2Click() {  
	    Timer tExit = null;  
	    if (isExit == false) {  
	        isExit = true;
	        Toast.makeText(this, "再按一次退出到桌面", Toast.LENGTH_SHORT).show();  
	        tExit = new Timer();  
	        tExit.schedule(new TimerTask() {  
	            @Override  
	            public void run() {  
	                isExit = false;
	            }  
	        }, 1000);
	    } else {  
	    	Intent home = new Intent(Intent.ACTION_MAIN);  
	        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	        home.addCategory(Intent.CATEGORY_HOME);  
	        startActivity(home);
	    }  
	}

	@Override
	protected void onDestroy() {
		Intent intent = new Intent();
        intent.setClass(getApplicationContext(), OnLineService.class);
        stopService(intent);
		super.onDestroy();
	}
	
}
