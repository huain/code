/**
 * 
 */
package org.hjin.upoa.ui.view.calendar;


import org.hjin.upoa.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author huangjin
 * 2014-9-2
 */
public class CalendarAdapter extends BaseAdapter {
	
	private MyDate[] mydates;
	
	private LayoutInflater mInflater;
	
	private Context mContext;
	
	private int mTodayPosition;
	
	public int getmTodayPosition() {
		return mTodayPosition;
	}

	public CalendarAdapter(Context context ,MyDate[] mydates){
		mContext = context;
		this.mydates = mydates;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setDate(MyDate[] dates){
		mydates = dates;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mydates.length;
	}

	@Override
	public Object getItem(int position) {
		return mydates[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		MyDate mydate = mydates[position];
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.calendar_gridview_item, parent, false);
			vh = new ViewHolder();
			vh.tv = (TextView)convertView.findViewById(R.id.calendar_grid_item_text);
			vh.iconDaily = (ImageView)convertView.findViewById(R.id.calendar_grid_item_icon_daily);
			vh.iconSign = (ImageView)convertView.findViewById(R.id.calendar_grid_item_icon_sign);
			convertView.setTag(vh);
			setStyle(mydate,convertView,vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
			setStyle(mydate,convertView,vh);
		}
		if(mydate.isToday() == 0){
			mTodayPosition = position;
		}
		convertView.setTag(R.id.tag_first, mydate);
		vh.tv.setText(mydate.getmDay()+"");
		return convertView;
	}
	
	class ViewHolder{
		TextView tv;
		ImageView iconDaily;
		ImageView iconSign;
	}
	
	public interface IDateOnClickListener{
		/**
		 * 日历item的点击事件
		 * @param mydate
		 */
		public void dateOnClick(MyDate mydate);
		
		/**
		 * 初始化日历下方的当日详细信息
		 * @param mydate
		 */
//		public void initDataInfo(MyDate mydate);
	}
	
	
	
	
	private void setStyle(MyDate mydate,View convertView,ViewHolder vh){
		convertView.setBackgroundColor(mContext.getResources().getColor(R.color.txt_white));
		switch(mydate.getStatus()){
		case LastMonthDay:;
		case NextMonthDay:{
			convertView.setEnabled(false);
			vh.tv.setEnabled(false);
			vh.tv.setTextColor(mContext.getResources().getColor(R.color.ultra_gray4));
		}break;
		case CurrentMonthDay:{
			if(mydate.isToday() > 0){
				convertView.setEnabled(false);
				vh.tv.setEnabled(false);
				vh.tv.setTextColor(mContext.getResources().getColor(R.color.ultra_gray4));
			}else{
				convertView.setEnabled(true);
				vh.tv.setEnabled(true);
				vh.tv.setTextColor(mContext.getResources().getColor(R.color.ultra_black));
			}
			
		}break;
		}
		
		if(mydate.getDailyStatus() != null){
//			vh.iconDaily.setVisibility(View.VISIBLE);
//			Log.d("ada", "===="+timeTag+":"+mydate.getmDay()+"=="+mydate.getStatus()+"===Daily:"+mydate.getDailyStatus());
			switch (mydate.getDailyStatus()) {
			case Yes:{
				vh.iconDaily.setImageResource(R.drawable.calendar_grid_item_status_green);
			}break;
			case No:{
				vh.iconDaily.setImageResource(R.drawable.calendar_grid_item_status_red);
			}break;
			default:
				break;
			}
		}else{
			vh.iconDaily.setImageResource(R.drawable.calendar_grid_item_status_gray);
//			vh.iconDaily.setVisibility(View.GONE);
		}
		if(mydate.getSignStatus() != null){
//			vh.iconSign.setVisibility(View.VISIBLE);
//			Log.d("ada",  "===="+timeTag+":"+mydate.getmDay()+"=="+mydate.getStatus()+"===Sign:"+mydate.getSignStatus());
			switch (mydate.getSignStatus()) {
			case Normal:{
				vh.iconSign.setImageResource(R.drawable.calendar_grid_item_status_green);
			}break;
			case AbNormal:{
				vh.iconSign.setImageResource(R.drawable.calendar_grid_item_status_red);
			}break;
			default:
				break;
			}
		}else{
			vh.iconSign.setImageResource(R.drawable.calendar_grid_item_status_gray);
//			vh.iconSign.setVisibility(View.GONE);
		}
	}

	
	

}
