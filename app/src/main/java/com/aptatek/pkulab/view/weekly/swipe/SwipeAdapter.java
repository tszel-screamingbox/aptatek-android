package com.aptatek.pkulab.view.weekly.swipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aptatek.pkulab.view.weekly.chart.WeeklyChartFragmentStarter;

import java.util.ArrayList;
import java.util.List;

public class SwipeAdapter extends FragmentStatePagerAdapter {

    private final List<Integer> weeks = new ArrayList<>();

    public SwipeAdapter(final FragmentManager fm, final List<Integer> validWeeks) {
        super(fm);

        weeks.addAll(validWeeks);
    }

    @Override
    public Fragment getItem(final int position) {
        return WeeklyChartFragmentStarter.newInstance(weeks.get(position));
    }

    @Override
    public int getCount() {
        return weeks.size();
    }

    public void setData(final List<Integer> validWeeks) {
        weeks.clear();
        weeks.addAll(validWeeks);
        notifyDataSetChanged();
    }

}
