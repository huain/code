package org.hjin.upoa.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hjin.upoa.R;
import org.hjin.upoa.constants.AppConstants;
import org.hjin.upoa.model.Secretary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SecretaryActivity extends BaseActivity {
	
	private Secretary mSecretary;
	
	private ListView mLs;
	
	private TextView mNoResult;
	
	
	private List<Map<String,String>> mGroupData;
	
	private List<List<Map<String,String>>> mChildData;
	
	private List<Map<String,String>> mArwaitData;
	
	private LayoutInflater mLayoutInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secretary);
		
		mLayoutInflater = getLayoutInflater();
		
		mLs = (ListView)findViewById(R.id.secretaryList);
		mNoResult = (TextView)findViewById(R.id.secretaryNoResult);
		
		
		mSecretary = AppConstants.sSecretary;
		if(mSecretary == null || mSecretary.getAllCount()<=0){
			mLs.setVisibility(View.GONE);
			mNoResult.setVisibility(View.VISIBLE);
			Log.d("SecretaryActivity", "*****"+(mSecretary == null?null:mSecretary.getAllCount()));
		}else{
			mGroupData = makeListData(mSecretary);
			if(mGroupData != null && mGroupData.size()>0){
				String[] from = new String[]{"icon","text","num"};
				int[] to = new int[]{R.id.secretary_item_icon,R.id.secretary_item_text,R.id.secretary_item_num};
				mLs.setAdapter(new SimpleAdapter(this, mGroupData, R.layout.secretary_list_item, from, to));
				Log.d("SecretaryActivity", "*****"+mSecretary.getAllCount());
			}
		}
	}
	
	private List<Map<String,String>> makeListData(Secretary secretary){
		if(secretary == null){
			return null;
		}
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if (secretary.getArwait() > 0) {
			list.add(makeListItemData(R.drawable.about, "工单待办",
					secretary.getArwait()));
		}
		if (secretary.getCmdbwait() > 0) {
			list.add(makeListItemData(R.drawable.about, "资产待办",
					secretary.getCmdbwait()));
		}
		if (secretary.getLeaveCount() > 0) {
			list.add(makeListItemData(R.drawable.about, "请假待办",
					secretary.getLeaveCount()));
		}
		if (secretary.getTravelCount() > 0) {
			list.add(makeListItemData(R.drawable.about, "出差待办",
					secretary.getTravelCount()));
		}
		if (secretary.getFinanceCount() > 0) {
			list.add(makeListItemData(R.drawable.about, "新财务报销单",
					secretary.getFinanceCount()));
		}
		if (secretary.getFinanceReadCount() > 0) {
			list.add(makeListItemData(R.drawable.about, "财务单待阅",
					secretary.getFinanceReadCount()));
		}
		if (secretary.getYdCount() > 0) {
			list.add(makeListItemData(R.drawable.about, "人员异动待办",
					secretary.getYdCount()));
		}
		if (secretary.getYd2Count() > 0) {
			list.add(makeListItemData(R.drawable.about, "人员异动待阅",
					secretary.getYd2Count()));
		}
		if (secretary.getAssetCount() > 0) {
			list.add(makeListItemData(R.drawable.about, "资产待办",
					secretary.getAssetCount()));
		}
		if (secretary.getPurchaseCount() > 0) {
			list.add(makeListItemData(R.drawable.about, "采购待办",
					secretary.getPurchaseCount()));
		}
		if (secretary.getTravelxmCount() > 0) {
			list.add(makeListItemData(R.drawable.about, "新出差待办",
					secretary.getTravelxmCount()));
		}
		if (secretary.getHr1Count() > 0) {
			list.add(makeListItemData(R.drawable.about, "HR工单（提交单据）待办",
					secretary.getHr1Count()));
		}
		if (secretary.getHr2Count() > 0) {
			list.add(makeListItemData(R.drawable.about, "HR工单（填写网上信息）待办",
					secretary.getHr2Count()));
		}
		if (secretary.getHr3Count() > 0) {
			list.add(makeListItemData(R.drawable.about, "HR工单（修改网上信息）待办",
					secretary.getHr3Count()));
		}
		if (secretary.getHr4Count() > 0) {
			list.add(makeListItemData(R.drawable.about, "HR工单（提交原件）待办",
					secretary.getHr4Count()));
		}
		if (secretary.getHr5Count() > 0) {
			list.add(makeListItemData(R.drawable.about, "HR工单（取回办理证件）待办",
					secretary.getHr5Count()));
		}
		if (secretary.getHr6Count() > 0) {
			list.add(makeListItemData(R.drawable.about, "HR工单（领取居住证）待办",
					secretary.getHr6Count()));
		}
		if (secretary.getMpCount() > 0) {
			list.add(makeListItemData(R.drawable.about, "名片待办",
					secretary.getMpCount()));
		}
		if (secretary.getMp2Count() > 0) {
			list.add(makeListItemData(R.drawable.about, "名片待阅",
					secretary.getMp2Count()));
		}
		if (secretary.getIt1() > 0) {
			list.add(makeListItemData(R.drawable.about, "IT工单待评分", secretary.getIt1()));
		}
		if (secretary.getIt2() > 0) {
			list.add(makeListItemData(R.drawable.about, "IT工单待受理", secretary.getIt2()));
		}
		if (secretary.getIt3() > 0) {
			list.add(makeListItemData(R.drawable.about, "虚拟机申请单待受理", secretary.getIt3()));
		}
		if (secretary.getIt4() > 0) {
			list.add(makeListItemData(R.drawable.about, "虚拟机申请单待评估", secretary.getIt4()));
		}
		if (secretary.getIt5() > 0) {
			list.add(makeListItemData(R.drawable.about, "邮件组申请单待受理", secretary.getIt5()));
		}
		if (secretary.getIt6() > 0) {
			list.add(makeListItemData(R.drawable.about, "海外邮箱申请单待受理", secretary.getIt6()));
		}
		if (secretary.getIt7() > 0) {
			list.add(makeListItemData(R.drawable.about, "SSL VPN申请单待受理", secretary.getIt7()));
		}
		if (secretary.getIt8() > 0) {
			list.add(makeListItemData(R.drawable.about, "泰岳邮箱申请单待受理", secretary.getIt8()));
		}
		if (secretary.getIt9() > 0) {
			list.add(makeListItemData(R.drawable.about, "基金邮箱申请单待受理", secretary.getIt9()));
		}
		if (secretary.getIt10() > 0) {
			list.add(makeListItemData(R.drawable.about, "非员工邮箱申请单待受理", secretary.getIt10()));
		}
		if (secretary.getIt11() > 0) {
			list.add(makeListItemData(R.drawable.about, "网络策略申请单待受理", secretary.getIt11()));
		}
		if (secretary.getIt12() > 0) {
			list.add(makeListItemData(R.drawable.about, "虚拟机申请单待审批", secretary.getIt12()));
		}
		if (secretary.getIt13() > 0) {
			list.add(makeListItemData(R.drawable.about, "邮件组申请单待审批", secretary.getIt13()));
		}
		if (secretary.getIt14() > 0) {
			list.add(makeListItemData(R.drawable.about, "海外邮箱申请单待审批", secretary.getIt14()));
		}
		if (secretary.getIt15() > 0) {
			list.add(makeListItemData(R.drawable.about, "SSL VPN申请单待审批", secretary.getIt15()));
		}
		if (secretary.getIt16() > 0) {
			list.add(makeListItemData(R.drawable.about, "泰岳邮箱申请单待审批", secretary.getIt16()));
		}
		if (secretary.getIt17() > 0) {
			list.add(makeListItemData(R.drawable.about, "基金邮箱申请单待审批", secretary.getIt17()));
		}
		if (secretary.getIt18() > 0) {
			list.add(makeListItemData(R.drawable.about, "非员工邮箱申请单待审批", secretary.getIt18()));
		}
		if (secretary.getIt19() > 0) {
			list.add(makeListItemData(R.drawable.about, "网络策略申请单待审批", secretary.getIt19()));
		}
		if (secretary.getJx() > 0) {
			list.add(makeListItemData(R.drawable.about, "绩效待办工单待审批", secretary.getJx()));
		}
		return list;

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
			if(getGroupText(groupPosition).equals("工单待办")){
//				int count = Integer.parseInt(m.get("num"));
				return mArwaitData == null ? 0 :(mArwaitData.size()>5?5:mArwaitData.size());
			}else{
				return 0;
			}
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mGroupData.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			if(getGroupText(groupPosition).equals("工单待办")){
				return mArwaitData.get(childPosition);
			}
			return null;
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
				convertView.setTag(gvh);
			}
			Map<String,String> data = mGroupData.get(groupPosition);
			gvh.title.setText(data.get("text"));
			gvh.num.setText(data.get("num"));
			gvh.icon.setImageResource(Integer.parseInt(data.get("icon")));
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
//			if()
			return null;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
		
		private String getGroupText(int groupPosition){
			Map<String,String> m = mGroupData.get(groupPosition);
			return m.get("text");
		}
		
		private class GroupViewHolder{
			public TextView title;
			public TextView num;
			public ImageView icon;
			
		}
	}

	

}
