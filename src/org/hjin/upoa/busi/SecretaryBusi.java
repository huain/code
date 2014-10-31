package org.hjin.upoa.busi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.Secretary;
import org.hjin.upoa.util.net.MyParameters;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 小秘书业务处理
 * @author Administrator
 *
 */
public class SecretaryBusi extends BaseBusi {
	
	/** flag标识：获取各种工单数目*/
	public static final int GET_TASK_COUNT = 0x0701;
	
	/** flag标识：获取工单待办信息*/
	public static final int GET_ARWAIT = 0x0702;

	public SecretaryBusi(Handler handler){
		this.mHandler = handler;
	}
	
	public void getTaskCount(){
		MyParameters params = new MyParameters();
		request(GET_TASK_COUNT, AppConstants.sReq_Secretary, params, HTTPMETHOD_POST, this);
	}
	
	/**
	 * 取得最新待办信息
	 */
	public void getArWait(){
		MyParameters params = new MyParameters();
		params.add("header_referer", AppConstants.sReq_IndexWaitDealInfo_Referer);
		request(GET_ARWAIT, AppConstants.sReq_IndexWaitDealInfo, params, HTTPMETHOD_GET, this);
	}

	@Override
	public void onComplete(String response, int flag) {
		Message msg = mHandler.obtainMessage();
		switch(flag){
		case GET_TASK_COUNT:{
//			response = "3,5,5,15,5,55,5,5,5,5,5,5,5,5,5,5,115,5,5,2225,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5";
			Log.d(TAG, "++++++++:"+response);
			Bundle data = new Bundle();
			msg.what = GET_TASK_COUNT;
			if(response != null && !"".equals(response)){
				String[] countData = response.split(",");
				if(countData != null && countData.length == 41){
					Secretary s = new Secretary();
					s.setArwait(Integer.parseInt(countData[0]));
					s.setCmdbwait(Integer.parseInt(countData[2]));
					s.setLeaveCount(Integer.parseInt(countData[3]));
					s.setTravelCount(Integer.parseInt(countData[4]));
					s.setFinanceCount(Integer.parseInt(countData[5]));
					s.setFinanceReadCount(Integer.parseInt(countData[6]));
					s.setYdCount(Integer.parseInt(countData[7]));
					s.setYd2Count(Integer.parseInt(countData[8]));
					s.setAssetCount(Integer.parseInt(countData[9]));
					s.setPurchaseCount(Integer.parseInt(countData[10]));
					s.setTravelxmCount(Integer.parseInt(countData[11]));
					s.setHr1Count(Integer.parseInt(countData[12]));
					s.setHr2Count(Integer.parseInt(countData[13]));
					s.setHr3Count(Integer.parseInt(countData[4]));
					s.setHr4Count(Integer.parseInt(countData[15]));
					s.setHr5Count(Integer.parseInt(countData[16]));
					s.setHr6Count(Integer.parseInt(countData[17]));
					s.setMpCount(Integer.parseInt(countData[18]));
					s.setMp2Count(Integer.parseInt(countData[19]));
					s.setIt1(Integer.parseInt(countData[20]));
					s.setIt2(Integer.parseInt(countData[21]));
					s.setIt3(Integer.parseInt(countData[22]));
					s.setIt4(Integer.parseInt(countData[23]));
					s.setIt5(Integer.parseInt(countData[24]));
					s.setIt6(Integer.parseInt(countData[25]));
					s.setIt7(Integer.parseInt(countData[26]));
					s.setIt8(Integer.parseInt(countData[27]));
					s.setIt9(Integer.parseInt(countData[28]));
					s.setIt10(Integer.parseInt(countData[29]));
					s.setIt11(Integer.parseInt(countData[30]));
					s.setIt12(Integer.parseInt(countData[31]));
					s.setIt13(Integer.parseInt(countData[32]));
					s.setIt14(Integer.parseInt(countData[33]));
					s.setIt15(Integer.parseInt(countData[34]));
					s.setIt16(Integer.parseInt(countData[35]));
					s.setIt17(Integer.parseInt(countData[36]));
					s.setIt18(Integer.parseInt(countData[37]));
					s.setIt19(Integer.parseInt(countData[38]));
					s.setJx(Integer.parseInt(countData[39]));
					msg.arg1 = 1;
					data.putSerializable("data", s);
					Log.d(TAG, "hahah");
				}else{
					msg.arg1 = 0;
					data.putCharSequence("data", "数据格式不正确！");
					Log.d(TAG, "数据格式不正确");
				}
			}else{
				msg.arg1 = 0;
				data.putCharSequence("data", "无数据！");
				Log.d(TAG, "无数据");
			}
			msg.setData(data);
			msg.sendToTarget();
		}break;
		case GET_ARWAIT:{
			msg.what = GET_ARWAIT;
			Bundle data = new Bundle();
			Document doc = Jsoup.parse(response);
			Element e_listcontent_table = doc.select("table[id=caseTable]").first();
			if (null != e_listcontent_table) {
				Elements e_listcontent = e_listcontent_table.select("tr");
				int datasum = 0;
				GregorianCalendar today = new GregorianCalendar();
				if (today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					datasum = 7;
				} else {
					datasum = today.get(Calendar.DAY_OF_WEEK) - 1;
				}
				datasum = datasum + 7;
				if (e_listcontent != null && e_listcontent.size() > 1) {
					List<Map<String, String>> result = new ArrayList<Map<String, String>>();
					Map<String, String> waitDealMap = new HashMap<String, String>();
					int sum = 11;
					if (e_listcontent.size() < 11) {
						sum = e_listcontent.size();
					}
					for (int i = 1; i < sum; i++) {
						Element div = e_listcontent.get(i);
						Elements tds = div.select("td");
						if (i % 2 == 1) {
							waitDealMap = new HashMap<String, String>();
							waitDealMap.put("index", "" + ((i / 2) + 1));
							waitDealMap.put("dealPerson", tds.get(1).text().trim());
							waitDealMap.put("dealTime", tds.get(2).text().trim());
							waitDealMap.put("dealStatus", tds.get(3).text().trim());
						} else {
							waitDealMap.put("dealName", tds.get(0).text().trim());
							result.add(waitDealMap);
						}
					}
					data.putSerializable("result", (Serializable) result);
					msg.setData(data);
				}
			}
		}break;
		default:break;
		}
	}
	
	

}
