package org.hjin.upoa.ui;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.NewsBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.ui.view.MyImageGetter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NewsInfoFragment extends BaseFragment{
	
	private String TAG = "NewsInfoFragment";
	
	private View mView;
	
	private NewsBusi mNewsBusi;
	
	private Handler mHandler;
	
	public static NewsInfoFragment newInstance(String id) {
		NewsInfoFragment newsInfo = new NewsInfoFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        newsInfo.setArguments(args);
        return newsInfo;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
		
		mHandler = new BaseHandler(activity.getApplicationContext()){
			@Override
			public void handleMessage(Message msg) {
				try {
					super.handleMessage(msg);
					switch(msg.what){
					// TODO list数据返回，界面处理
					case NewsBusi.GETNEWSINFO:{
						Bundle data = msg.getData();
						if(data.containsKey("newsContent")){
							String content = data.getString("newsContent");
							String title = data.getString("title");
							mActivity.setTitle(title);
							TextView tv = (TextView)mView.findViewById(R.id.newsContent);
							//Log.d(TAG, "===content:"+Html.fromHtml(content));
							//wv.loadData(content, "text/html", "UTF-8");
							//wv.loadDataWithBaseURL(null, content, "text/html","UTF-8", null);
							//tv.setText(Html.fromHtml(content));
							boolean isGetNetPic = false;
							boolean isnonepic = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("setting_item_nonepic", false);
							Log.d(TAG, "===："+isnonepic);
							ConnectivityManager conMan = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
							State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
							State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
							if(isnonepic && (mobile==State.CONNECTED||mobile==State.CONNECTING)){
								
							}else{
								isGetNetPic = true;
							}
//						if(wifi==State.CONNECTED||wifi==State.CONNECTING)
							Spanned spanned = Html.fromHtml(content, new MyImageGetter(getActivity(), tv,isGetNetPic), null);
							tv.setText(spanned);
						}
						
					}break;
					default:break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mView = inflater.inflate(R.layout.fragment_newsinfo, container,false);
		mNewsBusi = new NewsBusi(mHandler);
		mNewsBusi.getNewsInfo(getArguments().getString("id"));
		return mView;
	}
	
}
