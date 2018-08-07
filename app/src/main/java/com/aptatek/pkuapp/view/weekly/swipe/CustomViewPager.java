package com.aptatek.pkuapp.view.weekly.swipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

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
