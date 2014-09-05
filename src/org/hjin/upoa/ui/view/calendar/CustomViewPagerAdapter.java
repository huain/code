/**
 * 
 */
package org.hjin.upoa.ui.view.calendar;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author huangjin
 * 2014-9-5
 */
public class CustomViewPagerAdapter<V extends View> extends PagerAdapter {
	
	private V[] mViews;
	
	public CustomViewPagerAdapter(V[] views) {
		super();
		this.mViews = views;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		
//		if (container.getChildCount() == mViews.length) {
//			container.removeView(mViews[position % mViews.length]);
//		}
		container.removeView(mViews[position % mViews.length]);
		container.addView(mViews[position % mViews.length], 0);

		return mViews[position % mViews.length];
	}
	
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		
	}
	
	public V[] getAllItems() {
		return mViews;
	}
	
	

}
