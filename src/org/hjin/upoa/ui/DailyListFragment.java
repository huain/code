package org.hjin.upoa.ui;


import java.util.ArrayList;
import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.BaseBusi;
import org.hjin.upoa.busi.DailyBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.Daily;
import org.hjin.upoa.ui.view.DailyListAdapter;
import org.hjin.upoa.ui.view.PullToRefreshView;
import org.hjin.upoa.ui.view.PullToRefreshView.OnFooterRefreshListener;
import org.hjin.upoa.ui.view.PullToRefreshView.OnHeaderRefreshListener;
import org.hjin.upoa.util.Utility;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 历史日报列表
 * @author Administrator
 *
 */
public class DailyListFragment extends BaseFragment implements OnHeaderRefreshListener,OnFooterRefreshListener{
	
	private final String TAG = "DailyListFragment";
	
	private PullToRefreshView mPullToRefreshView;
	
	private ListView mDailyList;
	
	private DailyListAdapter mAdapter;
	
	private DailyBusi mDailyBusi;
	
	private View mV;
	
	private Handler mHandler;
	

	
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
						case DailyBusi.GETDAILYLIST:{
							Bundle data = msg.getData();
							if(data.containsKey("listContent")){
								List<Daily> list = (List<Daily>)data.getSerializable("listContent");
								if(AppConstants.sDaily_Page_Current == 1){
									mAdapter.setData(list);
								}else{
									mAdapter.append(list);
								}
								mPullToRefreshView.onHeaderRefreshComplete();
								mPullToRefreshView.onFooterRefreshComplete();
							}
							if(mV.findViewById(R.id.dailylist_load).VISIBLE == View.VISIBLE){
								mV.findViewById(R.id.dailylist_load).setVisibility(View.GONE);
							}
							
						}break;
						default:break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}};
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mV = inflater.inflate(R.layout.fragment_dailylist, container, false);
		loadingAnimatorActive(mV.findViewById(R.id.dailylist_load));
		return mV;
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		AppConstants.sDaily_Page_Current = 0;
		AppConstants.sDaily_Page_Sum=0;
		AppConstants.sDaily_Sum=0;
		
		mPullToRefreshView = (PullToRefreshView)(mActivity.findViewById(R.id.dailylist_pull_refresh_view));
		mAdapter = new DailyListAdapter(getActivity().getApplicationContext(),new ArrayList<Daily>());
		mDailyList = (ListView)mActivity.findViewById(R.id.dailyList);
		mDailyList.setAdapter(mAdapter);
		mDailyBusi = new DailyBusi(mHandler);
		mDailyBusi.getDailyList(0,0);
		
//		setListAdapter(new DataAdapter(this));
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
	}
	
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if(null != mDailyBusi){
			Log.d(TAG, "===sDaily_Page_Current:"+AppConstants.sDaily_Page_Current);
			Log.d(TAG, "===sDaily_Page_Sum:"+AppConstants.sDaily_Page_Sum);
			if(AppConstants.sDaily_Page_Current >= AppConstants.sDaily_Page_Sum){
				// TODO 当前页是最后一页
				Log.v(TAG, "===当前页是最后一页");
				mPullToRefreshView.onFooterRefreshComplete();
				Message msg = mHandler.obtainMessage();
				msg.getData().putString("message", "当前是最后一页，没有更多历史日志!");
				msg.sendToTarget();
			}else{
				mDailyBusi.getDailyList(AppConstants.sDaily_Page_Current + 1, AppConstants.sDaily_Page_Sum);
			}
		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if(null != mDailyBusi){
			AppConstants.sDaily_Page_Current = 0;
			mDailyBusi.getDailyList(0, 0);
		}
	}

	@Override
	public void onHeaderRefreshSetText(PullToRefreshView view) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

}
