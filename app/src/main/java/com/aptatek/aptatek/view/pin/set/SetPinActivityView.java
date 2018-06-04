package com.aptatek.aptatek.view.pin.set;


import com.hannesdorfmann.mosby3.mvp.MvpView;

interface SetPinActivityView extends MvpView {

    void onValidFingerPrintTyped();

    void onInvalidFingerPrintTyped();

    void onShowConfirmationTexts();

    void onMainActivityShouldLoad();

    void onResetPinActivityShouldLoad();
}