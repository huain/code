package org.hjin.upoa.ui;


import java.util.ArrayList;
import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.InfoBusi;
import org.hjin.upoa.model.Info;
import org.hjin.upoa.ui.view.InfoListAdapter;
import org.hjin.upoa.util.Utility;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 通讯录列表
 * @author Administrator
 *
 */
public class InfoListActivity extends BaseActivity {
	
	private final String TAG = "InfoListFragment";
	
	private ListView mInfoList;
	
	private TextView mTv;
	
	private TextView mTv2;
	
	private InfoListAdapter mAdapter;
	
	private InfoBusi mInfoBusi;
	
	private Handler mHandler = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_infolist);
		getActionBar().setTitle("联系人");
		mAdapter = new InfoListAdapter(this,new ArrayList<Info>());
		mInfoList = (ListView)findViewById(R.id.infoList);
		mInfoList.setAdapter(mAdapter);
		
		//mInfoBusi.getInfoList("huangjin");
		mTv = (TextView)findViewById(R.id.infoNoResult);
		mTv2 = (TextView)findViewById(R.id.infoLoad);
		
		
		mHandler = new BaseHandler(this){
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
					}break;
					default:break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		mInfoBusi = new InfoBusi(mHandler);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fragment_info, menu);
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
		return true;
	}
	
}
