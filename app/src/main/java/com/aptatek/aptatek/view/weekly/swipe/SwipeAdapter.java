package com.aptatek.aptatek.view.weekly.swipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aptatek.aptatek.view.weekly.chart.WeeklyChartFragmentStarter;

import java.util.List;

public class SwipeAdapter extends FragmentStatePagerAdapter {

    private final List<Integer> weeks;

    public SwipeAdapter(final FragmentManager fm, final List<Integer> validWeeks) {
        super(fm);
        this.weeks = validWeeks;
    }

    @Override
    public Fragment getItem(final int position) {
        return WeeklyChartFragmentStarter.newInstance(weeks.get(position));
    }

    @Override
    public int getCount() {
        return weeks.size();
    }
}