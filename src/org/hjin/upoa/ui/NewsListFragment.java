package org.hjin.upoa.ui;


import java.util.ArrayList;
import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.BaseBusi;
import org.hjin.upoa.busi.NewsBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.NewsInfo;
import org.hjin.upoa.ui.view.NewsListAdapter;
import org.hjin.upoa.ui.view.PullToRefreshView;
import org.hjin.upoa.ui.view.PullToRefreshView.OnFooterRefreshListener;
import org.hjin.upoa.ui.view.PullToRefreshView.OnHeaderRefreshListener;
import org.hjin.upoa.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 新闻中心：新闻列表
 * @author Administrator
 *
 */
public class NewsListFragment extends BaseFragment implements OnHeaderRefreshListener,OnFooterRefreshListener{
	
	private final String TAG = "NewsListFragment";
	
	private PullToRefreshView mPullToRefreshView;
	
	private ListView mNewsList;
	
	private NewsListAdapter mAdapter;
	
	private NewsBusi mNewsBusi;
	
	private View mV;
	
	private String mTitle;
	
	private String mType;
	
	private Handler mHandler;
	

	public NewsListFragment(String title,String type){
		this.mTitle = title;
		this.mType = type;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		
		mHandler = new BaseHandler(activity.getApplicationContext()){
			@Override
			public void handleMessage(Message msg) {
				try {
					super.handleMessage(msg);
					switch(msg.what){
					// TODO list数据返回，界面处理
					case NewsBusi.GETNEWSLIST:{
						Bundle data = msg.getData();
						if(data.containsKey("listContent")){
							List<NewsInfo> list = (List<NewsInfo>)data.getSerializable("listContent");
							if(AppConstants.sNews_Page_Current == 1){
								mAdapter.setData(list);
							}else{
								mAdapter.append(list);
							}
							
							mPullToRefreshView.onHeaderRefreshComplete();
							mPullToRefreshView.onFooterRefreshComplete();
						}
						if(mV.findViewById(R.id.newslist_load).VISIBLE == View.VISIBLE){
							mV.findViewById(R.id.newslist_load).setVisibility(View.GONE);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mV = inflater.inflate(R.layout.fragment_newslist, container, false);
		loadingAnimatorActive(mV.findViewById(R.id.newslist_load));
		return mV;
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		AppConstants.sNews_Page_Current = 0;
		AppConstants.sNews_Page_Sum=0;
		AppConstants.sNews_Sum=0;
		mPullToRefreshView = (PullToRefreshView)(mActivity.findViewById(R.id.newslist_pull_refresh_view));
		mAdapter = new NewsListAdapter(getActivity().getApplicationContext(),new ArrayList<NewsInfo>());
		mNewsList = (ListView)mActivity.findViewById(R.id.newsList);
		mNewsList.setAdapter(mAdapter);
		mNewsBusi = new NewsBusi(mHandler);
		mNewsBusi.getNewsList(0,0,mType);
		
		mNewsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				String newsid = v.getTag(NewsListAdapter.NEWSID)+"";
				showNews(newsid);
			}
		});
//		ls.setAdapter(mAdapter);
//		
//		if(cursor.getCount()<1){
//			initDataThread.start();
//		}
		
//		setListAdapter(new DataAdapter(this));
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
	}
	
	private void showNews(String id){
//		if(!Utility.isBlank(id)){
//			NewsInfoFragment newsFragment = NewsInfoFragment.newInstance(id);
//	        // Execute a transaction, replacing any existing fragment
//	        // with this one inside the frame.
//			
//	        FragmentTransaction ft = getFragmentManager().beginTransaction();
//	        ft.replace(R.id.content_frame, newsFragment);
//	        ft.addToBackStack("newinfo");
//	        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//	        ft.commit();
//		}
		
		Intent intent = new Intent();
		intent.setClass(getActivity(), NewsInfoActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}
	



	

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if(null != mNewsBusi){
			if(AppConstants.sNews_Page_Current >= AppConstants.sNews_Page_Sum){
				// TODO 当前页是最后一页
				Log.v(TAG, "===当前页是最后一页");
				mPullToRefreshView.onFooterRefreshComplete();
				Message msg = mHandler.obtainMessage();
				msg.getData().putString("message", "当前是最后一页，没有更多"+mTitle+"!");
				msg.sendToTarget();
			}else{
				mNewsBusi.getNewsList(AppConstants.sNews_Page_Current + 1, AppConstants.sNews_Page_Sum,mType);
			}
		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if(null != mNewsBusi){
			AppConstants.sNews_Page_Current = 0;
			mNewsBusi.getNewsList(0, 0,mType);
		}
	}

	@Override
	public void onHeaderRefreshSetText(PullToRefreshView view) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

}
