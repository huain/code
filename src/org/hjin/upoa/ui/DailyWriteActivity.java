package org.hjin.upoa.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.DailyBusi;
import org.hjin.upoa.model.Daily;
import org.hjin.upoa.ui.view.DateTimePickerDialogFragment;
import org.hjin.upoa.ui.view.ProgressDialogFragment;
import org.hjin.upoa.ui.view.SelectionDialogFragment;
import org.hjin.upoa.util.Utility;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class DailyWriteActivity extends BaseActivity {
	
	private final String TAG = "DailyFragment"; 
	
//	private Activity mActivity;
	
	private TextView mProCode;
	
	private TextView mProName;
	
	private TextView mBegintime;
	
	private TextView mEndtime;
	
	private TextView mType;
	
	private TextView mPosition;
	
	private EditText mTitle;
	
	private EditText mDesc;
	
	private DailyBusi mDailyBusi;
	
	private Daily mDaily;
	
	/**获取项目列表期间的loading*/
	private DialogFragment mPdf;
	
	private Handler mHandler = new BaseHandler(this){
		@Override
		public void handleMessage(Message msg) {
			try {
				super.handleMessage(msg);
				switch(msg.what){
				case DailyBusi.GETPROLIST:{
					//显示消息
					Bundle data = msg.getData();
					if(null != data){
						List<Map<String,String>> proList = (List<Map<String,String>>)data.getSerializable("listContent");
						onClickPro(proList);
					}
				}break;
				case DailyBusi.POSTDAILY:{
					//显示消息
					Bundle data = msg.getData();
					if(null != data && !Utility.isBlank(data.getString("message"))){
						String message = data.getString("message");
						Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
						finish();
						//mActivity.getFragmentManager().beginTransaction().replace(containerViewId, fragment)
//						FragmentManager fragmentManager = getFragmentManager();
//				    	FragmentTransaction ft = fragmentManager.beginTransaction();
//				    	ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right); 
//				    	Fragment fragment = new DailyFragment();
//						ft.replace(R.id.content_frame, fragment).commit();
					}
				}break;
				default:break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_daily);
		mDailyBusi = new DailyBusi(mHandler);
		
		getActionBar().setTitle("写日报");
		
		// 取得intent中设置的默认日期
		Intent intent = getIntent();
		int[] date = intent.getIntArrayExtra("date");
		
		mProCode = (TextView)findViewById(R.id.newdaily_pro_code);
		mProName = (TextView)findViewById(R.id.newdaily_pro_name);
		mBegintime = (TextView)findViewById(R.id.newdaily_begintime);
		mEndtime = (TextView)findViewById(R.id.newdaily_endtime);
		mType = (TextView)findViewById(R.id.newdaily_type);
		mPosition = (TextView)findViewById(R.id.newdaily_position);
		mTitle = (EditText)findViewById(R.id.newdaily_title);
		mDesc = (EditText)findViewById(R.id.newdaily_desc);
		mDaily = new Daily();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.SIMPLIFIED_CHINESE);
		if(intent != null && date!= null && date.length == 3){
			@SuppressWarnings("deprecation")
			Date d = new Date(date[0]-1900,date[1]-1,date[2]);
			Log.d(TAG,"==="+date[0]+"-"+date[1]+"-"+date[2]);
			mBegintime.setText(sdf.format(d)+" 09:00");
			mEndtime.setText(sdf.format(d)+" 18:00");
		}else{
			final boolean begintimeSetting = sp.getBoolean("setting_item_begintimedefault", false);
			final boolean endtimeSetting = sp.getBoolean("setting_item_endtimedefault", false);
			Date d = Calendar.getInstance().getTime();
			if(begintimeSetting){
				mBegintime.setText(sdf.format(d)+" 09:00");
			}
			if(endtimeSetting){
				mEndtime.setText(sdf.format(d)+" 18:00");
			}
		}
		
		String typeSetting = sp.getString("setting_item_typedefault", "");
		String positionSetting = sp.getString("setting_item_positiondefault", "");
		
		mType.setText(typeSetting);
		mPosition.setText(positionSetting);
		
		// 项目列表选择框弹出设置
		View proView = (View)findViewById(R.id.newdaily_pro_area);
		proView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//onClickPro(null);
				mPdf = ProgressDialogFragment.newInstance("正在加载项目列表……");
			    FragmentTransaction ft = getFragmentManager().beginTransaction();
			    Fragment prev = getFragmentManager().findFragmentByTag("proloading");
			    if (prev != null) {
			    	ft.remove(prev);
			    }
			    ft.addToBackStack(null);
			    mPdf.setCancelable(false);
			    mPdf.show(ft, "proloading");
				mDailyBusi.getProList();
			}
		});
		
		// 开始结束时间弹出设置
		View begintimeView = (View)findViewById(R.id.newdaily_begintime_area);
		View endtimeView = (View)findViewById(R.id.newdaily_endtime_area);
		Calendar maxTime_temp = Calendar.getInstance();
		maxTime_temp.set(Calendar.HOUR_OF_DAY, 23);
		maxTime_temp.set(Calendar.MINUTE, 59);
		Calendar minTime_temp = Utility.getMonday(Calendar.getInstance());
		minTime_temp.set(Calendar.HOUR_OF_DAY, 0);
		minTime_temp.set(Calendar.MINUTE, 1);
		
		final Calendar maxTime = maxTime_temp;
		final Calendar minTime = minTime_temp;
		
		
		
		Log.d(TAG, "===max:"+maxTime.toString());
		Log.d(TAG, "===min:"+minTime.toString());
		begintimeView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar defaultTime = Calendar.getInstance();
				defaultTime.set(Calendar.HOUR_OF_DAY, 9);
				defaultTime.set(Calendar.MINUTE, 0);
				onClickDateTime("请选择开始时间",defaultTime,maxTime,minTime,(TextView)v.findViewById(R.id.newdaily_begintime));
			}
		});
		
		endtimeView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar defaultTime = Calendar.getInstance();
				defaultTime.set(Calendar.HOUR_OF_DAY, 18);
				defaultTime.set(Calendar.MINUTE, 0);
				onClickDateTime("请选择结束时间",defaultTime,maxTime,minTime,(TextView)v.findViewById(R.id.newdaily_endtime));
			}
		});
		
		// 项目类型和工作地点弹出设置
		View typeView = (View)findViewById(R.id.newdaily_type_area);
		View positionView = (View)findViewById(R.id.newdaily_position_area);
		
		typeView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickType("请选择任务类型",(TextView)v.findViewById(R.id.newdaily_type));
			}
		});
		
		positionView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickPosition("请选择工作地点",(TextView)v.findViewById(R.id.newdaily_position));
			}
		});
		
		final TextView descTip =  (TextView)findViewById(R.id.newdaily_desc_tip);
		mDesc.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				descTip.setText((1200 - s.length()) +"/1200");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		// button事件
		Button nextBtn = (Button)findViewById(R.id.newdaily_next_btn);
		final View firststep = findViewById(R.id.newdaily_firststep);
		final View second = findViewById(R.id.newdaily_secondstep);
		nextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDaily.setCode(mProCode.getText().toString());
				mDaily.setName(mProName.getText().toString());
				mDaily.setBegintime(mBegintime.getText().toString());
				mDaily.setEndtime(mEndtime.getText().toString());
				mDaily.setType(mType.getText().toString());
				mDaily.setPosition(mPosition.getText().toString());
				if(!mDailyBusi.validate1(mDaily)){
					return;
				}
				mTitle.setText(mBegintime.getText().toString().substring(0, 10)+" 工作日志");
				firststep.setVisibility(View.GONE);
				second.setVisibility(View.VISIBLE);
			}
		});
		
		Button backBtn = (Button)findViewById(R.id.newdaily_back_btn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				second.setVisibility(View.GONE);
				firststep.setVisibility(View.VISIBLE);
			}
		});
		
		Button postBtn = (Button)findViewById(R.id.newdaily_post_btn);
		postBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDaily.setSubject(mTitle.getText().toString());
				mDaily.setDesc(mDesc.getText().toString());
				if(mDailyBusi.validate2(mDaily)){
					mDailyBusi.checkCreateDate(mDaily);
				}
			}
		});
		
	}
	

	
	
	
	


	// 项目选择框弹出
	/**
	 * 项目列表选择框弹出
	 * @param data data中map的key分别为“code”，“name”
	 */
	public void onClickPro(List<Map<String,String>> data) {
		// data 中的map的key分别为“code”，“name”
		TextView[] tvs = new TextView[]{mProCode,mProName};
		int[] totvs = new int[]{0,1};
		SelectionDialogFragment df = SelectionDialogFragment.newInstance("请选择项目",data,R.layout.selection_list_item_2,new String[]{"code","name"},new int[]{R.id.selection_item2_text1,R.id.selection_item2_text2},tvs,totvs);
	    FragmentTransaction ft = this.getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("datetimepicker");
//	    Fragment prev2 = getFragmentManager().findFragmentByTag("proloading");
	    if (prev != null) {
	    	ft.remove(prev);
	    }
	    if(mPdf != null){
	    	mPdf.dismiss();
	    }
//	    if(prev2 != null){
//	    	mPdf.dismiss();
////	    	DialogFragment pdf = (DialogFragment)prev2;
////	    	pdf.dismiss();
//	    	ft.remove(prev2);
//	    }
	    ft.addToBackStack(null);
	    df.setCancelable(false);
	    df.show(ft, "selection");
//		
	}
	
	// datetime选择框弹出
	public void onClickDateTime(String title,Calendar defaultTime,Calendar maxTime,Calendar minTime,TextView v) {
		DateTimePickerDialogFragment df = DateTimePickerDialogFragment.newInstance(title,defaultTime,maxTime,minTime,v);
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("datetimepicker");
	    if (prev != null) {
	    	ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    df.setCancelable(false);
	    df.show(ft, "datetimepicker");
	}
	
	// type选择框弹出
	public void onClickType(String title,TextView v) {
		List<Map<String,String>> data = new ArrayList<Map<String,String>>();
		String[] types = getResources().getStringArray(R.array.daily_type_array);
		if(null != types && types.length>0){
			for(String text:types){
				Map<String,String> a = new HashMap<String,String>();
				a.put("text1", text);
				data.add(a);
			}
		}
		SelectionDialogFragment df = SelectionDialogFragment.newInstance(title,data,R.layout.selection_list_item_1,new String[]{"text1"},new int[]{R.id.selection_item1_text},v);
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("datetimepicker");
	    if (prev != null) {
	    	ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    df.setCancelable(false);
	    df.show(ft, "selection");
	}
	
	// position选择框弹出
	public void onClickPosition(String title,TextView v) {
		List<Map<String,String>> data = new ArrayList<Map<String,String>>();
		
		String[] positions = getResources().getStringArray(R.array.daily_position_array);
		if(null != positions && positions.length>0){
			for(String text:positions){
				Map<String,String> a = new HashMap<String,String>();
				a.put("text1", text);
				data.add(a);
			}
		}
		
		SelectionDialogFragment df = SelectionDialogFragment.newInstance(title,data,R.layout.selection_list_item_1,new String[]{"text1"},new int[]{R.id.selection_item1_text},v);
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("datetimepicker");
	    if (prev != null) {
	    	ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    df.setCancelable(false);
	    df.show(ft, "selection");
//		
	}
	
	
}
