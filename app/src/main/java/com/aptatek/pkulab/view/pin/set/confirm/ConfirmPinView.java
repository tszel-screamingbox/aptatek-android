package com.aptatek.pkulab.view.pin.set.confirm;

import com.hannesdorfmann.mosby3.mvp.MvpView;


interface ConfirmPinView extends MvpView {

    void onConnectReaderShouldLoad();

    void onValidPinTyped();

    void onInvalidPinTyped();

    void onFingerprintActivityShouldLoad();
}
