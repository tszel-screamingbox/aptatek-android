package com.aptatek.aptatek.view.weekly;

import com.hannesdorfmann.mosby3.mvp.MvpView;

interface WeeklyResultActivityView extends MvpView {

    void onSubtitleChanged(final String subtitle);

    void onLastPageReached();

    void onFirstPageReached();

    void onLoadNextPage(final int page);
}
