package org.hjin.upoa.ui.view;

import java.util.List;

import org.hjin.upoa.R;
import org.hjin.upoa.model.Info;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoListAdapter extends BaseAdapter {
	
	private final String TAG = "InfoListAdapter";
	
	private LayoutInflater mLayoutInflater;
	
	private List<Info> mList;
	
	private Context mContext;
	
	
	public InfoListAdapter(Context context,List<Info> list){
		mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
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
			convertView = mLayoutInflater.inflate(R.layout.activity_infolist_item, null);
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
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				final ViewHolder vhdialog = (ViewHolder)v.getTag();
				new AlertDialog.Builder(mContext).setTitle(vhdialog.username.getText())
				.setMessage(vhdialog.dep.getText())
				.setPositiveButton("拨打电话", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+vhdialog.tel.getText())); 
						mContext.startActivity(intent);
					}
				})
				.setNegativeButton("发送邮件", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_SENDTO);
						intent.setData(Uri.parse("mailto:"+vhdialog.email.getText())); 
						mContext.startActivity(intent);
					}
				})
				.show();
				return true;
			}
		});
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
