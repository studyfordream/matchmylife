package com.spacemangames.biomatcher.view;


import android.view.MotionEvent;

public class TouchEventHandler {
    private static final float MIN_DISTANCE = 20f;

    private final Graph view;

    private float startX;

    private float dragX;

    private float offsetX;

    private boolean isDragging = true;

    public TouchEventHandler(Graph view) {
        this.view = view;
    }

    public boolean onTouch(MotionEvent event) {
        boolean result = false;
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            result = true;
        } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (isDragging) {
                offsetX += dragX;
                isDragging = false;
            } else {
                // GridPoint touchSquare = view.getSquareForPixels(event);
                // view.getController().onTouch(touchSquare);
            }
            result = true;
        } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            if (Math.abs(x - startX) > MIN_DISTANCE || isDragging) {
                isDragging = true;
                handleDrag(x);
            }
            result = true;
        }
        return result;
    }

    private void handleDrag(float x) {
        dragX = x - startX;
        view.invalidate();
    }

    public float getOffsetX() {
        if (isDragging) {
            return dragX + offsetX;
        } else {
            return offsetX;
        }
    }

    public void reset() {
        offsetX = 0;
    }
}
