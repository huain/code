package org.hjin.upoa.ui;

import org.hjin.upoa.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class BaseActivity extends Activity {
	
	protected boolean mDebugable = true;
	
	private final String apikey = "qyyG21wLBThXuQ7iEGrSzGwL";

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

}
