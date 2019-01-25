package com.aptatek.pkulab.view.pin.auth;

import com.aptatek.pkulab.device.DeviceHelper;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class AuthPinHostActivityPresenter extends MvpBasePresenter<AuthPinHostActivityView> {

    private final DeviceHelper deviceHelper;

    @Inject
    AuthPinHostActivityPresenter(final DeviceHelper deviceHelper) {
        this.deviceHelper = deviceHelper;
    }

    void initView() {
        if (deviceHelper.isFingperprintAuthAvailable()) {
            ifViewAttached(AuthPinHostActivityView::onFingerpintAuthShouldLoad);
        } else {
            ifViewAttached(AuthPinHostActivityView::onAuthPinFragmentShouldLoad);
        }
    }
}
