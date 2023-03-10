package com.aptatek.pkulab.view.splash;

import androidx.annotation.StringRes;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface SplashActivityView extends MvpView {

    void onParentalGateShouldLoad();

    void onRequestPinActivityShouldLoad();

    void onSetPinActivityShouldLoad();

    void onConnectReaderShouldLoad();

    void showAlertDialog(@StringRes int title, @StringRes int message);
}
