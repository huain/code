/**
 * 
 */
package org.hjin.upoa.ui;

import java.util.ArrayList;
import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.SmallNoteBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.SmallNote;
import org.hjin.upoa.ui.view.ProgressDialogFragment;
import org.hjin.upoa.ui.view.PullToRefreshView;
import org.hjin.upoa.ui.view.SmallNoteListAdapter;
import org.hjin.upoa.ui.view.SmallNoteReplyDialogFragment;
import org.hjin.upoa.ui.view.PullToRefreshView.OnFooterRefreshListener;
import org.hjin.upoa.ui.view.PullToRefreshView.OnHeaderRefreshListener;
import org.hjin.upoa.ui.view.SmallNoteListAdapter.onReplyClickListener;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

/**
 * 小字报
 * @author huangjin
 * 2014-8-14
 */
public class SmallNoteActivity extends BaseActivity implements OnHeaderRefreshListener,OnFooterRefreshListener,onReplyClickListener{
	
	private final String TAG = "SmallNoteAtivity";
	
	private SmallNoteBusi mSmallNoteBusi;
	
	private PullToRefreshView mPullToRefreshView;
	
	private ListView mSamllNoteList;
	
	private SmallNoteListAdapter mAdapter;
	
	
	private Handler mHandler = new BaseHandler(this){
		@Override
		public void handleMessage(Message msg) {
			try {
				super.handleMessage(msg);
				switch(msg.what){
				// list数据返回，界面处理
				case SmallNoteBusi.GETSMALLNOTELIST:{
					Bundle data = msg.getData();
					if(data.containsKey("listContent")){
						@SuppressWarnings("unchecked")
						List<SmallNote> list = (List<SmallNote>)data.getSerializable("listContent");
						if(AppConstants.sSmallNote_Page_Current == 1){
							mAdapter.setData(list);
						}else{
							mAdapter.append(list);
						}
						
						mPullToRefreshView.onHeaderRefreshComplete();
						mPullToRefreshView.onFooterRefreshComplete();
					}
					removeDialogByTag("loading");
//					if(mPdf !=null){
//						findViewById(R.id.smallnotelist_load).setVisibility(View.GONE);
//					}
				}break;
				default:break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smallnotelist);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("小字报");
		
		AppConstants.sSmallNote_Page_Current = 0;
		AppConstants.sSmallNote_Page_Sum = 0;
		AppConstants.sSmallNote_Sum = 0;
		mPullToRefreshView = (PullToRefreshView)(findViewById(R.id.smallnotelist_pull_refresh_view));
		mAdapter = new SmallNoteListAdapter(getApplicationContext(),new ArrayList<SmallNote>(),this);
		mSamllNoteList = (ListView)findViewById(R.id.smallnoteList);
		mSamllNoteList.setAdapter(mAdapter);
		mSmallNoteBusi = new SmallNoteBusi(mHandler);
		mSmallNoteBusi.getSmallNoteList(0, 0);
		
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        
        mPdf = ProgressDialogFragment.newInstance("正在加载……");
	    showDialog(mPdf, "loading");
        
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_smallnote, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_newsmallnote:{
			onClick("","");
		}break;
		default:
			return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
	
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		if(null != mSmallNoteBusi){
			if(AppConstants.sSmallNote_Page_Current >= AppConstants.sSmallNote_Page_Sum){
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
		if(null != mSmallNoteBusi){
			AppConstants.sSmallNote_Page_Current = 0;
			mSmallNoteBusi.getSmallNoteList(0, 0);
		}
	}

	@Override
	public void onHeaderRefreshSetText(PullToRefreshView view) {
		
	}

	/* (non-Javadoc)
	 * @see org.hjin.upoa.ui.view.SmallNoteListAdapter.onReplyClickListener#onClick(java.lang.String, java.lang.String)
	 */
	@Override
	public void onClick(String username, String userid) {
		SmallNoteReplyDialogFragment df = SmallNoteReplyDialogFragment.newInstance(username,userid);
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("smallNoteReply");
	    if (prev != null) {
	    	ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    df.setCancelable(false);
	    df.show(ft, "smallNoteReply");
		
	}

}
