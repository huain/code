/**
 * 
 */
package org.hjin.upoa.ui.view.calendar;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.CalendarBusi;
import org.hjin.upoa.util.DateUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

/**
 * @author huangjin
 * 2014-9-2
 */
public class CalendarView extends LinearLayout {
	 
	private LinearLayout mView;
	
	private Calendar mCalendar;
	
	private GridView mCalendarGridView;
	
	private View mCalendarGridViewLastSelectedView;
	
	private CalendarAdapter mCalendarAdapter;
	
	private CalendarBusi mCb;
	
	private CalendarPagerView mPagerView;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case CalendarBusi.GET_CARLENDAR_INFO:{
				MyDate[] dates = (MyDate[])msg.getData().getSerializable("dates");
				mCalendarAdapter.setDate(dates);
				int pos = mCalendarAdapter.getmTodayPosition();
				if(mCalendarGridView != null){
					mCalendarGridView.performItemClick(mCalendarGridView.getChildAt(pos), pos, mCalendarAdapter.getItemId(pos));
				}
			}break;
			default:break;
			}
		}
	};
	
	
	public CalendarView(Context context,AttributeSet attrs){
		super(context,attrs);
		init(context);
	}
	
	public CalendarView(Context context,CalendarPagerView pagerView){
		super(context);
		mPagerView = pagerView;
		init(context);
		
	}

	private void init(Context context){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = (LinearLayout)inflater.inflate(R.layout.calendar_pageview_item, this);
		mCalendarGridView = (GridView)mView.findViewById(R.id.gv_calendar);
		mCalendarGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				MyDate date = (MyDate)view.getTag(R.id.tag_first);
				if(date.isToday() > 0){
					return;
				}
				view.setBackgroundColor(getResources().getColor(R.color.ultra_light_blue));
				if (mCalendarGridViewLastSelectedView != null) {
					mCalendarGridViewLastSelectedView.setBackgroundColor(0x0000);
				}
				mCalendarGridViewLastSelectedView = view;
				mPagerView.dateOnClick(date);
			}
		});
		
		mCalendar = new Calendar(DateUtil.getYear(), DateUtil.getMonth());
		mCalendarAdapter = new CalendarAdapter(context, mCalendar.getDates());
		
		mCalendarGridView.setAdapter(mCalendarAdapter);
		mCb = new CalendarBusi(mCalendar.getDates(), mHandler);
	}
	
	public void setDate(int year,int month){
		mCalendar = new Calendar(year, month);
		if(mCalendarAdapter != null){
			mCalendarAdapter.setDate(mCalendar.getDates());
		}
		mCb.setDates(mCalendar.getDates());
		mCb.getCalendarInfo();
	}
	
	public void initDate(){
		mCb.getCalendarInfo();
	}
	
	//向右滑动
	public void rightSilde() {
		int month = CalendarPagerView.mShowDate.getmMonth();
		int year = CalendarPagerView.mShowDate.getmYear();
		if (month == 12) {
			CalendarPagerView.mShowDate.setmMonth(1);
			CalendarPagerView.mShowDate.setmYear(year+1);
		} else {
			CalendarPagerView.mShowDate.setmMonth(month+1);
		}
		setDate(CalendarPagerView.mShowDate.getmYear(),CalendarPagerView.mShowDate.getmMonth());
	}
	
	//向左滑动
	public void leftSilde() {
		int month = CalendarPagerView.mShowDate.getmMonth();
		int year = CalendarPagerView.mShowDate.getmYear();
		if (month == 1) {
			CalendarPagerView.mShowDate.setmMonth(12);
			CalendarPagerView.mShowDate.setmYear(year-1);
		} else {
			CalendarPagerView.mShowDate.setmMonth(month-1);
		}
		setDate(CalendarPagerView.mShowDate.getmYear(),CalendarPagerView.mShowDate.getmMonth());
	}
	
}
