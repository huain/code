package org.hjin.upoa.ui.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hjin.upoa.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarView extends LinearLayout {

	 private static Context context;
	    
	    private Date theInDay;
	    private String inday="",outday="";
	    public static View viewIn;
	    public static View viewOut;
	    public static String positionIn;
	    public static String positionOut;
	    
	    
	    private  List<String> gvList ;//�����

	    private OnDaySelectListener callBack;//�ص�����
	    
		private static String nowday= new SimpleDateFormat("yyyy-MM-dd").format(new Date()) ;

	    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");//���ڸ�ʽ��
	    
	    private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");//���ڸ�ʽ��

	    /**
	     * ���캯��
	     * @param context
	     */
	    public CalendarView(Context context) {
	        super(context);
	        CalendarView.context = context;
	        setTheDay(new Date());
	    }

	    /**
	     * ���캯��
	     * @param context
	     */
	    public CalendarView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        CalendarView.context = context;
	        setTheDay(new Date());
	    }
	    
	    public void setInDay(String inday){
	    	this.inday=inday;
	    }
	    
	    public void setOutDay(String outday){
	    	this.outday=outday;
	    }
	    
	    public void setTheDay(Date dateIn){
	    	this.theInDay=dateIn;
	    	init();
	    }

	    /**
	     * ��ʼ�������Լ�view�ȿؼ�
	     */
	    private void init() {
	    	 gvList = new ArrayList<String>();//�����
	        final Calendar cal = Calendar.getInstance();//��ȡ����ʵ��
	        cal.setTime(theInDay);//cal����Ϊ�����
	        cal.set(Calendar.DATE, 1);//cal���õ�ǰdayΪ��ǰ�µ�һ��
	        int tempSum = countNeedHowMuchEmpety(cal);//��ȡ��ǰ�µ�һ��Ϊ���ڼ�
	        int dayNumInMonth = getDayNumInMonth(cal);//��ȡ��ǰ���ж�����
	        setGvListData(tempSum, dayNumInMonth,cal.get(Calendar.YEAR)+"-"+getMonth((cal.get(Calendar.MONTH)+1)));

	        View view = LayoutInflater.from(context).inflate(R.layout.calendar, this, true);//��ȡ���֣���ʼ��ʼ��
	        TextView tv_year= (TextView) view.findViewById(R.id.tv_year);
	        if(cal.get(Calendar.YEAR)>new Date().getYear()){
	        	tv_year.setVisibility(View.VISIBLE);
	        	tv_year.setText(cal.get(Calendar.YEAR)+"��");
	        }
	        TextView tv_month= (TextView) view.findViewById(R.id.tv_month);
	        tv_month.setText(String.valueOf(theInDay.getMonth()+1));
	        CalendarGridView gv = (CalendarGridView) view.findViewById(R.id.gv_calendar);
	        calendarGridViewAdapter gridViewAdapter = new calendarGridViewAdapter(gvList,inday,outday);
	        gv.setAdapter(gridViewAdapter);
	        gv.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> adapterView, View arg1,int position, long arg3) {
	                String choiceDay = (String) adapterView.getAdapter().getItem(position);
	                String[] date=choiceDay.split(",");
	                String day=date[1];
	                if (!" ".equals(day)) {
	                    if (Integer.parseInt(day) < 10) {
	                    	day = "0" + date[1];
	                    }
	                    choiceDay = date[0] +"-"+ day;
	                    if (callBack != null) {//���ûص������ص�����
	                        callBack.onDaySelectListener(arg1,choiceDay);
	                    }
	                }
	            }
	        });
	    }


	    /**
	     * Ϊgridview�������Ҫչʾ������
	     * @param tempSum
	     * @param dayNumInMonth
	     */
	    private void setGvListData(int tempSum, int dayNumInMonth,String YM) {
	        gvList.clear();
	        for (int i = 0; i < tempSum; i++) {
	            gvList.add(" , ");
	        }
	        for (int j = 1; j <= dayNumInMonth; j++) {
	            gvList.add(YM+","+String.valueOf(j));
	        }
	    }
	    
	    private String getMonth(int month) {
	    	String mon="";
	    	if(month<10){
	    		mon="0"+month;
	    	}else{
	    		mon=""+month;
	    	}
	    	return mon;
	    }

	    /**
	     * ��ȡ��ǰ�µ��ܹ�����
	     * @param cal
	     * @return
	     */
	    private int getDayNumInMonth(Calendar cal) {
	        return cal.getActualMaximum(Calendar.DATE);
	    }

	    /**
	     * ��ȡ��ǰ�µ�һ���ڵ�һ����ݵĵڼ��죬�ó���һ�������ڼ�
	     * @param cal
	     * @return
	     */
	    private int countNeedHowMuchEmpety(Calendar cal) {
	        int firstDayInWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
	        return firstDayInWeek;
	    }


	    /**
	     * gridview��adapter��viewholder
	     * @author Administrator
	     *
	     */
	    static class GrideViewHolder {
	        TextView tvDay;
	    }

	    /**
	     * gridview��adapter
	     * @author Administrator
	     *
	     */
	    static class calendarGridViewAdapter extends BaseAdapter {
	    	
	    	List<String> gvList ;//�����
	    	String inday,outday;
	    	
	    	public calendarGridViewAdapter(List<String> gvList,String inday,String outday){
	    		super();
	    		this.gvList=gvList;
	    		this.inday=inday;
	    		this.outday=outday;
	    	}

	        @Override
	        public int getCount() {
	            return gvList.size();
	        }

	        @Override
	        public String getItem(int position) {
	            return gvList.get(position);
	        }

	        @Override
	        public long getItemId(int position) {
	            return position;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            GrideViewHolder holder;
	            if (convertView == null) {
	                holder = new GrideViewHolder();
	                convertView = inflate(context,R.layout.calendar_gridview_item, null);
	                holder.tvDay = (TextView) convertView.findViewById(R.id.tv_calendar_day);
	                convertView.setTag(holder);
	            } else {
	                holder = (GrideViewHolder) convertView.getTag();
	            }
	            String[] date=getItem(position).split(",");
	            if((position+1)%7==0||(position)%7==0){
	            	holder.tvDay.setTextColor(Color.parseColor("#339900"));
	            }else{
	            	
	            }
	            if(!date[1].equals(" ")){
	            	 String day=date[1];
	                 if(Integer.parseInt(date[1])<10){
	                 	day="0"+date[1];
	                 }
	                 holder.tvDay.setText(day);
	                 if((date[0]+"-"+day).equals(nowday)){
	                 	holder.tvDay.setTextColor(Color.parseColor("#FF6600"));
	                 	holder.tvDay.setTextSize(15);
	                 	holder.tvDay.setText("����");
	                 }
	                 
	            }
	            
	            return convertView;
	        }
	    }

	    /**
	     * �Զ�������ӿ�
	     * @author Administrator
	     *
	     */
	    public interface OnDaySelectListener {
	        void onDaySelectListener(View view,String date);
	    }

	    /**
	     * �Զ�������ӿ����ö���
	     * @param o
	     */
	    public void setOnDaySelectListener(OnDaySelectListener o) {
	        callBack = o;
	    }
}
