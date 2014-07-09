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
import android.widget.EditText;
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
    	f.setStyle(STYLE_NO_TITLE,0);
        Bundle args = new Bundle();
        args.putString("tips", tips);
        f.setArguments(args);
        return f;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_progress_dialog_1, container, false);
		ImageView iv = (ImageView)v.findViewById(R.id.progressDialogFragment1_image);
        Animation LoadingAnimation_1 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.animator.loading);
        LinearInterpolator lin = new LinearInterpolator();
        LoadingAnimation_1.setInterpolator(lin);
        iv.startAnimation(LoadingAnimation_1);
		
		
		String tips = getArguments().getString("tips");
		if(!Utility.isBlank(tips)){
			TextView tv = (TextView)v.findViewById(R.id.progressDialogFragment1_title);
			tv.setText(tips);
		}
		return v;
	}
	
}
