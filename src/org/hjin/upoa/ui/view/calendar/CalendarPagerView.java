/**
 * 
 */
package org.hjin.upoa.ui.view.calendar;

import org.hjin.upoa.R;
import org.hjin.upoa.ui.DailyWriteActivity;
import org.hjin.upoa.ui.view.calendar.CalendarAdapter.IDateOnClickListener;
import org.hjin.upoa.util.DateUtil;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author huangjin
 * 2014-9-5
 */
public class CalendarPagerView extends LinearLayout implements IDateOnClickListener{
	
	public static MyDate mShowDate;//自定义的日期  包括year month day
	
	private LinearLayout mView;
	
	private Context mContext;
	
	private int count = 5;
	
	private TextView mDailyInfo;
	
	private TextView mSignInfo;
	
	private Button mDailyBtn;
	
	private TextView mDate;
	
	private View mCalendarInfo;
	

	public CalendarPagerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init(){
		mShowDate = new MyDate(DateUtil.getYear(), DateUtil.getMonth(), DateUtil.getCurrentMonthDay());
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = (LinearLayout)inflater.inflate(R.layout.calendar, this);
		mDate = (TextView)mView.findViewById(R.id.calendar_date_show);
		
		mCalendarInfo = mView.findViewById(R.id.calendar_info);
		mDailyInfo = (TextView)mView.findViewById(R.id.calendar_dailyinfotv);
		mSignInfo = (TextView)mView.findViewById(R.id.calendar_signinfotv);
		mDailyBtn = (Button)mView.findViewById(R.id.calendar_write);
		
		ViewPager viewPager = (ViewPager)mView.findViewById(R.id.calendar_viewpager);
		
		CalendarView[] views = new CalendarView[count];
		for(int i = 0; i < count;i++){
			views[i] = new CalendarView(mContext,this);
		}
		
		CustomViewPagerAdapter<CalendarView> viewPagerAdapter = new CustomViewPagerAdapter<CalendarView>(views);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(498); 
		viewPager.setOnPageChangeListener(new CalendarViewPagerLisenter(this,viewPagerAdapter));
		
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
					intent.setClass(getContext(), DailyWriteActivity.class);
					int[] date = new int[3];
					date[0] = mydate.getmYear();
					date[1] = mydate.getmMonth();
					date[2] = mydate.getmDay();
					intent.putExtra("date", date);
					mContext.startActivity(intent);
				}
			});
		}else{
			mDailyBtn.setVisibility(View.GONE);
		}
	}
	
	

}
