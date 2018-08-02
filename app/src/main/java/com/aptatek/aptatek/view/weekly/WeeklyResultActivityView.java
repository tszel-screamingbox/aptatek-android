package com.aptatek.aptatek.view.weekly;

import com.hannesdorfmann.mosby3.mvp.MvpView;

interface WeeklyResultActivityView extends MvpView {

    void onSubtitleChanged(final String subtitle);

    void onUpdateRightArrow(final boolean isVisible);

    void onUpdateLeftArrow(final boolean isVisible);

    void onLoadNextPage(final int page);

    void displayUnitLabel(String unitLabel);
}
