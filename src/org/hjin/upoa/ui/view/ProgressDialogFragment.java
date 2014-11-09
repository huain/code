package org.hjin.upoa.ui.view;

import org.hjin.upoa.R;
import org.hjin.upoa.util.Utility;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Administrator
 *
 */
public class ProgressDialogFragment extends DialogFragment {
	
    /**
     * Create a new instance of ValidateDialogFragment, providing "validateCodePic"
     * as an argument.
     */
    public static ProgressDialogFragment newInstance(String tips) {
    	ProgressDialogFragment f = new ProgressDialogFragment();
    	f.setStyle(STYLE_NO_TITLE,R.style.MyDialogLoading);
        Bundle args = new Bundle();
        args.putString("tips", tips);
        f.setArguments(args);
        return f;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_progress_dialog_1, container, false);
		ImageView iv_blue = (ImageView)v.findViewById(R.id.loading1_image_blue);
		ImageView iv_green = (ImageView)v.findViewById(R.id.loading1_image_green);
		
        Animation LoadingAnimation_s2b = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.animator.circle_small2big_l);
        Animation LoadingAnimation_b2s = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.animator.circle_big2small_r);
        LinearInterpolator lin = new LinearInterpolator();
        LoadingAnimation_s2b.setInterpolator(lin);
        LoadingAnimation_b2s.setInterpolator(lin);
        iv_blue.startAnimation(LoadingAnimation_s2b);
        iv_green.startAnimation(LoadingAnimation_b2s);
		
		
		String tips = getArguments().getString("tips");
		if(!Utility.isBlank(tips)){
			TextView tv = (TextView)v.findViewById(R.id.progressDialogFragment1_title);
			tv.setText(tips);
		}
		return v;
	}
	
}
