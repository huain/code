package org.hjin.upoa.busi;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.SmallNote;
import org.hjin.upoa.util.Utility;
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
 * 小字报业务处理
 * @author Administrator
 *
 */
public class SmallNoteBusi extends BaseBusi {
	
	private final String TAG = "SmallNoteBusi";
	
	public static final int GETSMALLNOTELIST = 1;
	
	public static final int GETSMALLNOTEINFO = 2;
	
	public static final int SAVESMALLNOTE = 3;
	
	public SmallNoteBusi(Handler handler){
		super(handler);
	}
	
	/**
	 * 获取新闻列表，第二页，第三页……
	 * @param currentpage 当前获取的页数
	 * @param totalpages 总页数
	 */
	public void getSmallNoteList(int currentpage,int totalpages){
		MyParameters params = new MyParameters();
		if(currentpage > 1 && totalpages >0 && currentpage <= totalpages){
			params.add("var_currentpage", ""+currentpage);
			params.add("var_pagesize", "20");
			params.add("var_totalpages", ""+totalpages);
			params.add("var_selectvalues", "");
			params.add("var_istranfer", "1");
			params.add("header_referer", AppConstants.sReq_SmallNoteList_Referer);
			request(GETSMALLNOTELIST, AppConstants.sReq_SmallNoteList, params, HTTPMETHOD_POST, this);
		}else{
			params.add("header_referer", AppConstants.sReq_SmallNoteList_Referer);
			request(GETSMALLNOTELIST, AppConstants.sReq_SmallNoteList, params, HTTPMETHOD_GET, this);
		}
		
	};
	
	public void getSmallNoteInfo(String id){
		MyParameters params = new MyParameters();
		params.add("pid", id);
		params.add("header_referer", AppConstants.sReq_SmallNoteInfo_Referer);
		request(GETSMALLNOTEINFO, AppConstants.sReq_SmallNoteInfo, params, HTTPMETHOD_GET, this);
		
	};
	
	public void saveSmallNote(String title,String content){
		MyParameters params = new MyParameters();
		params.add("talk.isview", "1");
		params.add("talk.title", title);
		params.add("talk.contents", content);
		params.add("header_referer", AppConstants.sReq_SaveSmallNote_Referer);
		request(SAVESMALLNOTE, AppConstants.sReq_SaveSmallNote, params, HTTPMETHOD_POST, this);
	}
	

	@Override
	public void onComplete(String response,int flag) {
		Message msg = mHandler.obtainMessage();
		switch(flag){
		case GETSMALLNOTELIST:{
//			Log.v(TAG, "===GETNEWSLIST_response:"+response);
			msg.what = GETSMALLNOTELIST;
			Bundle data = new Bundle();
			Document doc = Jsoup.parse(response);
			Element e_sum = doc.select("i[class=ml20]").first();
			if(null != e_sum){
				String sum = e_sum.text();
				sum = sum.replace("共", "");
				sum = sum.replace("条数据", "");
				AppConstants.sSmallNote_Sum = Integer.parseInt(sum);
				AppConstants.sSmallNote_Page_Sum = AppConstants.sSmallNote_Sum /20;
				if(AppConstants.sSmallNote_Sum %20 != 0){
					AppConstants.sSmallNote_Page_Sum = AppConstants.sSmallNote_Page_Sum +1;
				}
				data.putString("listSum", sum);
			}
			
			Element e_listcontent_table = doc.select("table[id=caseTable]").first();
			if(null != e_listcontent_table){
				Elements e_listcontent = e_listcontent_table.select("tr");
				if(e_listcontent != null && e_listcontent.size()>1){
					AppConstants.sSmallNote_Page_Current = AppConstants.sSmallNote_Page_Current +1;
					List<SmallNote> smallNoteList = new ArrayList<SmallNote>();
					SmallNote sn = null;
					for(int i=1;i<e_listcontent.size();i++){
						Element tr =  e_listcontent.get(i);
						Elements tds =  tr.select("td");
						if(null != tds && tds.size() == 6){
							sn = new SmallNote();
							sn.setIndexid(tds.get(0).text());
							sn.setTitle(tds.get(1).text());
							//sn.setContent(tds.get(1).select("a"));
							sn.setUsername(tds.get(2).text());
							sn.setUserdep(tds.get(3).text());
							sn.setTime(tds.get(4).text());
							Elements idElement = tds.get(0).select("a");
							if(null != idElement && idElement.size() == 1){
								String onclick = idElement.get(0).attr("onclick");
								sn.setId(onclick.substring(14, onclick.length()-2));
							}
							Elements contentElement = tds.get(1).select("a");
							if(null != contentElement && contentElement.size() == 1){
								sn.setContent(contentElement.get(0).attr("title"));
							}
							Elements useridElement = tds.get(5).select("a");
							if(null != useridElement && useridElement.size() >= 1){
								String onclick = useridElement.get(0).attr("onclick");
								sn.setUserid(onclick.substring(onclick.indexOf("','")+3, onclick.length()-3));
							}
							Log.d(TAG, "===smallNote:[userid="+sn.getUserid()+"||id="+sn.getId()+"||" +"]");
							smallNoteList.add(sn);
						}
						
						
					}
					Log.v(TAG, "===smallNoteListList.size:"+smallNoteList.size());
					data.putSerializable("listContent", (Serializable) smallNoteList);
					msg.setData(data);
				}
			}else{
				setNoDataMsg(msg);
			}
			
		}break;
		case GETSMALLNOTEINFO:{
//			Log.v(TAG, "===GETNEWSLIST_response:"+response);
			msg.what = GETSMALLNOTEINFO;
			Bundle data = new Bundle();
			Document doc = Jsoup.parse(response);
			//Element content = doc.select("div").first();
			Element title1 = doc.select("h2").first();
			Element title2 = doc.select("H3").first();
			Element title3 = doc.select("H4").first();
			Element content = doc.select("DD").first();
//			Elements contents = content.select("span");
			Element read = doc.select("div[class=read]").first();
			StringBuffer sb = new StringBuffer("<div><p>");
			if(null != title1){
				sb.append(title1.html());
			}
			if(null != title2){
				sb.append(title2.html());
			}
			if(null != title3){
				sb.append(title3.html());
			}
			sb.append("</p><p>");
			if(null != content ){
				sb.append(content.html());
			}
			sb.append("</p><p>");
			if(null != read){
				sb.append(read.html());
			}
			sb.append("</p></div>");
			String newsContent = sb.toString();
			data.putString("newsContent", newsContent);
			msg.setData(data);
		}break;
		case SAVESMALLNOTE:{
			msg.what = SAVESMALLNOTE;
			if(!Utility.isBlank(response) && "1".equals(response)){
				Log.d(TAG, "===:小字报保存成功");
				msg.arg1 = 1;//发送成功
			}else{
				msg.arg1 = 1;//发送失败
			}
		}break;
		default:break;
		}
		msg.sendToTarget();
		//mHandler.sendMessage(msg);
	}

	@Override
	public void onComplete4binary(ByteArrayOutputStream responseOS,int flag) {
		// TODO Auto-generated method stub

	}

}
