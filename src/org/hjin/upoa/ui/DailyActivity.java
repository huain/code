/**
 * 
 */
package org.hjin.upoa.ui;

import org.hjin.upoa.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
//import org.hjin.upoa.ui.view.CalendarView;

/**
 * @author huangjin
 * 2014-8-14
 */
public class DailyActivity extends BaseActivity {
	
	private final String TAG = "DailyActivity";
	
//	private CalendarView mCalendarView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_daily, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_dailylist:{
			Intent i = new Intent();
			i.setClass(this, DailyListActivity.class);
			startActivity(i);
		}break;
		default:break;
		}
		return true;
	}
	
	

}
