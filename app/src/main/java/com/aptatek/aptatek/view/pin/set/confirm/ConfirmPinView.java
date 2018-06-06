package com.aptatek.aptatek.view.pin.set.confirm;

import com.hannesdorfmann.mosby3.mvp.MvpView;


interface ConfirmPinView extends MvpView {

    void onMainActivityShouldLoad();

    void onPinSetFragmentShouldLoad();

    void onValidPinTyped();

    void onInvalidPinTyped();
}