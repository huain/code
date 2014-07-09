package org.hjin.upoa.ui.view;

import org.hjin.upoa.R;
import org.hjin.upoa.busi.SmallNoteBusi;
import org.hjin.upoa.util.Utility;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SmallNoteReplyDialogFragment extends DialogFragment {
	
	private final String TAG = "SmallNoteReplyDialogFragment";
	
	/**开始提交*/
	public static final int START_REPLY = 0;
	/**提交完成*/
	public static final int END_REPLY = 1;
	
	public static final int SAVESMALLNOTE = 3;
	
	private LinearLayout mProgressLayout;
	
	private LinearLayout mContentLayout;
	/** 被回复用户用户名*/
	private String mUsername;
	/** 被回复用户id*/
	private String mUserid;
	
	private SmallNoteBusi mSmallNoteBusi;
	
	private SmallNoteReplyDialogFragment mDialog;
	
	public Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case SAVESMALLNOTE:{
				Log.d(TAG, "===SAVESMALLNOTE");
				if(msg.arg1 == 1){
					mDialog.dismiss();
				}else{
					TextView content_error = (TextView)mContentLayout.findViewById(R.id.smallnote_reply_content_error);
					content_error.setVisibility(View.VISIBLE);
					content_error.setText("发送失败，请重新发送！");
				}
			}break;
			default:;
			}
		}
		
	};
	
	

    /**
     * Create a new instance of ValidateDialogFragment, providing "validateCodePic"
     * as an argument.
     */
    public static SmallNoteReplyDialogFragment newInstance(String username,String userid) {
    	SmallNoteReplyDialogFragment f = new SmallNoteReplyDialogFragment();
    	f.setStyle(STYLE_NO_TITLE,0);
    	Bundle args = new Bundle();
    	args.putString("username", username);
    	args.putString("userid", userid);
    	f.setArguments(args);
    	f.mDialog = f;
        return f;
    }
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_smallnotereply_dialog, container, false);
		mProgressLayout = (LinearLayout)v.findViewById(R.id.smallnote_reply_progress);
		mContentLayout = (LinearLayout)v.findViewById(R.id.smallnote_reply_content_area);
		mUsername = getArguments().getString("username");
		mUserid = getArguments().getString("userid");
		if(!Utility.isBlank(mUsername) && !Utility.isBlank(mUserid)){
			EditText title = (EditText)mContentLayout.findViewById(R.id.smallnote_reply_title);
			title.setText("@" + mUsername +"(" + mUserid +")");
			title.setEnabled(false);
		}
		
		((Button)v.findViewById(R.id.smallnote_reply_reply)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = ((EditText)mContentLayout.findViewById(R.id.smallnote_reply_title)).getText().toString();
				String content = ((EditText)mContentLayout.findViewById(R.id.smallnote_reply_content)).getText().toString();
				
				if(Utility.isBlank(title) || Utility.isBlank(content)){
					if(Utility.isBlank(title)){
						TextView title_error = (TextView)mContentLayout.findViewById(R.id.smallnote_reply_title_error);
						title_error.setVisibility(View.VISIBLE);
						title_error.setText("请填写标题！");
					}
					if(Utility.isBlank(content)){
						TextView content_error = (TextView)mContentLayout.findViewById(R.id.smallnote_reply_content_error);
						content_error.setVisibility(View.VISIBLE);
						content_error.setText("请填写内容！");
					}
					return;
				}
				
				mContentLayout.setVisibility(View.GONE);
				mProgressLayout.setVisibility(View.VISIBLE);
				ImageView iv = (ImageView)mProgressLayout.findViewById(R.id.dialogFragment_image);
		        Animation LoadingAnimation_1 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.animator.loading);
		        LinearInterpolator lin = new LinearInterpolator();
		        LoadingAnimation_1.setInterpolator(lin);
		        iv.startAnimation(LoadingAnimation_1);
				TextView tip_tv = (TextView)mProgressLayout.findViewById(R.id.dialogFragment_tip);
				tip_tv.setText("正在提交回复内容...");
				
				mSmallNoteBusi = new SmallNoteBusi(mHandler);
				mSmallNoteBusi.saveSmallNote(title, content);
			}
		});
		
		((Button)v.findViewById(R.id.smallnote_reply_clear)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText title = (EditText)mContentLayout.findViewById(R.id.smallnote_reply_title);
				EditText content = (EditText)mContentLayout.findViewById(R.id.smallnote_reply_content);
				if(title.isEnabled()){
					title.setText("");
				}
				content.setText("");
			}
		});
		
		((Button)v.findViewById(R.id.smallnote_reply_back)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		
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




	public void doBack(View v){
		
	}
	
	public void doClear(View v){
		
	}
	
	public void doReply(View v){
		ImageView iv = (ImageView)mProgressLayout.findViewById(R.id.dialogFragment_image);
        Animation LoadingAnimation_1 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.animator.loading);
        LinearInterpolator lin = new LinearInterpolator();
        LoadingAnimation_1.setInterpolator(lin);
        iv.startAnimation(LoadingAnimation_1);
		TextView tip_tv = (TextView)mProgressLayout.findViewById(R.id.dialogFragment_tip);
		tip_tv.setText("正在提交回复内容...");
		
		
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	}
	
	
	
}
