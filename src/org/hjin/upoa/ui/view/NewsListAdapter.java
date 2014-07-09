package org.hjin.upoa.ui.view;

import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.model.NewsInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsListAdapter extends BaseAdapter {
	
	private final String TAG = "NewsListAdapter";
	
	public final static int NEWSID = -1;
	
	private LayoutInflater mLayoutInflater;
	
	private List<NewsInfo> mList;
	
	public NewsListAdapter(Context context,List<NewsInfo> list){
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
			convertView = mLayoutInflater.inflate(R.layout.fragment_newslist_item, null);
			vh = new ViewHolder();
			vh.title = (TextView)convertView.findViewById(R.id.newsListTitle);
			vh.time = (TextView)convertView.findViewById(R.id.newsListTime);
			convertView.setTag(vh);
		}
		NewsInfo ni = mList.get(position);
		vh.title.setText(ni.getmTitle());
		vh.time.setText(ni.getmTime());
		convertView.setTag(NEWSID, ni.getmId());
		return convertView;
	}
	
	public void append(List<NewsInfo> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}
	
	public void setData(List<NewsInfo> list){
		this.mList.clear();
		this.mList.addAll(list);
		Log.v(TAG, "==="+this.mList.size());
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		public TextView title;
		public TextView time;
	}

}
