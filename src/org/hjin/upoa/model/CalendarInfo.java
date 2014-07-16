package org.hjin.upoa.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 日历类
 * @author hjin1987
 *
 */
public class CalendarInfo {
	
	private List<Date> prev;
	
	private List<Date> current;
	
	private List<Date> next;
	
	
	public class Date{
		private String isholiday;
		private String isdaily;
		private String iscard;
		private String cardtime;
		private String isleave;
		private String isevection;
		
		
		/**
		 * 构造方法
		 * @param isholiday
		 * isholiday;
		 * @param isdaily
		 * isdaily;
		 * @param iscard
		 * iscard;
		 * @param cardtime
		 * cardtime;
		 * @param isleave
		 * isleave;
		 * @param isevection
		 * isevection
		 */
		public Date(String isholiday, String isdaily, String iscard,
				String cardtime, String isleave, String isevection) {
			super();
			this.isholiday = isholiday;
			this.isdaily = isdaily;
			this.iscard = iscard;
			this.cardtime = cardtime;
			this.isleave = isleave;
			this.isevection = isevection;
		}

		/**
		 * 取得当前日期的签到类型
		 * @return 
		 * 返回byte
		 * 0：未知类型
		 * 1：未填写日志；
		 * 2：请假；
		 * 3：休；
		 * 4：已签到；
		 * 5：迟到5分以内；
		 * 6：迟到5~15分；
		 * 7：迟到15分以上；
		 * 8：未签到；
		 * 9：出差；
		 * 
		 */
		public byte getDateType(){
			if(this.isholiday.equals("0") && 
					this.isleave.equals("0") &&
					this.isdaily.equals("0")){
				return 1;
			}else if(this.isleave.equals("1")){
				return 2;
			}else if(this.isholiday.equals("1")){
				return 3;
			}else if(this.iscard.equals("0")){
				return 4;
			}else if(this.iscard.equals("1")){
				return 5;
			}else if(this.iscard.equals("2")){
				return 6;
			}else if(this.iscard.equals("4") && !this.iscard.equals("未打卡")){
				return 7;
			}else if(this.iscard.equals("4")){
				return 8;
			}else if(this.isevection.equals("1")){
				return 9;
			}
			return 0;
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
		
	}


	public List<Date> getPrev() {
		return prev;
	}


	public void setPrev(List<Date> prev) {
		this.prev = prev;
	}


	public List<Date> getCurrent() {
		return current;
	}


	public void setCurrent(List<Date> current) {
		this.current = current;
	}


	public List<Date> getNext() {
		return next;
	}


	public void setNext(List<Date> next) {
		this.next = next;
	}


}
