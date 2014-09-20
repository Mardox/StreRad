package com.example.mark.streamradio.CostumView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by User on 2014.07.01..
 */
public class UntouchableScrollView extends ScrollView {

    public UntouchableScrollView(Context context) {
        super(context);
    }

    public UntouchableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UntouchableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
