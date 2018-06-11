package com.aptatek.aptatek.view.pin.request.add;

import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.device.DeviceHelper;
import com.aptatek.aptatek.domain.interactor.auth.AuthInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;


class RequestPinPresenter extends MvpBasePresenter<RequestPinView> {

    private final AuthInteractor authInteractor;
    private final DeviceHelper deviceHelper;

    @Inject
    RequestPinPresenter(final AuthInteractor authInteractor,
                        final DeviceHelper deviceHelper) {
        this.authInteractor = authInteractor;
        this.deviceHelper = deviceHelper;
    }

    void initView() {
        if (deviceHelper.isFingperprintAuthAvailable()) {
            ifViewAttached(RequestPinView::onFingerprintEnable);
        } else {
            ifViewAttached(RequestPinView::onFingerprintDisabled);
        }
    }

    void verifyPinCode(final PinCode pinCode) {
        if (authInteractor.isValidPinCode(pinCode)) {
            ifViewAttached(RequestPinView::onMainActivityShouldLoad);
        } else {
            ifViewAttached(RequestPinView::onInvalidPinTyped);
        }
    }
}
