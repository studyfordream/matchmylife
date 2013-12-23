package com.spacemangames.biomatcher.view;

import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class TouchEventHandler implements OnGestureListener {
    private final Graph view;

    public TouchEventHandler(Graph view) {
        this.view = view;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        view.fling(velocityX);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        view.scroll(distanceX);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
}
