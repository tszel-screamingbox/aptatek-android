package com.aptatek.pkuapp.view.test;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface TestActivityView extends MvpView {

    void setNextButtonEnabled(boolean enabled);

    void setNextButtonVisible(boolean visible);

    void setBottomBarVisible(boolean visible);

    void showScreen(@NonNull TestScreens screen);

    void showNextScreen();

    void showPreviousScreen();

}
