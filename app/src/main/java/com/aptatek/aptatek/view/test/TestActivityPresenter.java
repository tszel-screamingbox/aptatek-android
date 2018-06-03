package com.aptatek.aptatek.view.test;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class TestActivityPresenter extends MvpBasePresenter<TestActivityView> {

    @Inject
    TestActivityPresenter() {
    }

    @Override
    public void attachView(final @NonNull TestActivityView view) {
        super.attachView(view);

        // TODO check if test is running and display proper screen
    }
}
