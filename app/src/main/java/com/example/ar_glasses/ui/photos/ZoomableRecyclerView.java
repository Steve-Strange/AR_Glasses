package com.example.ar_glasses.ui.photos;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ZoomableRecyclerView extends RecyclerView {
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;
    private int minSpanCount = 2;
    private int maxSpanCount = 5;
    private GridLayoutManager gridLayoutManager;
    private boolean isScaling = false;

    public ZoomableRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public ZoomableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        gridLayoutManager = new GridLayoutManager(context, 3);
        setLayoutManager(gridLayoutManager);

        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                isScaling = true;
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 2.0f));
                int newSpanCount = Math.round(3 / scaleFactor);
                newSpanCount = Math.max(minSpanCount, Math.min(newSpanCount, maxSpanCount));
                if (newSpanCount != gridLayoutManager.getSpanCount()) {
                    gridLayoutManager.setSpanCount(newSpanCount);
                    requestLayout();
                    return true;
                }
                return false;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                isScaling = false;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        scaleGestureDetector.onTouchEvent(e);
        return isScaling || super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        scaleGestureDetector.onTouchEvent(e);
        if (isScaling) {
            return true;
        }
        return super.onTouchEvent(e);
    }

    public boolean isScaling() {
        return isScaling;
    }
}