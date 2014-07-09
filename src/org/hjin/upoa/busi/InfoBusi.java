package org.hjin.upoa.busi;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.Info;
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
 *  联系人业务
 * @author Administrator
 *
 */
public class InfoBusi extends BaseBusi {
	
	private final String TAG = "InfoBusi";
	
	public static final int GETINFOLIST = 1;
	
	public InfoBusi(Handler handler){
		super(handler);
	}
	
	/**
	 * 获取联系人列表，最多二十条
	 * @param co 搜索条件
	 */
	public void getInfoList(String co){
		MyParameters params = new MyParameters();
		params.add("co", co);
		params.add("header_referer", AppConstants.sReq_InfoList_Referer);
		request(GETINFOLIST, AppConstants.sReq_InfoList, params, HTTPMETHOD_GET, this);
	};
	
	@Override
	public void onComplete(String response,int flag) {
		Message msg = mHandler.obtainMessage();
		switch(flag){
		case GETINFOLIST:{
//			Log.v(TAG, "===GETNEWSLIST_response:"+response);
			msg.what = GETINFOLIST;
			Bundle data = new Bundle();
			Document doc = Jsoup.parse(response);
			Element e_sum = doc.select("i[class=ml20]").first();
			if(null != e_sum){
				String sum = e_sum.text();
				sum = sum.replace("共", "");
				sum = sum.replace("条数据", "");
				data.putString("listSum", sum);
			}
			Elements e_listcontent = doc.select("div[class=tongxunlu]");
			if(e_listcontent != null && e_listcontent.size()>0){
				Log.d(TAG, "==="+e_listcontent.size());
				List<Info> infoList = new ArrayList<Info>();
				Info info = null;
				for(int i=0;i<e_listcontent.size();i++){
					Element div =  e_listcontent.get(i);
					Elements lis =  div.select("li");
					if(null != lis && lis.size() == 3){
						info = new Info();
						Element e1 = lis.get(0).select("em").first();
						if(null != e1){
							String post = e1.text();
							post = post.substring(1, post.length()-1);
							info.setPost(post);
							e1.remove();
						}
						info.setUsername(lis.get(0).text());
						info.setDep(lis.get(1).text().substring(3));
						info.setTel(lis.get(2).text().substring(3));
						Element e2 = div.select("dd").first();
						if(null != e2){
							info.setEmail(e2.text());
						}
						infoList.add(info);
					}
					
				}
				Log.v(TAG, "===smallNoteListList.size:"+infoList.size());
				data.putSerializable("listContent", (Serializable) infoList);
				msg.setData(data);
			}
			Log.d(TAG, "==="+e_listcontent.size());
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
