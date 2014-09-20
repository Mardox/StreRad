package com.example.mark.streamradio.CostumView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by User on 2014.07.01..
 */
public class UntouchableHorizontalScrollView extends HorizontalScrollView {

    public UntouchableHorizontalScrollView(Context context) {
        super(context);
    }

    public UntouchableHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UntouchableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
