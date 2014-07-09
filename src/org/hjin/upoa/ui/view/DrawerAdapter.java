package org.hjin.upoa.ui.view;

import org.hjin.upoa.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter<T> extends ArrayAdapter<T> {
	
	private int[] imgids;
	
	private LayoutInflater mLayoutInflater;
	
	public DrawerAdapter(Context context, int resource, T[] objects, int[] imgids){
		super(context, resource, objects);
		mLayoutInflater = LayoutInflater.from(context);
		this.imgids = imgids;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView != null){
			vh = (ViewHolder)convertView.getTag();
		}else{
			convertView = mLayoutInflater.inflate(R.layout.drawer_list_item, null);
			vh = new ViewHolder();
			vh.name = (TextView)convertView.findViewById(android.R.id.text1);
			vh.icon = (ImageView)convertView.findViewById(R.id.image1);
			convertView.setTag(vh);
		}
		vh.name.setText(this.getItem(position).toString());
		vh.icon.setImageResource(imgids[position]);
		
		return convertView;
	}
	
	private class ViewHolder{
		public ImageView icon;
		public TextView name;
	}
	

}
