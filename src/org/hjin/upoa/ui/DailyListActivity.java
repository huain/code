package org.hjin.upoa.ui;


import java.util.ArrayList;
import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.DailyBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.Daily;
import org.hjin.upoa.ui.view.DailyListAdapter;
import org.hjin.upoa.ui.view.PullToRefreshView;
import org.hjin.upoa.ui.view.PullToRefreshView.OnFooterRefreshListener;
import org.hjin.upoa.ui.view.PullToRefreshView.OnHeaderRefreshListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * 历史日报列表
 * @author Administrator
 *
 */
public class DailyListActivity extends BaseActivity implements OnHeaderRefreshListener,OnFooterRefreshListener{
	
	private final String TAG = "DailyListFragment";
	
	private PullToRefreshView mPullToRefreshView;
	
	private ListView mDailyList;
	
	private DailyListAdapter mAdapter;
	
	private DailyBusi mDailyBusi;
	
	private Handler mHandler = new BaseHandler(this){
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
				}break;
				default:break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}};
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dailylist);
		
		getActionBar().setTitle("日报列表");
		AppConstants.sDaily_Page_Current = 0;
		AppConstants.sDaily_Page_Sum=0;
		AppConstants.sDaily_Sum=0;
		
		mPullToRefreshView = (PullToRefreshView)(findViewById(R.id.dailylist_pull_refresh_view));
		mAdapter = new DailyListAdapter(this,new ArrayList<Daily>());
		mDailyList = (ListView)findViewById(R.id.dailyList);
		mDailyList.setAdapter(mAdapter);
		mDailyBusi = new DailyBusi(mHandler);
		mDailyBusi.getDailyList(0,0);
		
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
		
	}
	
	
	
	

}
