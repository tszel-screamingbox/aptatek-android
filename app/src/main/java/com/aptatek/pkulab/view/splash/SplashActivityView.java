package com.aptatek.pkulab.view.splash;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface SplashActivityView extends MvpView {

    void onParentalGateShouldLoad();

    void onRequestPinActivityShouldLoad();

    void onSetPinActivityShouldLoad();
}
