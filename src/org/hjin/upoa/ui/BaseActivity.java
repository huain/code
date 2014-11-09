package org.hjin.upoa.ui;

import org.hjin.upoa.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;

public class BaseActivity extends Activity {
	
	protected boolean mDebugable = true;
	
	private final String apikey = "qyyG21wLBThXuQ7iEGrSzGwL";
	
	protected DialogFragment mPdf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		
//		if(mDebugable){
//			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//			.detectDiskReads()
//			.detectDiskWrites()
//			.detectNetwork()
//			.penaltyLog()
//			.build());
//			
//			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//			.detectLeakedSqlLiteObjects()
//			.penaltyLog()
//			.build());
//		}
		
//		if (!Utils.hasBind(getApplicationContext())) {
//        PushManager.startWork(getApplicationContext(),
//                PushConstants.LOGIN_TYPE_API_KEY,
//                apikey);
            // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
            // PushManager.enableLbs(getApplicationContext());
//        }
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_base, menu);
		return true;
	}
	
	/**
	 * 根据tag，移除dialog
	 * @param tag
	 */
	protected void removeDialogByTag(String tag){
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
	protected void showDialog(DialogFragment df,String tag){
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag(tag);
	    if (prev != null) {
	    	ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    df.setCancelable(false);
	    df.show(ft, tag);
	}

}
