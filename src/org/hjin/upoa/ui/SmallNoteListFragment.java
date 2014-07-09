package org.hjin.upoa.ui;


import java.util.ArrayList;
import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.SmallNoteBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.SmallNote;
import org.hjin.upoa.ui.view.PullToRefreshView;
import org.hjin.upoa.ui.view.PullToRefreshView.OnFooterRefreshListener;
import org.hjin.upoa.ui.view.PullToRefreshView.OnHeaderRefreshListener;
import org.hjin.upoa.ui.view.SmallNoteListAdapter;
import org.hjin.upoa.ui.view.SmallNoteListAdapter.onReplyClickListener;
import org.hjin.upoa.ui.view.SmallNoteReplyDialogFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 小字报列表
 * @author Administrator
 *
 */
public class SmallNoteListFragment extends BaseFragment implements OnHeaderRefreshListener,OnFooterRefreshListener,onReplyClickListener{
	
	private final String TAG = "SmallNoteListFragment";
	
	private PullToRefreshView mPullToRefreshView;
	
	private ListView mSamllNoteList;
	
	private SmallNoteListAdapter mAdapter;
	
	private SmallNoteBusi mSmallNoteBusi;
	
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
					case SmallNoteBusi.GETSMALLNOTELIST:{
						Bundle data = msg.getData();
						if(data.containsKey("listContent")){
							List<SmallNote> list = (List<SmallNote>)data.getSerializable("listContent");
							if(AppConstants.sSmallNote_Page_Current == 1){
								mAdapter.setData(list);
							}else{
								mAdapter.append(list);
							}
							
							mPullToRefreshView.onHeaderRefreshComplete();
							mPullToRefreshView.onFooterRefreshComplete();
						}
						if(mV.findViewById(R.id.smallnotelist_load).VISIBLE == View.VISIBLE){
							mV.findViewById(R.id.smallnotelist_load).setVisibility(View.GONE);
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
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}




	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.fragment_smallnote, menu);
	}
	
	




	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_newsmallnote:{
			onClick("","");
		}break;
		default:break;
		}
		return true;
		//return super.onOptionsItemSelected(item);
	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mV = inflater.inflate(R.layout.fragment_smallnotelist, container, false);
		loadingAnimatorActive(mV.findViewById(R.id.smallnotelist_load));
		return mV;
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		AppConstants.sSmallNote_Page_Current = 0;
		AppConstants.sSmallNote_Page_Sum = 0;
		AppConstants.sSmallNote_Sum = 0;
		mPullToRefreshView = (PullToRefreshView)(mActivity.findViewById(R.id.smallnotelist_pull_refresh_view));
		mAdapter = new SmallNoteListAdapter(getActivity().getApplicationContext(),new ArrayList<SmallNote>(),this);
		mSamllNoteList = (ListView)mActivity.findViewById(R.id.smallnoteList);
		mSamllNoteList.setAdapter(mAdapter);
		mSmallNoteBusi = new SmallNoteBusi(mHandler);
		mSmallNoteBusi.getSmallNoteList(0, 0);
		
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
	}
	
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if(null != mSmallNoteBusi){
			if(AppConstants.sSmallNote_Page_Current >= AppConstants.sSmallNote_Page_Sum){
				// TODO 当前页是最后一页
				Log.v(TAG, "===当前页是最后一页");
				mPullToRefreshView.onFooterRefreshComplete();
				Message msg = mHandler.obtainMessage();
				msg.getData().putString("message", "当前是最后一页，没有更多小字报!");
				msg.sendToTarget();
			}else{
				mSmallNoteBusi.getSmallNoteList(AppConstants.sSmallNote_Page_Current + 1, AppConstants.sSmallNote_Page_Sum);
			}
		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		if(null != mSmallNoteBusi){
			AppConstants.sSmallNote_Page_Current = 0;
			mSmallNoteBusi.getSmallNoteList(0, 0);
		}
	}

	@Override
	public void onHeaderRefreshSetText(PullToRefreshView view) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onClick(String username, String userid) {
		SmallNoteReplyDialogFragment df = SmallNoteReplyDialogFragment.newInstance(username,userid);
	    FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("smallNoteReply");
	    if (prev != null) {
	    	ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    df.setCancelable(false);
	    df.show(ft, "smallNoteReply");
	}
	
	
	
	

}
