/**
 * 
 */
package org.hjin.upoa.ui.view.calendar;

import org.hjin.upoa.util.DateUtil;


/**
 * @author huangjin
 * 2014-9-2
 */
public class Calendar {
	
	public static MyDate mShowDate;//自定义的日期  包括year month day
	
	/** 当前所要展示的日期数据 （6*7） */
	private MyDate[] mDates = new MyDate[42]; 
	
	public static MyDate sToday = new MyDate(DateUtil.getYear(),DateUtil.getMonth(),DateUtil.getCurrentMonthDay());
	
	private int mYear;
	
	private int mMonth;
	
	public Calendar(int year,int month){
		this.mYear = year;
		this.mMonth = month;
		init();
	}
	
	private void init(){
		int currentMonthDays = DateUtil.getMonthDays(mYear, mMonth);
		int lastMonth = mMonth-1;
		int lastMonthYear = mYear;
		int nextMonth = mMonth +1;
		int nextMonthYear = mYear;
		if(lastMonth == 0){
			lastMonth = 12;
			lastMonthYear = mYear -1;
		}
		if(nextMonth == 13){
			nextMonth = 1;
			nextMonthYear = mYear +1;
		}
		int lastMonthDays = DateUtil.getMonthDays(lastMonthYear, lastMonth);
		int firstDayWeek = DateUtil.getWeekDayFromDate(mYear, mMonth);
		
		for(int i=0;i<42;i++){
			MyDate date = null;
			if(i>=firstDayWeek && i<(firstDayWeek+currentMonthDays)){
				date = new MyDate(mYear,mMonth,i-firstDayWeek+1);
				date.setStatus(MyDate.DateStatus.CurrentMonthDay);
//				if(date.getmDay() == DateUtil.getCurrentMonthDay()){
//					//date.setStatus(MyDate.DateStatus.ClickDay);
////					mClickDateIndex = i;
//				}else{
//					date.setStatus(MyDate.DateStatus.CurrentMonthDay);
//				}
			}else if(i<firstDayWeek){
				date = new MyDate(lastMonthYear,lastMonth,lastMonthDays-firstDayWeek+i+1);
				date.setStatus(MyDate.DateStatus.LastMonthDay);
			}else if(i>=(firstDayWeek+currentMonthDays)){
				date = new MyDate(nextMonthYear,nextMonth,i-firstDayWeek-currentMonthDays+1);
				date.setStatus(MyDate.DateStatus.NextMonthDay);
			}
			mDates[i] = date;
		}
	}
	
	public MyDate[] getDates(){
		return mDates;
	}
	
	
//	private voi
	

}
