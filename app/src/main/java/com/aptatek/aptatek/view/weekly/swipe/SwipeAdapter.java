package com.aptatek.aptatek.view.weekly.swipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aptatek.aptatek.view.weekly.chart.WeeklyChartFragment;

public class SwipeAdapter extends FragmentStatePagerAdapter {

    private boolean isSwipeDisabled = false;
    private final int size;

    public SwipeAdapter(final FragmentManager fm, final int size) {
        super(fm);
        this.size = size;
    }

    @Override
    public Fragment getItem(final int position) {
        return new WeeklyChartFragment();
    }

    @Override
    public int getCount() {
        return size;
    }
}
