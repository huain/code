package org.hjin.upoa.ui;

import org.hjin.upoa.R;
import org.hjin.upoa.push.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

public class BaseActivity extends Activity {
	
	protected boolean mDebugable = true;
	
	private final String apikey = "qyyG21wLBThXuQ7iEGrSzGwL";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		
		if(mDebugable){
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.detectDiskReads()
			.detectDiskWrites()
			.detectNetwork()
			.penaltyLog()
			.build());
			
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			.detectLeakedSqlLiteObjects()
			.penaltyLog()
			.build());
		}
		
//		if (!Utils.hasBind(getApplicationContext())) {
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                apikey);
            // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
            // PushManager.enableLbs(getApplicationContext());
//        }
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_base, menu);
		return true;
	}
	

}
