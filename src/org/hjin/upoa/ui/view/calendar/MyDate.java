/**
 * 
 */
package org.hjin.upoa.ui.view.calendar;

import java.io.Serializable;
import java.util.Date;

import org.hjin.upoa.util.DateUtil;

/**
 * @author huangjin
 * 2014-8-20
 */
public class MyDate implements Serializable{
	
	private static MyDate mToday = new MyDate(DateUtil.getYear(),DateUtil.getMonth(),DateUtil.getCurrentMonthDay());
	
	private int mYear;
	
	private int mMonth;
	
	private int mDay;
	
	private int mWeek;
	
	private DateStatus status;
	
	/** 日报状态*/
	private DailyStatus dailyStatus;
	/** 签到状态*/
	private SignStatus signStatus;
	
	
	private String isholiday;
	private String isdaily;
	private String iscard;
	private String cardtime;
	private String isleave;
	private String isevection;
	
	public MyDate(int year,int month,int day){
		this.mYear = year;
		this.mMonth = month;
		this.mDay = day;
		this.mWeek = DateUtil.getWeekDayFromDate(mYear, mMonth, mDay);
	}
	
	@SuppressWarnings("deprecation")
	public MyDate(Date date){
		this(date.getYear(),date.getMonth(),date.getDate());
	}
	
	public void updateStatus(){
		if(this.isAfter(Calendar.sToday)){
			return;
		}
		
		if(this.isholiday.equals("0") && 
				this.isleave.equals("0") &&
				this.isdaily.equals("0")){
			setDailyStatus(DailyStatus.No);
		}else if(this.isholiday.equals("0") && 
				this.isleave.equals("0") &&
				this.isdaily.equals("1")){
			setDailyStatus(DailyStatus.Yes);
		}
		
		if(this.isholiday.equals("0") && 
				this.isleave.equals("0") &&
				this.iscard.equals("0")){
			setSignStatus(SignStatus.Normal);
		}else if(this.isholiday.equals("0") && 
					this.isleave.equals("0") &&
					!this.iscard.equals("0")){
			setSignStatus(SignStatus.AbNormal);
		}
	}
	
	public String getDateString(){
		return mYear + "-" + (mMonth > 9 ? mMonth : ("0" + mMonth))
				+ "-" + (mDay > 9 ? mDay : ("0" + mDay));
	}
	
	public String getDailyInfo(){
		String info = "";
		if(this.isholiday.equals("1")){
			info = "法定假期";
			return info;
		}
		if(this.isleave.equals("1")){
			info = "已请假";
			return info;
		}
		if(this.isdaily.equals("0")){
			info = "未填写日志，请填写";
		}else{
			info = "已填写";
		}
		return info;
	}
	
	public String getSignInfo(){
		String info = "";
		if(this.isholiday.equals("1")){
			info = "法定假期";
			return info;
		}
		if(this.isleave.equals("1")){
			info = "已请假";
			return info;
		}
		if(this.iscard.equals("4")&& this.cardtime.equals("未打卡") ){
			info = "未签到";
		}else if(this.iscard.equals("0")){
			info = "已签到("+ this.cardtime +")";
		}else if(this.iscard.equals("1")){
			info = "迟到5分以内("+ this.cardtime +")";
		}else if(this.iscard.equals("2")){
			info = "迟到5-15分("+ this.cardtime +")";
		}else if(this.iscard.equals("4") || this.iscard.equals("3")){
			info = "迟到15分以上("+ this.cardtime +")";
		}
		return info;
	}
	
	public boolean getDailyBtn(){
		if(this.isAfter(mToday)){
			return false;
		}
		if(this.isAfter(new MyDate(DateUtil.getSundayOfLastWeek()))){
			if(this.isholiday.equals("0") && 
					this.isleave.equals("0") &&
					this.isdaily.equals("0")){
				return true;
			}
		}
		return false;
	}
	
	
	public boolean isAfter(MyDate date){
		if(this.mYear>date.mYear){
			return true;
		}else if(this.mYear>=date.mYear && this.mMonth>date.mMonth){
			return true;
		}else if(this.mYear>=date.mYear && this.mMonth>=date.mMonth && this.mDay > date.mDay)
			return true;
		else
			return false;
	}
	
	public int isToday(){
		if(this.mYear == DateUtil.getYear() && this.mMonth == DateUtil.getMonth() && this.mDay == DateUtil.getCurrentMonthDay()){
			return 0;
		}else if(this.isAfter(mToday)){
			return 1;
		}else{
			return -1;
		}
	}
	
	

	public int getmYear() {
		return mYear;
	}

	public void setmYear(int mYear) {
		this.mYear = mYear;
	}

	public int getmMonth() {
		return mMonth;
	}

	public void setmMonth(int mMonth) {
		this.mMonth = mMonth;
	}

	public int getmDay() {
		return mDay;
	}

	public void setmDay(int mDay) {
		this.mDay = mDay;
	}

	public int getmWeek() {
		return mWeek;
	}

	public void setmWeek(int mWeek) {
		this.mWeek = mWeek;
	}
	
	public DateStatus getStatus() {
		return status;
	}

	public void setStatus(DateStatus status) {
		this.status = status;
	}

	

	public DailyStatus getDailyStatus() {
		return dailyStatus;
	}



	public void setDailyStatus(DailyStatus dailyStatus) {
		this.dailyStatus = dailyStatus;
	}



	public SignStatus getSignStatus() {
		return signStatus;
	}



	public void setSignStatus(SignStatus signStatus) {
		this.signStatus = signStatus;
	}
	
	public String getIsholiday() {
		return isholiday;
	}
	public void setIsholiday(String isholiday) {
		this.isholiday = isholiday;
	}
	public String getIsdaily() {
		return isdaily;
	}
	public void setIsdaily(String isdaily) {
		this.isdaily = isdaily;
	}
	public String getIscard() {
		return iscard;
	}
	public void setIscard(String iscard) {
		this.iscard = iscard;
	}
	public String getCardtime() {
		return cardtime;
	}
	public void setCardtime(String cardtime) {
		this.cardtime = cardtime;
	}
	public String getIsleave() {
		return isleave;
	}
	public void setIsleave(String isleave) {
		this.isleave = isleave;
	}
	public String getIsevection() {
		return isevection;
	}
	public void setIsevection(String isevection) {
		this.isevection = isevection;
	}



	/**
	 * 定义了日历显示的四种状态，上月的日期，当前月日期，下月日期，选中日期
	 * @author huangjin
	 * 2014-8-20
	 */
	public enum DateStatus{
		LastMonthDay,CurrentMonthDay,NextMonthDay
	}
	
	public enum DailyStatus{
		Yes,No
	}
	
	public enum SignStatus{
		Normal,AbNormal
	}
	

}
