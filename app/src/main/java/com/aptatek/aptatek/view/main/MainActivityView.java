package com.aptatek.aptatek.view.main;

import com.hannesdorfmann.mosby3.mvp.MvpView;

interface MainActivityView extends MvpView {

    void updateTitles(final String title, final String subTitle);
}
