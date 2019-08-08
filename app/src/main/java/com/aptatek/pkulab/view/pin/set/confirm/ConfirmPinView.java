package com.aptatek.pkulab.view.pin.set.confirm;

import com.aptatek.pkulab.view.pin.set.SetPinHostActivityView;
import com.hannesdorfmann.mosby3.mvp.MvpView;


interface ConfirmPinView extends MvpView, SetPinHostActivityView {

    void onConnectReaderShouldLoad();

    void onFingerprintActivityShouldLoad();
}
