package com.aptatek.pkulab.view.pin.set;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface SetPinHostActivityView extends MvpView {

    void onValidPinTyped();

    void onInvalidPinTyped();

}
