package org.hjin.upoa.ui.view;

import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.model.Daily;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DailyListAdapter extends BaseAdapter {
	
	private final String TAG = "DailyListAdapter";
	
	private LayoutInflater mLayoutInflater;
	
	private List<Daily> mList;
	
	
	public DailyListAdapter(Context context,List<Daily> list){
		mLayoutInflater = LayoutInflater.from(context);
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return  position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		ViewHolder vh;
		if(convertView != null){
			vh = (ViewHolder)convertView.getTag();
		}else{
			convertView = mLayoutInflater.inflate(R.layout.fragment_dailylist_item, null);
			vh = new ViewHolder();
			vh.subject = (TextView)convertView.findViewById(R.id.daily_subject);
			vh.code = (TextView)convertView.findViewById(R.id.daily_code);
			vh.name = (TextView)convertView.findViewById(R.id.daily_name);
			vh.begintime = (TextView)convertView.findViewById(R.id.daily_begintime);
			vh.endtime = (TextView)convertView.findViewById(R.id.daily_endtime);
			vh.time = (TextView)convertView.findViewById(R.id.daily_time);
			vh.desc = (TextView)convertView.findViewById(R.id.daily_desc);
			convertView.setTag(vh);
		}
		Daily daily = mList.get(position);
		vh.subject.setText(daily.getSubject());
		vh.code.setText(daily.getCode());
		vh.name.setText(daily.getName());
		vh.begintime.setText(daily.getBegintime());
		vh.endtime.setText(daily.getEndtime());
		vh.time.setText(daily.getTime());
		vh.desc.setText(daily.getDesc());
		
		return convertView;
	}
	
	public void append(List<Daily> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}
	
	public void setData(List<Daily> list){
		this.mList.clear();
		this.mList.addAll(list);
		Log.d(TAG, "==="+this.mList.size());
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView subject;
		public TextView code;
		public TextView name;
		public TextView begintime;
		public TextView endtime;
		public TextView time;
		public TextView desc;
	}
	

}
