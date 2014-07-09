package org.hjin.upoa.ui;

import org.hjin.upoa.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class BaseFragment extends Fragment {
	
	private final String TAG = "BaseFragment";
	
	protected Activity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	protected void loadingAnimatorActive(View v){
		ImageView iv = (ImageView)v.findViewById(R.id.fragmentload_image);
        Animation LoadingAnimation_1 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.animator.loading);
        LinearInterpolator lin = new LinearInterpolator();
        LoadingAnimation_1.setInterpolator(lin);
        iv.startAnimation(LoadingAnimation_1);
	}
	
	
	

}
