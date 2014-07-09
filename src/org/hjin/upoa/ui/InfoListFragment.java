package org.hjin.upoa.ui;


import java.util.ArrayList;
import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.InfoBusi;
import org.hjin.upoa.model.Info;
import org.hjin.upoa.ui.view.InfoListAdapter;
import org.hjin.upoa.util.Utility;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 通讯录列表
 * @author Administrator
 *
 */
public class InfoListFragment extends BaseFragment {
	
	private final String TAG = "InfoListFragment";
	
	private ListView mInfoList;
	
	private TextView mTv;
	
	private TextView mTv2;
	
	private InfoListAdapter mAdapter;
	
	private InfoBusi mInfoBusi;
	
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
					case InfoBusi.GETINFOLIST:{
						Bundle data = msg.getData();
						if(data.containsKey("listContent")){
							List<Info> list = (List<Info>)data.getSerializable("listContent");
							if(null != list && list.size()>0){
								Log.d(TAG, "===:n条");
								if(mInfoList.getVisibility() == View.GONE){
									mTv.setVisibility(View.GONE);
									mTv2.setVisibility(View.GONE);
									mInfoList.setVisibility(View.VISIBLE);
								}
								mAdapter.setData(list);
							}else{
								Log.d(TAG, "===:0条");
								if(mInfoList.getVisibility() == View.VISIBLE){
									mInfoList.setVisibility(View.GONE);
									mTv.setVisibility(View.VISIBLE);
								}
							}
							
						}else{
							if(mInfoList.getVisibility() == View.VISIBLE){
								mInfoList.setVisibility(View.GONE);
								mTv.setVisibility(View.VISIBLE);
							}
						}
//						if(mV.findViewById(R.id.infolist_load).VISIBLE == View.VISIBLE){
//							mV.findViewById(R.id.infolist_load).setVisibility(View.GONE);
//						}
						
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
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}




	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "===infolist:onCreateOptionsMenu");
		
		inflater.inflate(R.menu.fragment_info, menu);
		MenuItem i = menu.findItem(R.id.action_search);
		Button btn = (Button)(i.getActionView().findViewById(R.id.actionbar_search_btn));
		final EditText text = (EditText)i.getActionView().findViewById(R.id.actionbar_search);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utility.isBlank(text.getText().toString().trim())){
					return;
				}
				mInfoBusi = new InfoBusi(mHandler);
				mInfoBusi.getInfoList(text.getText().toString());
				
			}
		});
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mV = inflater.inflate(R.layout.fragment_infolist, container, false);
		//loadingAnimatorActive(mV.findViewById(R.id.infolist_load));
		return mV;
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mAdapter = new InfoListAdapter(getActivity().getApplicationContext(),new ArrayList<Info>());
		mInfoList = (ListView)mActivity.findViewById(R.id.infoList);
		mInfoList.setAdapter(mAdapter);
		mInfoBusi = new InfoBusi(mHandler);
		//mInfoBusi.getInfoList("huangjin");
		mTv = (TextView)mActivity.findViewById(R.id.infoNoResult);
		mTv2 = (TextView)mActivity.findViewById(R.id.infoLoad);
		
	}
	
	
}
