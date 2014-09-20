package com.example.mark.streamradio;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by User on 2014.08.03..
 */
class GestureListener extends GestureDetector.SimpleOnGestureListener {

    private String currentGestureDetected;

    @Override
    public boolean onSingleTapUp(MotionEvent ev) {
        currentGestureDetected = "";
        return true;
    }

    @Override
    public void onShowPress(MotionEvent ev) {
        currentGestureDetected = "";
    }

    @Override
    public void onLongPress(MotionEvent ev) {
        currentGestureDetected = "";
        RadioList.showDialog();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        currentGestureDetected = "";
        return true;
    }

    @Override
    public boolean onDown(MotionEvent ev) {
        currentGestureDetected = "";
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        currentGestureDetected = "";
        return true;
    }
}
