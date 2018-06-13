package com.aptatek.aptatek.view.pin.request.add;

import com.hannesdorfmann.mosby3.mvp.MvpView;


interface RequestPinView extends MvpView {

    void onMainActivityShouldLoad();

    void onInvalidPinTyped();
}
