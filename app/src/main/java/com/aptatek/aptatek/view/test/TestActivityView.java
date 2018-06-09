package com.aptatek.aptatek.view.test;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface TestActivityView extends MvpView {

    void setCircleCancelVisible(boolean visible);

    void setCancelBigVisible(boolean visible);

    void setNavigationButtonVisible(boolean visible);

    void setNavigationButtonText(@NonNull String buttonText);

    void showScreen(@NonNull TestScreens screen);

    void navigateBack();

    void navigateForward();

}
