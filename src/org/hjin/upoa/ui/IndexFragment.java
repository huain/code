package org.hjin.upoa.ui;

import java.util.List;
import java.util.Map;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.IndexBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.util.Utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class IndexFragment extends BaseFragment {
	
	private final String TAG = "IndexFragment"; 
	
	private IndexBusi mIndexBusi;
	
	private Activity mActivity;
	
	private ImageView mHeader;
	
	private TextView mFullname;
	
	private TextView mPost;
	
	private TextView mDep;
	
	private TextView mOnLineSum;
	
	private TextView mWaitDealSum;
	
	
	private ListView mWaitDealList;
	
	private TextView mWaitDealList_None;
	
	
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
					Bundle data = msg.getData();
					switch(msg.what){
					case IndexBusi.GETUSERINFO:{
						if(!Utility.isBlank(data.getString("fullname"))){
							mFullname.setText(data.getString("fullname"));
						}
						if(!Utility.isBlank(data.getString("userpost"))){
							mPost.setText(data.getString("userpost"));
						}
						if(!Utility.isBlank(data.getString("userdep"))){
							mDep.setText(data.getString("userdep"));
						}
					}break;
					case IndexBusi.GETUSERHEADERINFO:{
						mHeader.setImageDrawable(Utility.getDrawableFromSD(
								AppConstants.sReq_IndexUserHeaderInfo+AppConstants.loginname+"_head_160.jpg"));
					}break;
					case IndexBusi.GETUSERHEADERINFO_F:{}break;
//					case IndexBusi.GETONLINESUM:{
//						if(!Utility.isBlank(data.getString("result"))){
//							mOnLineSum.setText(data.getString("result"));
//						}
//					}break;
//					case IndexBusi.GETWAITDEALSUM:{
//						if(!Utility.isBlank(data.getString("result"))){
//							mWaitDealSum.setText(data.getString("result"));
//						}
//					}break;
					case IndexBusi.GETDAILYINFO:{
						List<String> list = (List<String>)data.getSerializable("result");
						int dataLength = 0;
						if(null != list){
							dataLength = list.size();
						}
						Log.d(TAG, "===GETDAILYINFO:"+list.toString());
						for(int i=0;i<8;i++){
							String idAreaPrefix = "index_dailyinfo_item_area";
							String idPrefix = "index_dailyinfo_item";
							TextView tv = (TextView)mActivity.findViewById(getResources().getIdentifier(idPrefix+i, "id", "org.hjin.upoa"));
							LinearLayout ll = (LinearLayout)mActivity.findViewById(getResources().getIdentifier(idAreaPrefix+i, "id", "org.hjin.upoa"));
							final float scale = getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (16 * scale + 0.5f);
							if(i<dataLength){
								String status = list.get(dataLength - i -1);
								if("8".equals(status)){
									tv.setText("完成");
									ll.setBackgroundResource(R.drawable.green);
									ll.setPadding(0, padding_in_px, 0, padding_in_px);
								}else if("0".equals(status)){
									tv.setText("未写");
									ll.setBackgroundResource(R.drawable.red);
									ll.setPadding(0, padding_in_px, 0, padding_in_px);
								}else{
									tv.setText("不全");
									ll.setBackgroundResource(R.drawable.yellow);
									ll.setPadding(0, padding_in_px, 0, padding_in_px);
								}
							}else{
								tv.setText("未到");
							}
						}
					}break;
					case IndexBusi.GETWAITDEALINFO:{
						List<Map<String,String>> list = (List<Map<String,String>>)data.getSerializable("result");
						if(null != list && list.size()>0){
							mWaitDealList_None.setVisibility(View.GONE);
							ListAdapter aa = new SimpleAdapter(mActivity.getApplicationContext(), list, R.layout.index_waitdeallist_item, new String[]{"index","dealName","dealTime","dealStatus"}, new int[]{R.id.index_waitdeallist_index,R.id.index_waitdeallist_title,R.id.index_waitdeallist_time,R.id.index_waitdeallist_status});
							mWaitDealList.setAdapter(aa);
						}else{
							mWaitDealList_None.setVisibility(View.VISIBLE);
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
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_index, container, false);
		mHeader = (ImageView)v.findViewById(R.id.index_header);
		mFullname = (TextView)v.findViewById(R.id.index_fullname);
		mPost = (TextView)v.findViewById(R.id.index_post);
		mDep = (TextView)v.findViewById(R.id.index_dep);
		mOnLineSum = (TextView)v.findViewById(R.id.index_onlinesum);
		mWaitDealSum = (TextView)v.findViewById(R.id.index_waitdealsum);
		mWaitDealList = (ListView)v.findViewById(R.id.index_waitdeallist);
		mWaitDealList_None = (TextView)v.findViewById(R.id.index_waitdeallist_none);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mIndexBusi = new IndexBusi(mActivity, mHandler);
		mIndexBusi.getUserInfo();
		boolean isnonepic = PreferenceManager.getDefaultSharedPreferences(mActivity.getApplicationContext()).getBoolean("setting_item_nonepic", false);
		ConnectivityManager conMan = (ConnectivityManager)mActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if(isnonepic && (mobile==State.CONNECTED||mobile==State.CONNECTING)){
			
		}else{
			mIndexBusi.getUserHeaderInfo();
		}
		
//		mIndexBusi.getOnLineSum();
		mIndexBusi.getWaitDealSum();
		mIndexBusi.getDailyInfo();
		mIndexBusi.getWaitDealInfo();
	}
	
	
	
	
}
