package org.hjin.upoa.busi;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.ui.view.calendar.MyDate;
import org.hjin.upoa.ui.view.calendar.MyDate.DateStatus;
import org.hjin.upoa.util.DateUtil;
import org.hjin.upoa.util.net.MyParameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 签到日历业务处理
 * @author Administrator
 *
 */
public class CalendarBusi extends BaseBusi {
	
	private final String TAG = "CalendarBusi";
	
	/**
	 * flag标识：取得日历信息
	 */
	public static final int GET_CARLENDAR_INFO = 0x0401;
	
	/**
	 * 初始条件date
	 */
	private MyDate[] mDates;
	
	public CalendarBusi(MyDate[] dates,Handler handler){
		mDates = dates;
		mHandler = handler;
	}
	
	public void setDates(MyDate[] dates){
		mDates = dates;
	}
	
	
	/**
	 * 获取日历信息
	 * @param dateJson
	 */
	public void getCalendarInfo(){
		/*
		 * {"prev":{"y":2014,"m":5,"s":26,"e":31},
		 * "current":{"y":2014,"m":6,"s":1,"e":30},
		 * "next":{"y":2014,"m":7,"s":1,"e":6}}
		 */
		if(mDates.length>0){
			MyParameters params = new MyParameters();
			params.add("dateJson", date2Json(mDates));
			Log.d(TAG, "===获取日历信息：初始条件JSON："+ params.getValue("dateJson"));
			params.add("header_referer", AppConstants.sReq_Calendar_Referer);
			request(GET_CARLENDAR_INFO, AppConstants.sReq_Calendar, params, HTTPMETHOD_POST, this);
		}
	};
	
	@Override
	public void onComplete(String response,int flag) {
		switch(flag){
		case GET_CARLENDAR_INFO:{
			Log.d(TAG, response);
			JSONObject jo = null;
			try {
				jo = new JSONObject(response);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(jo != null){
				JSONArray prevList = null;
				JSONArray currentList = null;
				JSONArray nextList = null;
				try {
					prevList = jo.getJSONArray("prev");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					currentList = jo.getJSONArray("current");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				try {
					nextList = jo.getJSONArray("next");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				int prevLength = prevList==null?0:prevList.length();
				int currentLength = currentList==null?0:currentList.length();
				int nextLength = nextList==null?0:nextList.length();
				int i=0;
				try {
					if((prevLength+currentLength+nextLength) == mDates.length){
						Message msg = mHandler.obtainMessage();
						msg.what = GET_CARLENDAR_INFO;
						Bundle data = msg.getData();
						if(prevLength>0){
							for(;i<prevLength;i++){
								MyDate date = mDates[i];
								date.setIsholiday(prevList.getJSONObject(i).getString("isholiday"));
								date.setIsdaily(prevList.getJSONObject(i).getString("isdaily"));
								date.setIscard(prevList.getJSONObject(i).getString("iscard"));
								date.setCardtime(prevList.getJSONObject(i).getString("cardtime"));
								date.setIsleave(prevList.getJSONObject(i).getString("isleave"));
								date.setIsevection(prevList.getJSONObject(i).getString("isevection"));
								date.updateStatus();
							}
						}
						
						if(currentLength>0){
							for(;i<prevLength+currentLength;i++){
								MyDate date = mDates[i];
								int j = i-prevLength;
								date.setIsholiday(currentList.getJSONObject(j).getString("isholiday"));
								date.setIsdaily(currentList.getJSONObject(j).getString("isdaily"));
								date.setIscard(currentList.getJSONObject(j).getString("iscard"));
								date.setCardtime(currentList.getJSONObject(j).getString("cardtime"));
								date.setIsleave(currentList.getJSONObject(j).getString("isleave"));
								date.setIsevection(currentList.getJSONObject(j).getString("isevection"));
								date.updateStatus();
							}
						}
						
						if(nextLength>0){
							for(;i<prevLength+currentLength+nextLength;i++){
								MyDate date = mDates[i];
								int k = i - prevLength-currentLength;
								date.setIsholiday(nextList.getJSONObject(k).getString("isholiday"));
								date.setIsdaily(nextList.getJSONObject(k).getString("isdaily"));
								date.setIscard(nextList.getJSONObject(k).getString("iscard"));
								date.setCardtime(nextList.getJSONObject(k).getString("cardtime"));
								date.setIsleave(nextList.getJSONObject(k).getString("isleave"));
								date.setIsevection(nextList.getJSONObject(k).getString("isevection"));
								date.updateStatus();
							}
						}
						
						data.putSerializable("dates", mDates);
						msg.sendToTarget();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}break;
		default:break;
		}
	}

	/**
	 * MyDate转换为Json
	 * @param dates
	 * @return
	 */
	private String date2Json(MyDate[] dates){
		if(dates == null || dates.length == 0){
			return "";
		}
		/*
		 * {"prev":{"y":2014,"m":5,"s":26,"e":31},
		 * "current":{"y":2014,"m":6,"s":1,"e":30},
		 * "next":{"y":2014,"m":7,"s":1,"e":6}}
		 */
		StringBuffer sb = new StringBuffer("");
		sb.append("{\"prev\":{\"y\":");
		int lastNum = 0;
		int currNum = 0;
		//int nextNum = 0;
		int length = dates.length;		
		if(dates[0].getStatus() == DateStatus.CurrentMonthDay){
			currNum = DateUtil.getMonthDays(dates[0].getmYear(), dates[0].getmMonth());
			sb.append("\"\",\"m\":\"\",\"s\":\"\",\"e\":\"\"},");
		}else{
			lastNum = DateUtil.getMonthDays(dates[0].getmYear(), dates[0].getmMonth());
			currNum = DateUtil.getMonthDays(dates[length/2].getmYear(), dates[length/2].getmMonth());
			sb.append(dates[0].getmYear());
			sb.append(",\"m\":");
			sb.append(dates[0].getmMonth());
			sb.append(",\"s\":");
			sb.append(dates[0].getmDay());
			sb.append(",\"e\":");
			sb.append(lastNum);
			sb.append("},");
		}
		sb.append("\"current\":{\"y\":");
		sb.append(dates[length/2].getmYear());
		sb.append(",\"m\":");
		sb.append(dates[length/2].getmMonth());
		sb.append(",\"s\":1,\"e\":");
		sb.append(currNum);
		sb.append("},");
		
		if(dates[length-1].getStatus() == DateStatus.CurrentMonthDay){
			sb.append("{\"next\":{\"y\":\"\",\"m\":\"\",\"s\":\"\",\"e\":\"\"}}");
		}else{
			sb.append("\"next\":{\"y\":");
			sb.append(dates[length-1].getmYear());
			sb.append(",\"m\":");
			sb.append(dates[length-1].getmMonth());
			sb.append(",\"s\":1,\"e\":");
			sb.append(dates[length-1].getmDay());
			sb.append("}}");
		}
		return sb.toString();
	}

}
