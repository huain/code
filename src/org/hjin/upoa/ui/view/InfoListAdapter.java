package org.hjin.upoa.ui.view;

import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.model.Info;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoListAdapter extends BaseAdapter {
	
	private final String TAG = "InfoListAdapter";
	
	private LayoutInflater mLayoutInflater;
	
	private List<Info> mList;
	
	
	public InfoListAdapter(Context context,List<Info> list){
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
			convertView = mLayoutInflater.inflate(R.layout.fragment_infolist_item, null);
			vh = new ViewHolder();
			vh.username = (TextView)convertView.findViewById(R.id.info_username);
			vh.dep = (TextView)convertView.findViewById(R.id.info_dep);
			vh.post = (TextView)convertView.findViewById(R.id.info_post);
			vh.tel = (TextView)convertView.findViewById(R.id.info_tel);
			vh.email = (TextView)convertView.findViewById(R.id.info_email);
			convertView.setTag(vh);
		}
		Info info = mList.get(position);
		vh.username.setText(info.getUsername());
		vh.dep.setText(info.getDep());
		vh.post.setText(info.getPost());
		vh.tel.setText(info.getTel());
		vh.email.setText(info.getEmail());
		
		return convertView;
	}
	
	public void append(List<Info> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}
	
	public void setData(List<Info> list){
		this.mList.clear();
		this.mList.addAll(list);
		Log.d(TAG, "==="+this.mList.size());
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView username;
		public TextView dep;
		public TextView tel;
		public TextView email;
		public TextView post;
	}
	

}
