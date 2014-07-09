package org.hjin.upoa.busi;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.NewsInfo;
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
 * 新闻中心业务处理
 * @author Administrator
 *
 */
public class NewsBusi extends BaseBusi {
	
	private final String TAG = "NewsBusi";
	
	public static final int GETNEWSLIST = 1;
	
	public static final int GETNEWSINFO = 2;
	
	public NewsBusi(Handler handler){
		super(handler);
	}
	
	/**
	 * 获取新闻列表，第二页，第三页……
	 * @param currentpage 当前获取的页数
	 * @param totalpages 总页数
	 */
	public void getNewsList(int currentpage,int totalpages,String type){
		MyParameters params = new MyParameters();
		if(currentpage > 1 && totalpages >0 && currentpage <= totalpages){
			params.add("var_currentpage", ""+currentpage);
			params.add("var_pagesize", "20");
			params.add("var_totalpages", ""+totalpages);
			params.add("var_selectvalues", "");
			params.add("var_istranfer", "1");
			params.add("var_sorttype", "1");
			//params.add("var_sortfield", "3");
			params.add("newstype", type);
			params.add("header_referer", AppConstants.sReq_NewsList_Referer);
			request(GETNEWSLIST, AppConstants.sReq_NewsList, params, HTTPMETHOD_POST, this);
		}else{
			params.add("newstype", type);
			params.add("var_sorttype", "1");
			//params.add("var_sortfield", "3");
			params.add("header_referer", AppConstants.sReq_NewsList_Referer);
			request(GETNEWSLIST, AppConstants.sReq_NewsList, params, HTTPMETHOD_GET, this);
		}
		
	};
	
	public void getNewsInfo(String id){
		MyParameters params = new MyParameters();
		params.add("newsId", id);
		params.add("header_referer", AppConstants.sReq_NewsInfo_Referer);
		request(GETNEWSINFO, AppConstants.sReq_NewsInfo, params, HTTPMETHOD_GET, this);
		
	};
	

	@Override
	public void onComplete(String response,int flag) {
		Message msg = mHandler.obtainMessage();
		switch(flag){
		case GETNEWSLIST:{
//			Log.v(TAG, "===GETNEWSLIST_response:"+response);
			msg.what = GETNEWSLIST;
			Bundle data = new Bundle();
			Document doc = Jsoup.parse(response);
			Log.d(TAG, "---:"+AppConstants.sNews_Sum);
			if(AppConstants.sNews_Sum == 0){
				Element e_sum = doc.select("i[class=ml20]").first();
				String sum = e_sum.text();
				sum = sum.replace("共", "");
				sum = sum.replace("条数据", "");
				AppConstants.sNews_Sum = Integer.parseInt(sum);
				AppConstants.sNews_Page_Sum = AppConstants.sNews_Sum /20;
				if(AppConstants.sNews_Sum%20 != 0){
					AppConstants.sNews_Page_Sum = AppConstants.sNews_Page_Sum +1;
				}
				data.putString("listSum", sum);
				Log.d(TAG, "===sum:"+sum);
			}
			Elements e_listcontent = doc.select("a[onclick^=openNews(]");
			if(e_listcontent != null && e_listcontent.size()>0){
				AppConstants.sNews_Page_Current = AppConstants.sNews_Page_Current +1;
				List<NewsInfo> newsList = new ArrayList<NewsInfo>();
				NewsInfo ni = new NewsInfo();
				for(int i=0;i<e_listcontent.size();i++){
					Element content =  e_listcontent.get(i);
					if(i%2 == 0){
						ni = new NewsInfo();
						String id = content.attr("onclick");
						String title = content.text();
						if(!Utility.isBlank(id)){
							id = id.replace("openNews('", "");
							id = id.replace("');", "");
							ni.setmId(id);
						}
						ni.setmTitle(title);
					}else{
						ni.setmTime(content.text());
						newsList.add(ni);
					}
				}
				data.putSerializable("listContent", (Serializable) newsList);
				msg.setData(data);
			}
		}break;
		case GETNEWSINFO:{
//			Log.v(TAG, "===GETNEWSLIST_response:"+response);
			msg.what = GETNEWSINFO;
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
			data.putString("title", title1.text());
			msg.setData(data);
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
