package org.hjin.upoa.ui.view.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CalendarGridiew extends GridView {
	
    public CalendarGridiew(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 

    public CalendarGridiew(Context context) { 
        super(context); 
    } 

    public CalendarGridiew(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 

    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

        int expandSpec = MeasureSpec.makeMeasureSpec( 
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
}
