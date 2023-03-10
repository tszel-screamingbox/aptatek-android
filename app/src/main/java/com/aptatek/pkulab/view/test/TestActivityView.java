package com.aptatek.pkulab.view.test;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface TestActivityView extends MvpView, TestActivityCommonView {

    void showScreen(@NonNull TestScreens screen);

    void showTurnReaderOn();

    void onBackPressed();

    void showBatteryAlert();

    TestScreens getPreviousScreen();
}
