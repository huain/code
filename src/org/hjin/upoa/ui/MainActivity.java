package org.hjin.upoa.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.LoginBusi;
import org.hjin.upoa.busi.MainBusi;
import org.hjin.upoa.busi.SecretaryBusi;
import org.hjin.upoa.busi.SmallNoteBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.Secretary;
import org.hjin.upoa.service.OnLineService;
import org.hjin.upoa.util.Utility;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	
	private final String TAG = "MainActivity";
	
	private SecretaryBusi mSecretaryBusi;
	
	private LoginBusi mLoginBusi;
	
	private ImageView mHeader;
	
	private TextView mFullname;
	
	private TextView mPost;
	
	private TextView mDep;
	
	private TextView mSecretaryNum;
	
	private TextView mOnLineNum;
	
	private View mUpdateRed;
	
//	private ListView mWaitDealList;
//	
//	private TextView mWaitDealList_None;
	
	private SharedPreferences sp ;

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
	    		if(msg.arg2 == 1){
	    			shake(mOnLineNum);
	    		}
	    	}break;
	    	case LoginBusi.GET_VERSION_INFO:{
	    		if(msg.arg1 == 1){
	    			mUpdateRed.setVisibility(View.VISIBLE);
	    			showUpdate();
	    		}else{
	    			Toast.makeText(getApplicationContext(), "��ǰ�Ѿ������°汾��", Toast.LENGTH_SHORT).show();
	    		}
	    	}break;
	    	case SmallNoteBusi.SAVESMALLNOTE:{
	    		if(msg.arg1 == 1){
	    			Toast.makeText(getApplicationContext(), "����ɹ���лл���ķ���", Toast.LENGTH_SHORT).show();
	    		}else{
	    			Toast.makeText(getApplicationContext(), "���ź�������ʧ���ˣ�������һ�������һ�Σ�", Toast.LENGTH_SHORT).show();
	    		}
	    	}break;
			}
    	};
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        mHeader = (ImageView)findViewById(R.id.index_header);
		mFullname = (TextView)findViewById(R.id.index_fullname);
		mPost = (TextView)findViewById(R.id.index_post);
		mDep = (TextView)findViewById(R.id.index_dep);
		
		mUpdateRed = findViewById(R.id.index_update_red);
		
		mSecretaryNum = (TextView)findViewById(R.id.index_secretary_num_tv);
		mOnLineNum = (TextView)findViewById(R.id.index_online_num_tv);
		mOnLineNum.setText(""+AppConstants.onlinesum);
        
        // ��־Ӧ���Ƿ��и��� 
        int isHasUpdate = sp.getInt("isHasUpdate", 0);
        if(isHasUpdate != 0){
        	mUpdateRed.setVisibility(View.VISIBLE);
        }
        
        // ������������ͳ��service
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
        
		// ��ʾ�û�������Ϣ
		mHeader.setImageDrawable(Utility.getDrawableFromSD(
				AppConstants.sReq_IndexUserHeaderInfo+AppConstants.loginname+"_head_160.jpg"));
		
		
		mFullname.setText(sp.getString("fullname", "δ֪����"));
		mPost.setText(sp.getString("userpost", ""));
		mDep.setText(sp.getString("userdep", ""));
		
		
		// ��ȡ������Ŀ
		mSecretaryBusi = new SecretaryBusi(mHandler);
		mSecretaryBusi.getTaskCount();
		
		mLoginBusi = new LoginBusi(getApplicationContext(), mHandler);
    }
    
    
    
    /**
     * ��ť���
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
    	case R.id.index_update_btn:{
    		int version = sp.getInt("version", 0);
    		int version_packInfo = 0;
			PackageInfo packInfo;
			try {
				packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				version_packInfo = packInfo.versionCode;
			} catch (NameNotFoundException e) {
			}
			if(version != version_packInfo){
				mLoginBusi.getVersionInfo();
			}else{
				Toast.makeText(getApplicationContext(), "��ǰ�Ѿ������°汾��", Toast.LENGTH_SHORT).show();
			}
    	}break;
    	case R.id.index_share_btn:{
    		share();
    	}break;
    	}
    }
    
    private void showUpdate(){
    	String versionName = sp.getString("versionName", "");
    	final String link = sp.getString("link", "");
    	String updateDesc = sp.getString("updateDesc", "��������");
    	new AlertDialog.Builder(this).setTitle("UOA �汾"+versionName+"����")
		.setMessage(updateDesc)
		.setPositiveButton("�������", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link)); 
				startActivity(intent);
			}
		})
		.setNegativeButton("�ݲ�����", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences.Editor editor = sp.edit();
				editor.putInt("isHasUpdate", 0);
				editor.commit();
				mUpdateRed.setVisibility(View.GONE);
			}
		})
		.show();
    }
    
    private void share(){
    	final String content = "������ʹ��UOA��̩��OA��V2.0Beta����Ҳ�����԰�~!�������ӣ�http://pan.baidu.com/s/1kTv9S9p";
    	new AlertDialog.Builder(this).setTitle("����С�ֱ�")
		.setMessage(content)
		.setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SmallNoteBusi sb = new SmallNoteBusi(mHandler);
				sb.saveSmallNote("������ʹ��UOA��С�ֱ�����Ҳ�����԰�~", content);
			}
		})
		.show();
    }
    
    // ���������仯ʱ��
    private void shake(View view){
    	int numOfShakes = 24;
    	long duration = 360;
    	float shakeDistance = 8f;
    	TimeInterpolator interpolator = new AccelerateDecelerateInterpolator();
    	long singleShakeDuration = duration / numOfShakes / 2;
		if (singleShakeDuration == 0)
			singleShakeDuration = 1;
		final AnimatorSet shakeAnim = new AnimatorSet();
		shakeAnim
				.playSequentially(ObjectAnimator.ofFloat(view,
						View.TRANSLATION_X, shakeDistance), ObjectAnimator
						.ofFloat(view, View.TRANSLATION_X, -shakeDistance),
						ObjectAnimator.ofFloat(view, View.TRANSLATION_X,
								shakeDistance), ObjectAnimator.ofFloat(view,
								View.TRANSLATION_X, 0));
		shakeAnim.setInterpolator(interpolator);
		shakeAnim.setDuration(singleShakeDuration);
		shakeAnim.start();
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
	 * ˫���˳����� 
	 */  
	private void exitBy2Click() {  
	    Timer tExit = null;  
	    if (isExit == false) {  
	        isExit = true;
	        Toast.makeText(this, "�ٰ�һ���˳�������", Toast.LENGTH_SHORT).show();  
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
