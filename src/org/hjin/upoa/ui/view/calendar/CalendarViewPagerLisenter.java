package org.hjin.upoa.ui.view.calendar;

import org.hjin.upoa.R;
import org.hjin.upoa.ui.DailyActivity;

import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

public class CalendarViewPagerLisenter implements OnPageChangeListener {
	
	private TextView mYearView;
	
	private TextView mMonthView;

	private SildeDirection mDirection = SildeDirection.NO_SILDE;
	int mCurrIndex = 498;
	private CalendarView[] mShowViews;

	public CalendarViewPagerLisenter(View view,CustomViewPagerAdapter<CalendarView> viewAdapter) {
		super();
		this.mShowViews = viewAdapter.getAllItems();
		mYearView = (TextView)view.findViewById(R.id.tv_year);
		mMonthView = (TextView)view.findViewById(R.id.tv_month);
		mYearView.setText(Calendar.mShowDate.getmYear()+"��");
		mMonthView.setText(Calendar.mShowDate.getmMonth()+"");
		mShowViews[mCurrIndex % mShowViews.length].initDate();
	}
	
	@Override
	public void onPageSelected(int arg0) {
		measureDirection(arg0);
		updateCalendarView(arg0);
	}

	private void updateCalendarView(int arg0) {
		if(mDirection == SildeDirection.RIGHT){
			mShowViews[arg0 % mShowViews.length].rightSilde();
		}else if(mDirection == SildeDirection.LEFT){
			mShowViews[arg0 % mShowViews.length].leftSilde();
		}
		mYearView.setText(Calendar.mShowDate.getmYear()+"��");
		mMonthView.setText(Calendar.mShowDate.getmMonth()+"");
		mDirection = SildeDirection.NO_SILDE;
	}

	
	/**
	 * �жϻ�������
	 * @param arg0
	 */
	private void measureDirection(int arg0) {

		if (arg0 > mCurrIndex) {
			mDirection = SildeDirection.RIGHT;

		} else if (arg0 < mCurrIndex) {
			mDirection = SildeDirection.LEFT;
		}
		mCurrIndex = arg0;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}



	enum SildeDirection {
		RIGHT, LEFT, NO_SILDE;
	}
}