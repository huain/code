/**
 * 
 */
package org.hjin.upoa.ui;

import java.util.Date;

import org.hjin.upoa.R;
import org.hjin.upoa.ui.view.CalendarView;

import android.os.Bundle;
import android.view.View;

/**
 * @author huangjin
 * 2014-8-14
 */
public class DailyActivity extends BaseActivity {
	
	private final String TAG = "DailyActivity";
	
	private CalendarView mCalendarView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily);
		
		
	}
	
	

}
