package org.hjin.upoa.ui.view;

import java.util.List;
import java.util.Map;

import org.hjin.upoa.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SelectionDialogFragment extends DialogFragment {

	private final String TAG = "SelectionDialogFragment";
	
	private String mTitle;
	
	private List<Map<String,String>> mList;
	
	private TextView mTv;
	
	private ListView mLv;
	
	private String[] mFrom;
	
	private int[] mTo;
	
	private int mRes;
	/** 回调中设置的数据来源，mFrom中的第几个key对应的值 */
	private int[] mTotvs;
	/** 回调中被设置数据的TextView控件 */
	private TextView[] mTvs;

	private SelectionDialogFragment mDialog;
	
	private Activity mActivity;

	public static SelectionDialogFragment newInstance(String title,List<Map<String,String>> data,int res,String[] from,int[] to,TextView v) {
		SelectionDialogFragment f = new SelectionDialogFragment();
		f.setStyle(STYLE_NO_TITLE, 0);
		Bundle args = new Bundle();
		args.putString("title", title);
		f.setArguments(args);
		f.mDialog = f;
		f.mList = data;
		f.mTv = v;
		f.mRes = res;
		f.mFrom = from;
		f.mTo = to;
		return f;
	}
	
	public static SelectionDialogFragment newInstance(String title,List<Map<String,String>> data,int res,String[] from,int[] to,TextView[] tvs,int[] totvs) {
		SelectionDialogFragment f = new SelectionDialogFragment();
		f.setStyle(STYLE_NO_TITLE, 0);
		Bundle args = new Bundle();
		args.putString("title", title);
		f.setArguments(args);
		f.mDialog = f;
		f.mList = data;
		f.mTvs = tvs;
		f.mTotvs = totvs;
		f.mRes = res;
		f.mFrom = from;
		f.mTo = to;
		return f;
	}
	
	

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		mActivity = activity;
		super.onAttach(activity);
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_selection_dialog,
				container, false);
		
		mLv = (ListView)v.findViewById(R.id.selection_list);
		Log.d(TAG, "===mList:"+mList.size());
		
		TextView mTitleView = (TextView) v.findViewById(R.id.selection_title);
		mTitle = getArguments().getString("title");
		mTitleView.setText(mTitle);
		Button backBtn = (Button) v.findViewById(R.id.selection_back);
//		Button submitBtn = (Button) v.findViewById(R.id.selection_submit);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

//		submitBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mDialog.dismiss();
//			}
//		});
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Window window = getDialog().getWindow();
		window.setBackgroundDrawable(null);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	public void doBack(View v) {

	}

	public void doClear(View v) {

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//String[] from
		super.onActivityCreated(savedInstanceState);
		ListAdapter aa = new SimpleAdapter(mActivity.getApplicationContext(), mList, mRes, mFrom, mTo);
		mLv.setAdapter(aa);
		mLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mTv != null){
					mTv.setText(mList.get(position).get(mFrom[0]));
					Log.d(TAG, "===select:" + mList.get(position).get(mFrom[0]));
				}else if(mTvs != null && mTotvs != null && mTvs.length>0 && mTvs.length == mTotvs.length){
					for(int i=0;i<mTvs.length;i++){
						TextView tv = mTvs[i];
						tv.setText(mList.get(position).get(mFrom[mTotvs[i]]));
					}
				}
				mDialog.dismiss();
			}
			
		});
//		mLv.(new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				mTv.setText(mList.get(position).get(mFrom[0]));
//				Log.d(TAG, "===select:" + mList.get(position).get(mFrom[0]));
//			}
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				
//			}
//			
//		});

	}

}
