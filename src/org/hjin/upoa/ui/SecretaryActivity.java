package org.hjin.upoa.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.SecretaryBusi;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.Secretary;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

public class SecretaryActivity extends BaseActivity {
	
	private final static String TAG = "SecretaryActivity";
	
	private Secretary mSecretary;
	
	private ExpandableListView mLs;
	
	private TextView mNoResult;
	
	private BaseExpandableListAdapter mAdapter;
	
	
	private List<Map<String,String>> mGroupData;
	
	private List<List<Map<String,String>>> mChildData;
	
	private int mArwaitPosition;
	
	private List<Map<String,String>> mArwaitData = new ArrayList<Map<String,String>>();
	
	private LayoutInflater mLayoutInflater;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			switch(msg.what){
			case SecretaryBusi.GET_ARWAIT:{
				mChildData.set(mArwaitPosition, (List<Map<String,String>>)data.getSerializable("result"));
//				mArwaitData = mChildData.get(mArwaitPosition) = (List<Map<String,String>>)data.getSerializable("result");
				Log.d(TAG, "===handleMessage:"+mArwaitData.size());
				if(mAdapter != null){
					mAdapter.notifyDataSetChanged();
				}
			}break;
			}
		}
		
	};
	
	private SecretaryBusi mSecretaryBusi = new SecretaryBusi(mHandler);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secretary);
		
		mLayoutInflater = getLayoutInflater();
		
		mLs = (ExpandableListView)findViewById(R.id.secretaryList);
		mLs.setGroupIndicator(null);
		mNoResult = (TextView)findViewById(R.id.secretaryNoResult);
		
		mSecretary = AppConstants.sSecretary;
		if(mSecretary == null || mSecretary.getAllCount()<=0){
			mLs.setVisibility(View.GONE);
			mNoResult.setVisibility(View.VISIBLE);
			Log.d("SecretaryActivity", "*****"+(mSecretary == null?null:mSecretary.getAllCount()));
		}else{
			makeListData(mSecretary);
			if(mGroupData != null && mGroupData.size()>0){
//				String[] from = new String[]{"icon","text","num"};
//				int[] to = new int[]{R.id.secretary_item_icon,R.id.secretary_item_text,R.id.secretary_item_num};
//				mLs.setAdapter(new SimpleAdapter(this, mGroupData, R.layout.secretary_list_item, from, to));
				BaseExpandableListAdapter mAdapter = new ExpandableAdapter();
				mLs.setAdapter(mAdapter);
				Log.d("SecretaryActivity", "*****"+mSecretary.getAllCount());
			}
		}
	}
	
	private void makeListData(Secretary secretary){
		if(secretary == null){
			return;
		}
		mGroupData = new ArrayList<Map<String,String>>();
		mChildData = new ArrayList<List<Map<String,String>>>();
		if (secretary.getArwait() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "工单待办",
					secretary.getArwait()));
			mArwaitPosition = mGroupData.size()-1;
			mChildData.add(mArwaitData);
			mSecretaryBusi.getArWait();
		}
		if (secretary.getCmdbwait() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "资产待办",
					secretary.getCmdbwait()));
			mChildData.add(null);
		}
		if (secretary.getLeaveCount() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "请假待办",
					secretary.getLeaveCount()));
			mChildData.add(null);
		}
		if (secretary.getTravelCount() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "出差待办",
					secretary.getTravelCount()));
			mChildData.add(null);
		}
		if (secretary.getFinanceCount() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "新财务报销单",
					secretary.getFinanceCount()));
			mChildData.add(null);
		}
		if (secretary.getFinanceReadCount() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "财务单待阅",
					secretary.getFinanceReadCount()));
			mChildData.add(null);
		}
		if (secretary.getYdCount() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "人员异动待办",
					secretary.getYdCount()));
			mChildData.add(null);
		}
		if (secretary.getYd2Count() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "人员异动待阅",
					secretary.getYd2Count()));
			mChildData.add(null);
		}
		if (secretary.getAssetCount() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "资产待办",
					secretary.getAssetCount()));
			mChildData.add(null);
		}
		if (secretary.getPurchaseCount() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "采购待办",
					secretary.getPurchaseCount()));
			mChildData.add(null);
		}
		if (secretary.getTravelxmCount() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "新出差待办",
					secretary.getTravelxmCount()));
			mChildData.add(null);
		}
		if (secretary.getHr1Count() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "HR工单（提交单据）待办",
					secretary.getHr1Count()));
			mChildData.add(null);
		}
		if (secretary.getHr2Count() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "HR工单（填写网上信息）待办",
					secretary.getHr2Count()));
			mChildData.add(null);
		}
		if (secretary.getHr3Count() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "HR工单（修改网上信息）待办",
					secretary.getHr3Count()));
			mChildData.add(null);
		}
		if (secretary.getHr4Count() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "HR工单（提交原件）待办",
					secretary.getHr4Count()));
			mChildData.add(null);
		}
		if (secretary.getHr5Count() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "HR工单（取回办理证件）待办",
					secretary.getHr5Count()));
			mChildData.add(null);
		}
		if (secretary.getHr6Count() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "HR工单（领取居住证）待办",
					secretary.getHr6Count()));
			mChildData.add(null);
		}
		if (secretary.getMpCount() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "名片待办",
					secretary.getMpCount()));
			mChildData.add(null);
		}
		if (secretary.getMp2Count() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "名片待阅",
					secretary.getMp2Count()));
			mChildData.add(null);
		}
		if (secretary.getIt1() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "IT工单待评分", secretary.getIt1()));
			mChildData.add(null);
		}
		if (secretary.getIt2() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "IT工单待受理", secretary.getIt2()));
			mChildData.add(null);
		}
		if (secretary.getIt3() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "虚拟机申请单待受理", secretary.getIt3()));
			mChildData.add(null);
		}
		if (secretary.getIt4() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "虚拟机申请单待评估", secretary.getIt4()));
			mChildData.add(null);
		}
		if (secretary.getIt5() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "邮件组申请单待受理", secretary.getIt5()));
			mChildData.add(null);
		}
		if (secretary.getIt6() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "海外邮箱申请单待受理", secretary.getIt6()));
			mChildData.add(null);
		}
		if (secretary.getIt7() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "SSL VPN申请单待受理", secretary.getIt7()));
			mChildData.add(null);
		}
		if (secretary.getIt8() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "泰岳邮箱申请单待受理", secretary.getIt8()));
			mChildData.add(null);
		}
		if (secretary.getIt9() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "基金邮箱申请单待受理", secretary.getIt9()));
			mChildData.add(null);
		}
		if (secretary.getIt10() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "非员工邮箱申请单待受理", secretary.getIt10()));
			mChildData.add(null);
		}
		if (secretary.getIt11() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "网络策略申请单待受理", secretary.getIt11()));
			mChildData.add(null);
		}
		if (secretary.getIt12() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "虚拟机申请单待审批", secretary.getIt12()));
			mChildData.add(null);
		}
		if (secretary.getIt13() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "邮件组申请单待审批", secretary.getIt13()));
			mChildData.add(null);
		}
		if (secretary.getIt14() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "海外邮箱申请单待审批", secretary.getIt14()));
			mChildData.add(null);
		}
		if (secretary.getIt15() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "SSL VPN申请单待审批", secretary.getIt15()));
			mChildData.add(null);
		}
		if (secretary.getIt16() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "泰岳邮箱申请单待审批", secretary.getIt16()));
			mChildData.add(null);
		}
		if (secretary.getIt17() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "基金邮箱申请单待审批", secretary.getIt17()));
			mChildData.add(null);
		}
		if (secretary.getIt18() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "非员工邮箱申请单待审批", secretary.getIt18()));
			mChildData.add(null);
		}
		if (secretary.getIt19() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "网络策略申请单待审批", secretary.getIt19()));
			mChildData.add(null);
		}
		if (secretary.getJx() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "绩效待办工单待审批", secretary.getJx()));
			mChildData.add(null);
		}
		
		if (secretary.getKqdtj() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "考勤单待提交", secretary.getKqdtj()));
			mChildData.add(null);
		}
		
		if (secretary.getKqdsp() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "考勤单待审批", secretary.getKqdsp()));
			mChildData.add(null);
		}
		
		if (secretary.getZzcg() > 0) {
			mGroupData.add(makeListItemData(R.drawable.about, "新媒转正草稿待处理", secretary.getZzcg()));
			mChildData.add(null);
		}
		
	}
	
	/**
	 * 
	 * @param imageSrc
	 * @param text
	 * @param num
	 * @return
	 */
	private Map<String,String> makeListItemData(int imageSrc,String text,int num){
		Map<String,String> m = new HashMap<String,String>();
		m.put("icon", imageSrc+"");
		m.put("text", text);
		m.put("num", num+"");
		return m;
	}
	
	
	private class ExpandableAdapter extends BaseExpandableListAdapter{
		
		@Override
		public int getGroupCount() {
			return mGroupData.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			Log.d(TAG, "===getChildrenCount:"+(mChildData == null ? 0 :
				((mChildData.size()==0 || mChildData.get(groupPosition) == null) ? 0 : 
					(mChildData.get(groupPosition).size()))));
			return mChildData == null ? 0 :
				((mChildData.size()==0 || mChildData.get(groupPosition) == null) ? 0 : 
					(mChildData.get(groupPosition).size()));
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mGroupData.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return mChildData == null ? null :
				(mChildData.get(groupPosition) == null ? null :
					mChildData.get(groupPosition).get(childPosition));
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return groupPosition<<32+childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder gvh;
			if(convertView != null){
				gvh = (GroupViewHolder)convertView.getTag();
			}else{
				convertView = mLayoutInflater.inflate(R.layout.secretary_list_item, null);
				gvh = new GroupViewHolder();
				gvh.title = (TextView)convertView.findViewById(R.id.secretary_item_text);
				gvh.num = (TextView)convertView.findViewById(R.id.secretary_item_num);
				gvh.icon = (ImageView)convertView.findViewById(R.id.secretary_item_icon);
				gvh.indiator = (ImageView)convertView.findViewById(R.id.secretary_item_indicator);
				convertView.setTag(gvh);
			}
			if(groupPosition == mArwaitPosition){
				gvh.indiator.setVisibility(View.VISIBLE);
			}else{
				gvh.indiator.setVisibility(View.INVISIBLE);
			}
			Map<String,String> data = mGroupData.get(groupPosition);
			gvh.title.setText(data.get("text"));
			gvh.num.setText(data.get("num"));
			gvh.icon.setImageResource(Integer.parseInt(data.get("icon")));
			if(isExpanded){
				gvh.indiator.setImageResource(R.drawable.ic_action_expand);
			}else{
				gvh.indiator.setImageResource(R.drawable.ic_action_collapse);
			}
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder cvh;
			if(convertView != null){
				cvh = (ChildViewHolder)convertView.getTag();
			}else{
				convertView = mLayoutInflater.inflate(R.layout.secretary_waitdeallist_item, null);
				cvh = new ChildViewHolder();
				cvh.index = (TextView)convertView.findViewById(R.id.secretary_waitdeallist_index);
				cvh.title = (TextView)convertView.findViewById(R.id.secretary_waitdeallist_title);
				cvh.time = (TextView)convertView.findViewById(R.id.secretary_waitdeallist_time);
				cvh.status = (TextView)convertView.findViewById(R.id.secretary_waitdeallist_status);
				convertView.setTag(cvh);
			}
			Map<String,String> data = mChildData.get(groupPosition).get(childPosition);
			cvh.index.setText(data.get("index"));
			cvh.title.setText(data.get("dealPerson"));
			cvh.time.setText(data.get("dealTime"));
			cvh.status.setText(data.get("dealStatus"));
			Log.d(TAG,"===getChildView:"+(childPosition+1)+":"+data.get("dealPerson")+":"+data.get("dealTime")+":"+data.get("dealStatus"));
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
		
//		private String getGroupText(int groupPosition){
//			Map<String,String> m = mGroupData.get(groupPosition);
//			return m.get("text");
//		}
		
		private class GroupViewHolder{
			public TextView title;
			public TextView num;
			public ImageView icon;
			public ImageView indiator;
			
		}
		
		private class ChildViewHolder{
			public TextView index;
			public TextView title;
			public TextView time;
			public TextView status;
			
		}
	}

	

}
