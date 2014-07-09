package org.hjin.upoa.ui.view;

import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.model.SmallNote;
import org.hjin.upoa.util.Utility;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SmallNoteListAdapter extends BaseAdapter {
	
	private final String TAG = "SmallNoteListAdapter";
	
	public final static int SMALLNOTEID = -1;
	
	public final static int SMALLNOTEUSERID = -2;
	
	private LayoutInflater mLayoutInflater;
	
	private List<SmallNote> mList;
	
	private onReplyClickListener mOnReplyClickListener;
	
	public SmallNoteListAdapter(Context context,List<SmallNote> list,onReplyClickListener listener){
		mLayoutInflater = LayoutInflater.from(context);
		this.mList = list;
		this.mOnReplyClickListener = listener;
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
			convertView = mLayoutInflater.inflate(R.layout.fragment_smallnotelist_item, null);
			vh = new ViewHolder();
			vh.title = (TextView)convertView.findViewById(R.id.smallnoteList_title);
			vh.time = (TextView)convertView.findViewById(R.id.smallnoteList_time);
			vh.username = (TextView)convertView.findViewById(R.id.smallnoteList_username);
			vh.userdep = (TextView)convertView.findViewById(R.id.smallnoteList_userdep);
//			vh.userheadpic = (ImageView)convertView.findViewById(R.id.smallnoteList_userheadpic);
			vh.content = (TextView)convertView.findViewById(R.id.smallnoteList_content);
			convertView.setTag(vh);
		}
		SmallNote sn = mList.get(position);
		vh.title.setText(sn.getTitle());
		vh.time.setText(sn.getTime());
		vh.username.setText(sn.getUsername());
		vh.userdep.setText(sn.getUserdep());
		if(!Utility.isBlank(sn.getContent())){
			vh.content.setText(sn.getContent());
		}else{
			vh.content.setText("如题↑↑↑");
		}
//		vh.userheadpic.setText(sn.getTitle());
		convertView.setTag(SMALLNOTEID, sn.getId());
		convertView.setTag(SMALLNOTEUSERID, sn.getUserid());
		
		TextView content = (TextView)convertView.findViewById(R.id.smallnoteList_content);
		//Button replyBtn = (Button)convertView.findViewById(R.id.mallnoteList_replybtn);
		content.setTag(-1, sn.getUsername());
		content.setTag(-2,sn.getUserid());
		content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnReplyClickListener.onClick(v.getTag(-1)+"",v.getTag(-2)+"");
			}
			

		});
		return convertView;
	}
	
	public void append(List<SmallNote> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}
	
	public void setData(List<SmallNote> list){
		this.mList.clear();
		this.mList.addAll(list);
		Log.d(TAG, "==="+this.mList.size());
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
//		public ImageView userheadpic;
		public TextView username;
		public TextView userdep;
		public TextView title;
		public TextView time;
		public TextView content;
	}
	
	/**
	 * 小字报回复按钮点击接口
	 * @author Administrator
	 *
	 */
	public interface onReplyClickListener{
		/**
		 * 回复按钮点击
		 * @param username
		 * @param userid
		 */
		public void onClick(String username,String userid);
	}

}
