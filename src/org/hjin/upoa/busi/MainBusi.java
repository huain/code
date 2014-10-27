package org.hjin.upoa.busi;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.util.Utility;
import org.hjin.upoa.util.net.MyHttpException;
import org.hjin.upoa.util.net.MyParameters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 *  首页业务
 * @author Administrator
 *
 */
public class MainBusi extends BaseBusi {
	
	private final String TAG = "IndexBusi";
	/**取得用户信息flag*/
	public static final int GETUSERINFO = 0x0201;
	/**取得用户头像flag*/
	public static final int GETUSERHEADERINFO = 0x0202;
	/**取得用户头像flag失败*/
	public static final int GETUSERHEADERINFO_F = 0x0203;
	/**取得当前在线人数flag*/
	public static final int GETONLINESUM = 0x0204;
	/**取得当前待办数量flag*/
	public static final int GETWAITDEALSUM = 0x0205;
	/**取得最近日报信息flag*/
	public static final int GETDAILYINFO = 0x0206;
	/**取得最新待办flag*/
	public static final int GETWAITDEALINFO = 0x0207;
	
	
	public MainBusi(Context context, Handler handler) {
		super(context,handler);
	}

	/**
	 * 取得用户的基本信息
	 */
	public void getUserInfo(){
		MyParameters params = new MyParameters();
		params.add("usercode", "");
		params.add("header_referer", AppConstants.sReq_IndexUserInfo_Referer);
		request(GETUSERINFO, AppConstants.sReq_IndexUserInfo_Referer, params, HTTPMETHOD_POST, this);
	}
	
	/**
	 * 取得用户头像
	 */
	public void getUserHeaderInfo(){
		MyParameters params = new MyParameters();
		request4Binary(GETUSERHEADERINFO, AppConstants.sReq_IndexUserHeaderInfo+AppConstants.loginname+"_head_160.jpg", params, HTTPMETHOD_POST, this);
	};
	
	/**
	 * 取得当前在线用户数量
	 */
	public void getOnLineSum(){
		MyParameters params = new MyParameters();
		params.add("header_referer", AppConstants.sReq_IndexOnLineSum_Referer);
		request(GETONLINESUM, AppConstants.sReq_IndexOnLineSum, params, HTTPMETHOD_POST, this);
	}
	
	/**
	 * 取得当前待办数量
	 */
	public void getWaitDealSum(){
		MyParameters params = new MyParameters();
		params.add("header_referer", AppConstants.sReq_IndexWaitDealSum_Referer);
		request(GETWAITDEALSUM, AppConstants.sReq_IndexWaitDealSum, params, HTTPMETHOD_POST, this);
	}
	
	/**
	 * 取得最近日报填写信息
	 */
	public void getDailyInfo(){
		MyParameters params = new MyParameters();
		params.add("header_referer", AppConstants.sReq_IndexDailyInfo_Referer);
		request(GETDAILYINFO, AppConstants.sReq_IndexDailyInfo, params, HTTPMETHOD_GET, this);
	}
	
	/**
	 * 取得最新待办信息
	 */
	public void getWaitDealInfo(){
		MyParameters params = new MyParameters();
		params.add("header_referer", AppConstants.sReq_IndexWaitDealInfo_Referer);
		request(GETWAITDEALINFO, AppConstants.sReq_IndexWaitDealInfo, params, HTTPMETHOD_GET, this);
	}
	
	
	
	
	@Override
	public void onComplete(String response,int flag) {
		Log.d(TAG, "===onComplete:"+flag);
		Message msg = mHandler.obtainMessage();
		switch(flag){
		case GETUSERINFO:{
			msg.what = GETUSERINFO;
			Bundle data = new Bundle();
			JSONObject jo = null;
			try {
				JSONArray js = new JSONArray(response);
				if(null != js && js.length()>0){
					jo = js.getJSONObject(0);
				}
				if(null != jo){
					data.putString("fullname", jo.getString("F_USERNAME"));
					data.putString("userdep", jo.getString("F_DEPTNAME"));
					data.putString("userpost", jo.getString("F_DICNAME"));
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
					Editor e = sp.edit();
					e.putString("index_fullname", jo.getString("F_USERNAME"));
					e.putString("index_userdep", jo.getString("F_DEPTNAME"));
					e.putString("index_userpost", jo.getString("F_DICNAME"));
					e.commit();
					
				}
			} catch (JSONException e) {
				msg.what = SHOWMESSAGE;
				msg.getData().putString("message", "服务器返回数据格式错误，请重试！");
				e.printStackTrace();
			}
			msg.setData(data);
		}break;
		case GETONLINESUM:{
			msg.what = GETONLINESUM;
			Bundle data = new Bundle();
			data.putString("result", response);
			msg.setData(data);
		}break;
		case GETWAITDEALSUM:{
			msg.what = GETWAITDEALSUM;
			Bundle data = new Bundle();
			data.putString("result", response.split(",")[0]);
			msg.setData(data);
		}break;
		case GETDAILYINFO:{
			msg.what = GETDAILYINFO;
			Bundle data = new Bundle();
			Document doc = Jsoup.parse(response);
			Element e_listcontent_table = doc.select("table[id=caseTable]").first();
			if(null != e_listcontent_table){
				Elements e_listcontent = e_listcontent_table.select("tr");
				int datasum = 0;
				GregorianCalendar today = new GregorianCalendar();
				if(today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
					datasum = 7;
				}else{
					datasum = today.get(Calendar.DAY_OF_WEEK) - 1;
				}
				datasum = datasum +7;
				if(e_listcontent != null && e_listcontent.size()>1){
					int sum = 14;
					if(e_listcontent.size()<15){
						sum = e_listcontent.size()-1;
					}
					Map<String,String> dailyMap = new HashMap<String,String>();
					for(int i=1;i<sum;i++){
						Element div =  e_listcontent.get(i);
						Elements tds =  div.select("td");
						dailyMap.put(tds.get(1).text().trim(), tds.get(3).text().trim());
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					List<String> result = new ArrayList<String>();
					int i = 0;
					for(;i<datasum-7;i++){
						Date todayDate = today.getTime();
						String dateStr = sdf.format(todayDate)+" 00:00:00";
						if(dailyMap.containsKey(dateStr)){
							result.add(dailyMap.get(dateStr).replace("小时", ""));
						}else{
							result.add("0");
						}
						today.add(Calendar.DAY_OF_MONTH, -1);
					}
					// 跳过上周的周六日
					String result_temp = "8";
					int j=0;
					today.add(Calendar.DAY_OF_MONTH, -2);
					for(i=i+2;i<datasum;i++){
						Date todayDate = today.getTime();
						String dateStr = sdf.format(todayDate)+" 00:00:00";
						if(dailyMap.containsKey(dateStr) && dailyMap.get(dateStr).equals("8小时")){
							j++;
						}
						today.add(Calendar.DAY_OF_MONTH, -1);
					}
					if(j==5){
						result_temp = "8";
					}else if(j==0){
						result_temp = "0";
					}else{
						result_temp = "4";
					}
					result.add(result_temp);
					//list格式：[8,8,8,……,0]  从今天往前，8代表已填，0代表未填，可能还有其他时间数目，4代表4小时
					data.putSerializable("result", (Serializable) result);
					msg.setData(data);
				}
			}else{
				msg.what = SHOWMESSAGE;
				msg.getData().putString("message", "日志填写信息获取失败，请重试！");
			}
			
		}break;
		case GETWAITDEALINFO:{
			msg.what = GETWAITDEALINFO;
			Bundle data = new Bundle();
			Document doc = Jsoup.parse(response);
			Element e_listcontent_table = doc.select("table[id=caseTable]").first();
			if(null != e_listcontent_table){
				Elements e_listcontent = e_listcontent_table.select("tr");
				int datasum = 0;
				GregorianCalendar today = new GregorianCalendar();
				if(today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
					datasum = 7;
				}else{
					datasum = today.get(Calendar.DAY_OF_WEEK) - 1;
				}
				datasum = datasum +7;
				if(e_listcontent != null && e_listcontent.size()>1){
					List<Map<String,String>> result = new ArrayList<Map<String,String>>();
					Map<String,String> waitDealMap = new HashMap<String,String>();
					int sum = 11;
					if(e_listcontent.size() < 11){
						sum = e_listcontent.size();
					}
					for(int i=1;i<sum;i++){
						Element div =  e_listcontent.get(i);
						Elements tds =  div.select("td");
						if(i%2 == 1){
							waitDealMap = new HashMap<String,String>();
							waitDealMap.put("index", ""+((i/2)+1));
							waitDealMap.put("dealPerson", tds.get(1).text().trim());
							waitDealMap.put("dealTime", tds.get(2).text().trim());
							waitDealMap.put("dealStatus", tds.get(3).text().trim());
						}else{
							waitDealMap.put("dealName", tds.get(0).text().trim());
							result.add(waitDealMap);
						}
					}
					data.putSerializable("result", (Serializable) result);
					msg.setData(data);
				}
			}else{
				msg.what = SHOWMESSAGE;
				msg.getData().putString("message", "待办信息获取失败，请重试！");
			}
			
		}break;
		default:break;
		}
		msg.sendToTarget();
	}

	@Override
	public void onComplete4binary(ByteArrayOutputStream responseOS,int flag) {
		Message msg = mHandler.obtainMessage();
		switch(flag){
		case GETUSERHEADERINFO:{
			msg.what = GETUSERHEADERINFO;
			//Bundle data = new Bundle();
			//data.putByteArray("result", responseOS.toByteArray());
			//msg.setData(data);
			Utility.saveFile(AppConstants.sReq_IndexUserHeaderInfo+AppConstants.loginname+"_head_160.jpg", 
					responseOS.toByteArray());
		}break;
		default:break;
		}
		msg.sendToTarget();
	}

	@Override
	public void onError(MyHttpException e,int flag) {
		Log.d(TAG, "===error"+flag+" , status:"+e.getStatusCode());
		Message msg = mHandler.obtainMessage();
		switch(flag){
		case GETUSERHEADERINFO:{
			msg.what = GETUSERHEADERINFO_F;
			Bundle data = new Bundle();
			if(e.getStatusCode() != 200){
				
			}
			msg.setData(data);
		}break;
		default:break;
		}
		msg.sendToTarget();
	}
	
	

}
