package org.hjin.upoa.ui.view;

import org.hjin.upoa.R;

import android.app.DialogFragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ValidateDialogFragment extends DialogFragment {
	
	private final String TAG = "ValidateDialogFragment";
	
	/**开始验证：显示验证码*/
	public static final int START_VALIDATE = 0;
	/**结束输入：输入完验证码*/
	public static final int END_INPUT = 1;
	/**结束验证：验证完毕，关闭本dialog*/
	public static final int END_VALIDATE = 2;
	
	private LinearLayout mProgressLayout;
	
	private LinearLayout mContentLayout;
	
	public Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case START_VALIDATE:{
				Log.v(TAG, "===START_VALIDATE");
				mProgressLayout.setVisibility(View.GONE);
				mContentLayout.setVisibility(View.VISIBLE);
				Bundle data = msg.getData();
				if(data.getByteArray("validateCodePic") != null){
					Log.v(TAG, "===START_VALIDATE");
					ImageButton ib = (ImageButton)mContentLayout.findViewById(R.id.validateDialogFragment_codepic);
					byte[] picContent = data.getByteArray("validateCodePic");
					ib.setImageBitmap(BitmapFactory.decodeByteArray(picContent,0,picContent.length));
				}
				
				final EditText et = (EditText)mContentLayout.findViewById(R.id.validateDialogFragment_codeinput);
				Button btnPositive = (Button)mContentLayout.findViewById(R.id.validateDialogFragment_positivebtn);
				btnPositive.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Bundle data = new Bundle();
						data.putCharSequence("valiedateCode", et.getText().toString());
						((IValidateDialogFragment)getActivity()).doPositiveClick(data);
					}
				});
				
				Button btnNegative = (Button)mContentLayout.findViewById(R.id.validateDialogFragment_negativebtn);
				btnNegative.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Bundle data = new Bundle();
						data.putCharSequence("valiedateCode", et.getText().toString());
						((IValidateDialogFragment)getActivity()).doNegativeClick(data);
					}
				});
			}break;
			case END_INPUT:{
				Log.v(TAG, "===END_INPUT");
				TextView tip_tv = (TextView)mProgressLayout.findViewById(R.id.validateDialogFragment_tip);
				tip_tv.setText("正在验证...");
				mContentLayout.setVisibility(View.GONE);
				mProgressLayout.setVisibility(View.VISIBLE);
			}break;
			case END_VALIDATE:{
			}break;
			default:;
			}
		}
		
	};

    /**
     * Create a new instance of ValidateDialogFragment, providing "validateCodePic"
     * as an argument.
     */
    public static ValidateDialogFragment newInstance() {
    	ValidateDialogFragment f = new ValidateDialogFragment();
    	f.setStyle(STYLE_NO_TITLE,0);
        return f;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_validate_dialog, container, false);
		mProgressLayout = (LinearLayout)v.findViewById(R.id.validateDialogFragment_progress);
		mContentLayout = (LinearLayout)v.findViewById(R.id.validateDialogFragment_content);
		ImageView iv = (ImageView)v.findViewById(R.id.validateDialogFragment_image);
        Animation LoadingAnimation_1 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.animator.loading);
        LinearInterpolator lin = new LinearInterpolator();
        LoadingAnimation_1.setInterpolator(lin);
        iv.startAnimation(LoadingAnimation_1);
		TextView tip_tv = (TextView)mProgressLayout.findViewById(R.id.validateDialogFragment_tip);
		tip_tv.setText("正在获取验证码...");
		return v;
	}
	
	
	public interface IValidateDialogFragment{
		public void doPositiveClick(Bundle data);
		
		public void doNegativeClick(Bundle data);
		
	}
    
}
