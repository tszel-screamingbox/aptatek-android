package com.aptatek.aptatek.view.weekly;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

interface WeeklyResultActivityView extends MvpView {

    void onSubtitleChanged(final String subtitle);

    void onUpdateRightArrow(final boolean isVisible);

    void onUpdateLeftArrow(final boolean isVisible);

    void onLoadNextPage(final int page);

    void displayUnitLabel(String unitLabel);

    void displayValidWeekList(List<Integer> validWeeks);
}
