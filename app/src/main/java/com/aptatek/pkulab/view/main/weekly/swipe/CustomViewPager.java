package com.aptatek.pkulab.view.main.weekly.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    private boolean disable = false;

    public CustomViewPager(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewPager(@NonNull final Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        return !disable && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        return !disable && super.onTouchEvent(event);
    }

    public void disableSwipe(final boolean disable) {
        this.disable = disable;
    }
}
