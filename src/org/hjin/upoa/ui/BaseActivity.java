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
            // Push: �������ڵ���λ�����ͣ����Դ�֧�ֵ���λ�õ����͵Ŀ���
            // PushManager.enableLbs(getApplicationContext());
//        }
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_base, menu);
		return true;
	}
	
	/**
	 * ����tag���Ƴ�dialog
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
	 * ��ʾdialog
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
