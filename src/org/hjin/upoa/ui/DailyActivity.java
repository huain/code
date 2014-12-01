/**
 * 
 */
package org.hjin.upoa.ui;

import org.hjin.upoa.R;
import org.hjin.upoa.ui.view.ProgressDialogFragment;
import org.hjin.upoa.ui.view.calendar.Calendar;
import org.hjin.upoa.ui.view.calendar.CalendarAdapter.IDateOnClickListener;
import org.hjin.upoa.ui.view.calendar.CalendarView;
import org.hjin.upoa.ui.view.calendar.CalendarView.ICalendarDataLoadListener;
import org.hjin.upoa.ui.view.calendar.CalendarViewPagerLisenter;
import org.hjin.upoa.ui.view.calendar.CustomViewPagerAdapter;
import org.hjin.upoa.ui.view.calendar.MyDate;
import org.hjin.upoa.util.DateUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
//import org.hjin.upoa.ui.view.CalendarView;

/**
 * @author huangjin
 * 2014-8-14
 */
public class DailyActivity extends BaseActivity  implements IDateOnClickListener,ICalendarDataLoadListener{
	
	private final String TAG = "DailyActivity";
	
	
	public final int REQ_CODE = 1;
	
//	private CalendarView mCalendarView;
	
	
	private LinearLayout mView;
	
	private Context mContext;
	
	private int count = 5;
	
	private TextView mDailyInfo;
	
	private TextView mSignInfo;
	
	private Button mDailyBtn;
	
	private TextView mDate;
	
	private View mCalendarInfo;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily);
		getActionBar().setTitle("øº«⁄");
		init();
	}
	
	private void init(){
		Calendar.mShowDate = new MyDate(DateUtil.getYear(), DateUtil.getMonth(), DateUtil.getCurrentMonthDay());
		mDate = (TextView)findViewById(R.id.calendar_date_show);
		
		mCalendarInfo = findViewById(R.id.calendar_info);
		mDailyInfo = (TextView)findViewById(R.id.calendar_dailyinfotv);
		mSignInfo = (TextView)findViewById(R.id.calendar_signinfotv);
		mDailyBtn = (Button)findViewById(R.id.calendar_write);
		
		ViewPager viewPager = (ViewPager)findViewById(R.id.calendar_viewpager);
		
		CalendarView[] views = new CalendarView[count];
		for(int i = 0; i < count;i++){
			views[i] = new CalendarView(this,this,this);
		}
		
		CustomViewPagerAdapter<CalendarView> viewPagerAdapter = new CustomViewPagerAdapter<CalendarView>(views);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(498); 
		viewPager.setOnPageChangeListener(new CalendarViewPagerLisenter(findViewById(R.id.daily_calendar),viewPagerAdapter));
		
	}

	@Override
	public void dateOnClick(final MyDate mydate) {
		if(mydate == null){
			return;
		}
		mDate.setText(mydate.getDateString());
		mDailyInfo.setText(mydate.getDailyInfo());
		mSignInfo.setText(mydate.getSignInfo());
		
		if(mCalendarInfo.getVisibility() == View.GONE){
			mCalendarInfo.setVisibility(View.VISIBLE);
		}
		if(mydate.getDailyBtn()){
			mDailyBtn.setVisibility(View.VISIBLE);
			mDailyBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), DailyWriteActivity.class);
					int[] date = new int[3];
					date[0] = mydate.getmYear();
					date[1] = mydate.getmMonth();
					date[2] = mydate.getmDay();
					intent.putExtra("date", date);
//					startActivity(intent);
					startActivityForResult(intent, REQ_CODE);
				}
			});
		}else{
			mDailyBtn.setVisibility(View.GONE);
		}
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQ_CODE && resultCode == RESULT_OK){
//			init();
		}
		
	}

	@Override
	public void onPreDataLoad() {
		mPdf = ProgressDialogFragment.newInstance("º”‘ÿ÷–°≠°≠");
	    showDialog(mPdf, "loading");
	}

	@Override
	public void onFinishDataLoad() {
		removeDialogByTag("loading");
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
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	

}
