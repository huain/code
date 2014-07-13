package org.hjin.upoa.busi;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.Calendar;
import org.hjin.upoa.model.NewsInfo;
import org.hjin.upoa.util.Utility;
import org.hjin.upoa.util.net.MyParameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
	
	public static final int GET_CARLENDAR_INFO = 1;
	
	public CalendarBusi(Handler handler){
		super(handler);
	}
	
	
	/**
	 * 获取日历信息
	 * @param dateJson
	 */
	public void getCalendarInfo(String dateJson){
		MyParameters params = new MyParameters();
		params.add("dateJson", dateJson);
		params.add("header_referer", AppConstants.sReq_NewsInfo_Referer);
		request(GET_CARLENDAR_INFO, AppConstants.sReq_NewsInfo, params, HTTPMETHOD_GET, this);
		
	};
	

	@Override
	public void onComplete(String response,int flag) {
		Message msg = mHandler.obtainMessage();
		switch(flag){
		case GET_CARLENDAR_INFO:{
			msg.what = GET_CARLENDAR_INFO;
			Bundle data = new Bundle();
			try {
				JSONObject jo = new JSONObject(response);
				Calendar c = new Calendar();
				if(jo != null){
					JSONArray prevList = jo.getJSONArray("prev");
					JSONArray currentList = jo.getJSONArray("current");
					JSONArray nextList = jo.getJSONArray("next");
					if(prevList != null && prevList.length()>0){
						List<Calendar.Date> prev = new ArrayList<Calendar.Date>();
						for(int i=0;i<prevList.length();i++){
							Calendar.Date date = c.new Date(
									prevList.getJSONObject(i).getString("isholiday"),
									prevList.getJSONObject(i).getString("isdaily"),
									prevList.getJSONObject(i).getString("iscard"),
									prevList.getJSONObject(i).getString("cardtime"),
									prevList.getJSONObject(i).getString("isleave"),
									prevList.getJSONObject(i).getString("isevection"));
 							prev.add(date);
							
						}
						c.setPrev(prev);
					}
					
					if(currentList != null && currentList.length()>0){
						List<Calendar.Date> current = new ArrayList<Calendar.Date>();
						for(int i=0;i<currentList.length();i++){
							Calendar.Date date = c.new Date(
									currentList.getJSONObject(i).getString("isholiday"),
									currentList.getJSONObject(i).getString("isdaily"),
									currentList.getJSONObject(i).getString("iscard"),
									currentList.getJSONObject(i).getString("cardtime"),
									currentList.getJSONObject(i).getString("isleave"),
									currentList.getJSONObject(i).getString("isevection"));
 							current.add(date);
							
						}
						c.setCurrent(current);
					}
					
					if(nextList != null && nextList.length()>0){
						List<Calendar.Date> next = new ArrayList<Calendar.Date>();
						for(int i=0;i<prevList.length();i++){
							Calendar.Date date = c.new Date(
									nextList.getJSONObject(i).getString("isholiday"),
									nextList.getJSONObject(i).getString("isdaily"),
									nextList.getJSONObject(i).getString("iscard"),
									nextList.getJSONObject(i).getString("cardtime"),
									nextList.getJSONObject(i).getString("isleave"),
									nextList.getJSONObject(i).getString("isevection"));
 							next.add(date);
						}
						c.setNext(next);
					}
					
					data.putSerializable("data", (Serializable)c);
					msg.setData(data);
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}break;
		default:break;
		}
		msg.sendToTarget();
	}

	@Override
	public void onComplete4binary(ByteArrayOutputStream responseOS,int flag) {

	}

}
