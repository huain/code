package org.hjin.upoa.busi;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.Daily;
import org.hjin.upoa.util.Utility;
import org.hjin.upoa.util.net.MyParameters;
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
 *  日报业务
 * @author Administrator
 *
 */
public class DailyBusi extends BaseBusi {
	
	private final String TAG = "DailyBusi";
	
	/** flag标识：取得历史日报列表*/
	public static final int GETDAILYLIST = 0x0301;
	/** flag标识：取得项目列表*/
	public static final int GETPROLIST = 0x0302;
	/** flag标识：检查项目权限*/
	public static final int CHECKPRONO = 0x0303;
	/** flag标识：检查日报时间*/
	public static final int CHECKHOUR = 0x0304;
	/** flag标识：提交日报填写信息*/
	public static final int POSTDAILY = 0x0305;
	/** flag标识：检查日报填写日期*/
	public static final int CHECKCREATEDATE = 0x0306;
	
	private Daily mDaily;
	
	public DailyBusi(Handler handler){
		super(handler);
	}
	
	/**
	 * 获取日报详情列表，最多二十条
	 * @param co 搜索条件
	 */
	public void getDailyList(int currentpage,int totalpages){
		MyParameters params = new MyParameters();
		if(currentpage > 1 && totalpages >0 && currentpage <= totalpages){
			params.add("var_currentpage", ""+currentpage);
			params.add("var_pagesize", "20");
			params.add("var_totalpages", ""+totalpages);
			params.add("var_selectvalues", "");
			params.add("var_istranfer", "1");
			params.add("var_sorttype", "0");
			request(GETDAILYLIST, AppConstants.sReq_DailyList, params, HTTPMETHOD_POST, this);
		}else{
			params.add("header_referer", AppConstants.sReq_DailyList_Referer);
			request(GETDAILYLIST, AppConstants.sReq_DailyList, params, HTTPMETHOD_GET, this);
		}
	};
	
	/**
	 * 获取项目列表，最多二十条
	 * @param co 搜索条件
	 */
	public void getProList(){
		MyParameters params = new MyParameters();
		params.add("header_referer", AppConstants.sReq_DailyNewProList_Referer);
		request(GETPROLIST, AppConstants.sReq_DailyNewProList, params, HTTPMETHOD_GET, this);
	};
	
	/**
	 * daily数据验证第一步，不包括主题和描述
	 * @param daily 
	 * @return 成功，返回true，失败，返回false
	 */
	public boolean validate1(Daily daily){
		boolean result = true;
		Message msg = mHandler.obtainMessage();
		msg.what = SHOWMESSAGE;
		
		String begintime = daily.getBegintime();
		String endtime = daily.getEndtime();
		
		try {
			if(Utility.isBlank(daily.getCode()) && !"日常任务".equals(daily.getType()) && !"售前任务".equals(daily.getType())){
				result = false;
				msg.getData().putString("message", "请选择项目信息！");
			}else if(Utility.isBlank(begintime)){
				result = false;
				msg.getData().putString("message", "请选择开始时间！");
			}else if(Utility.isBlank(endtime)){
				result = false;
				msg.getData().putString("message", "请选择结束时间！");
			}else if(!begintime.substring(0, 10).equals(endtime.substring(0, 10))){
				Log.d(TAG, "===begindate:"+begintime.substring(0, 10));
				result = false;
				msg.getData().putString("message", "开始时间和结束时间必须为同一天！");
			}else if(begintime.compareTo(endtime)>0 || begintime.compareTo(endtime)==0){
				result = false;
				msg.getData().putString("message", "结束时间必须晚于开始时间！");
			}else if(Utility.isBlank(daily.getType())){
				result = false;
				msg.getData().putString("message", "请选择任务类型！");
			}else if(Utility.isBlank(daily.getPosition())){
				result = false;
				msg.getData().putString("message", "请选择工作地点！");
			}else{
				if(null != daily){
					daily.setBegintime(begintime+":00");
					daily.setEndtime(endtime+":00");
					Calendar c = Calendar.getInstance();
					c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(begintime+":00"));
					long begin = c.getTimeInMillis();
					c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endtime+":00"));
					long end = c.getTimeInMillis();
					Log.d(TAG, "===long:"+(end - begin));
					if((end - begin)/(60*60*1000)>=8){
						daily.setTime("8");
					}else{
						BigDecimal  bd = new BigDecimal(end - begin);
						bd.setScale(2, BigDecimal.ROUND_DOWN);
						Log.d(TAG, "===bd:"+bd.doubleValue());
						daily.setTime(""+new BigDecimal(bd.doubleValue()/(60*60*1000)).setScale(2,BigDecimal.ROUND_DOWN));
					}
				}
			}
			
		} catch (ParseException e) {
			result = false;
			msg.getData().putString("message", "时间格式错误！");
			e.printStackTrace();
		}
		if(!result){
			msg.sendToTarget();
		}
		return result;
	}
	
	/**
	 * 日志提交验证，第二步验证，验证主题和描述信息
	 * @param daily
	 * @return
	 */
	public boolean validate2(Daily daily){
		boolean result = true;
		Message msg = mHandler.obtainMessage();
		msg.what = SHOWMESSAGE;
		if(Utility.isBlank(daily.getSubject())){
			result = false;
			msg.getData().putString("message", "请填写日志主题！");
		}else if(Utility.isBlank(daily.getDesc())){
			result = false;
			msg.getData().putString("message", "请填写日志描述！");
		}else if(daily.getDesc().length()>1200){
			result = false;
			msg.getData().putString("message", "日志描述字数超过限制！");
		}
		if(!result){
			msg.sendToTarget();
		}
		return result;
	}
	
	/**
	 * 检查日志填写日期
	 * @param daily
	 */
	public void checkCreateDate(Daily daily){
		mDaily = daily;
		MyParameters params = new MyParameters();
		params.add("dateStr", daily.getBegintime());
		params.add("header_referer", AppConstants.sReq_DailyNewCommon_Referer);
		request(CHECKCREATEDATE, AppConstants.sReq_DailyNew_CheckCreateDate, params, HTTPMETHOD_POST, this);
	}
	
	/**
	 * 检查项目编号是否合法
	 * @param daily
	 */
	public void checkProjNo(Daily daily){
		if(null == daily.getCode() || "".equals(daily.getCode())){
			Log.d(TAG, "==="+daily.getType());
			if(daily.getType().equals("日常任务") || daily.getType().equals("售前任务")){
				this.onComplete("true", CHECKPRONO);
				return;
			}else{
				this.onComplete("false", CHECKPRONO);
			}
		}else{
			MyParameters params = new MyParameters();
			params.add("wd.projectNo", daily.getCode());
			params.add("header_referer", AppConstants.sReq_DailyNewCommon_Referer);
			request(CHECKPRONO, AppConstants.sReq_DailyNew_CheckProjectCode, params, HTTPMETHOD_POST, this);
		}
	}
	
	/**
	 * 检查日志的时间是否符合要求
	 * @param daily
	 */
	public void checkHour(Daily daily){
		if(null == daily.getTime() || "".equals(daily.getTime()) ||"0.00".equals(daily.getTime())){
			this.onComplete("false0", CHECKHOUR);
		}else{
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(daily.getBegintime().startsWith(sdf.format(date))){
				MyParameters params = new MyParameters();
				params.add("manHour_id", daily.getTime());
				params.add("startTime_id", daily.getBegintime());
				params.add("wd.pid", "");
				params.add("header_referer", AppConstants.sReq_DailyNewCommon_Referer);
				request(CHECKHOUR, AppConstants.sReq_DailyNew_CheckHour, params, HTTPMETHOD_POST, this);
			}else{
				MyParameters params = new MyParameters();
				params.add("manHour_id", daily.getTime());
				params.add("startTime_id", daily.getBegintime());
				params.add("header_referer", AppConstants.sReq_DailyNewCommon_Referer);
				request(CHECKHOUR, AppConstants.sReq_DailyNew_CheckSupplementHour, params, HTTPMETHOD_POST, this);
			}
		}
	}
	/**
	 * 提交日志
	 * @param daily
	 */
	public void postDaily(Daily daily){
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(daily.getBegintime().startsWith(sdf.format(date))){
			MyParameters params = new MyParameters();
			params.add("wd.pid", "");
			params.add("wd.projectNo", daily.getCode());
			params.add("wd.projectName", daily.getName());
			params.add("startTime_id", daily.getBegintime().substring(11, 16));
			params.add("endTime_id", daily.getEndtime().substring(11, 16));
			params.add("manHour_id", daily.getTime());
			params.add("wd.baseSchema", getTyepCode(daily.getType()));
			params.add("wd.workPlace", daily.getPosition());
			params.add("wd.workTitle", daily.getSubject());
			params.add("wd.taskDescribe", daily.getDesc());
			params.add("header_referer", AppConstants.sReq_DailyNewCommon_Referer);
			request(POSTDAILY, AppConstants.sReq_DailyNew_Post, params, HTTPMETHOD_POST, this);
		}else{
			MyParameters params = new MyParameters();
			params.add("wd.pid", "");
			params.add("wd.projectNo", daily.getCode());
			params.add("wd.projectName", daily.getName());
			params.add("startTime_id", daily.getBegintime());
			params.add("endTime_id", daily.getEndtime());
			params.add("manHour_id", daily.getTime());
			params.add("wd.baseSchema", getTyepCode(daily.getType()));
			params.add("wd.workPlace", daily.getPosition());
			params.add("wd.workTitle", daily.getSubject());
			params.add("wd.taskDescribe", daily.getDesc());
			params.add("header_referer", AppConstants.sReq_DailyNewCommon_Referer);
			request(POSTDAILY, AppConstants.sReq_DailyNew_PostSupplement, params, HTTPMETHOD_POST, this);
		}
	}
	
	private String getTyepCode(String type){
		if("日常任务".equals(type)){
			return "WF:UL_DA_CTK";
		}else if("售前任务".equals(type)){
			return "WF:UL_DA_PAT";
		}else if("实施任务".equals(type)){
			return "WF:UL_DA_IFT";
		}else if("开发任务".equals(type)){
			return "WF:UL_DA_DLPS";
		}else if("测试任务".equals(type)){
			return "WF:UL_DA_TEPS";
		}else{
			return "";
		}
	}
	
	@Override
	public void onComplete(String response,int flag) {
		Message msg = mHandler.obtainMessage();
		switch(flag){
		case GETDAILYLIST:{
			msg.what = GETDAILYLIST;
			Bundle data = new Bundle();
			Document doc = Jsoup.parse(response);
			if(AppConstants.sDaily_Sum == 0){
				Element e_sum = doc.select("i[class=ml20]").first();
				if(null != e_sum){
					String sum = e_sum.text();
					sum = sum.replace("共", "");
					sum = sum.replace("条数据", "");
					Log.d(TAG, "===sDaily_Sum:"+sum);
					AppConstants.sDaily_Sum = Integer.parseInt(sum);
					AppConstants.sDaily_Page_Sum = AppConstants.sDaily_Sum /20;
					if(AppConstants.sDaily_Sum %20 != 0){
						AppConstants.sDaily_Page_Sum = AppConstants.sDaily_Page_Sum +1;
					}
					data.putString("listSum", sum);
				}
			}
			Element e_listcontent_table = doc.select("table[id=caseTable]").first();
			if(null != e_listcontent_table){
				Elements e_listcontent = e_listcontent_table.select("tr");
				if(e_listcontent != null && e_listcontent.size()>1){
					AppConstants.sDaily_Page_Current = AppConstants.sDaily_Page_Current +1;
					Log.d(TAG, "==="+e_listcontent.size());
					List<Daily> dailyList = new ArrayList<Daily>();
					Daily daily = null;
					for(int i=1;i<e_listcontent.size();i++){
						Element div =  e_listcontent.get(i);
						Elements tds =  div.select("td");
						if(i%2==1 && null != tds && tds.size() == 6){
							daily = new Daily();
							daily.setCode(tds.get(0).text());
							daily.setName(tds.get(1).text());
							daily.setSubject(tds.get(2).text());
							daily.setBegintime(tds.get(3).text());
							daily.setEndtime(tds.get(4).text());
							daily.setTime(tds.get(5).text());
							String id = tds.get(5).select("a").attr("onclick");
							id = id.substring(11,id.length()-3);
							daily.setId(id);
						}else if(i%2==0 && null != tds && tds.size() == 2){
							daily.setDesc(tds.get(1).text());
							dailyList.add(daily);
						}
					}
					Log.v(TAG, "===smallNoteListList.size:"+dailyList.size());
					data.putSerializable("listContent", (Serializable) dailyList);
					msg.setData(data);
				}
			}else{
				setNoDataMsg(msg);
			}
		}break;
		case GETPROLIST:{
			msg.what = GETPROLIST;
			Bundle data = new Bundle();
			Document doc = Jsoup.parse(response);
			Element e_sum = doc.select("i[class=ml20]").first();
			if(null != e_sum){
				String sum = e_sum.text();
				sum = sum.replace("共", "");
				sum = sum.replace("条数据", "");
				data.putString("listSum", sum);
			}
			Element e_listcontent_table = doc.select("table[id=caseTable]").first();
			if(null != e_listcontent_table){
				Elements e_listcontent = e_listcontent_table.select("tr");
				if(e_listcontent != null && e_listcontent.size()>2){
					Log.d(TAG, "==="+e_listcontent.size());
					List<Map<String,String>> proList = new ArrayList<Map<String,String>>();
					Map<String,String> pro = null;
					for(int i=2;i<e_listcontent.size();i++){
						Element div =  e_listcontent.get(i);
						Elements tds =  div.select("td");
						if(null != tds && tds.size() == 3){
							pro = new HashMap<String,String>();
							pro.put("code", tds.get(1).text());
							pro.put("name", tds.get(2).text());
							proList.add(pro);
						}
					}
					Log.v(TAG, "===smallNoteListList.size:"+proList.size());
					data.putSerializable("listContent", (Serializable) proList);
					msg.setData(data);
				}
			}else{
				setNoDataMsg(msg);
			}
			
		}break;
		case CHECKPRONO:{
			Log.d(TAG, "===CHECKPRONO:"+response);
			if("true".equals(response)){
				this.checkHour(mDaily);
			}else{
				try {
					JSONObject jo = new JSONObject(response);
					String name = jo.getString("projectName");
					if(null ==name){
						msg.what = SHOWMESSAGE;
						msg.getData().putString("message", "该项目编号不存在，或无权限建立此项目日志，请核实!");
					}else{
						this.checkHour(mDaily);
					}
				} catch (JSONException e) {
					msg.what = SHOWMESSAGE;
					msg.getData().putString("message", "该项目编号不存在，或无权限建立此项目日志，请核实!");
					//e.printStackTrace();
				}
			}
		}break;
		case CHECKHOUR:{
			Log.d(TAG, "===CHECKHOUR:"+response);
			if("false0".equals(response)){
				msg.what = SHOWMESSAGE;
				msg.getData().putString("message", "历时小时数目必须大于0!");
			}else if("false".equals(response)){
				msg.what = SHOWMESSAGE;
				msg.getData().putString("message", "今日日志总历时不能大于8，请重新输入!");
			}else{
				this.postDaily(mDaily);
			}
		}break;
		case POSTDAILY:{
			msg.what = POSTDAILY;
			msg.getData().putString("message", "日志保存成功!");
		}
		case CHECKCREATEDATE:{
			if("true".equals(response)){
				this.checkProjNo(mDaily);
			}else if("false".equals(response)){
				msg.what = SHOWMESSAGE;
				msg.getData().putString("message", "所建日期为节假日,不允许建立日志!");
			}else if("false1".equals(response)){
				msg.what = SHOWMESSAGE;
				msg.getData().putString("message", "所建日期超过补建时限,请通过跨周补建日报补建!");
			}else{
				
			}
		}break;
		default:break;
		}
		
		msg.sendToTarget();
	}

}
