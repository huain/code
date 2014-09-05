/**
 * 
 */
package org.hjin.upoa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * @author huangjin
 * 2014-8-20
 */
public class DateUtil {
	
	/**
	 * 返回指定月份的天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthDays(int year, int month) {
		if (month > 12) {
			month = 1;
			year += 1;
		} else if (month < 1) {
			month = 12;
			year -= 1;
		}
		int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int days = 0;

		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			arr[1] = 29; // 闰年2月29天
		}

		try {
			days = arr[month - 1];
		} catch (Exception e) {
			e.getStackTrace();
		}

		return days;
	}
	
	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	public static int getCurrentMonthDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getWeekDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 返回指定月份第一天的星期
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getWeekDayFromDate(int year, int month) {
		return DateUtil.getWeekDayFromDate(year,month,1);
	}
	
	/**
	 * 返回指定日期的星期
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getWeekDayFromDate(int year, int month,int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.getDateFromString(year, month,day));
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}
	
	public static Date getDateFromString(int year, int month) {
		return getDateFromString(year,month,1);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static Date getDateFromString(int year, int month,int day){
		String dateString = year + "-" + (month > 9 ? month : ("0" + month))
				+ "-" + (day > 9 ? day : ("0" + day));
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return date;
	}
	
	public static Date getMondayOfWeek(){
		Calendar cal =Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
		Date date = null;
		date = cal.getTime();
		return date;
	}
	
	public static Date getSundayOfLastWeek(){
		Calendar cal =Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); 
		Date date = null;
		date = cal.getTime();
		return date;
	}
	
	

}
