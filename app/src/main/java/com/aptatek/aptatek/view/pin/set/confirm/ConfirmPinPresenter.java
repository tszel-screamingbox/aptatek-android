package com.aptatek.aptatek.view.pin.set.confirm;

import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.device.DeviceHelper;
import com.aptatek.aptatek.domain.interactor.auth.AuthInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;


class ConfirmPinPresenter extends MvpBasePresenter<ConfirmPinView> {

    private static final int TIMER_PERIOD_IN_SEC = 1;

    private final AuthInteractor authInteractor;
    private final DeviceHelper deviceHelper;

    @Inject
    ConfirmPinPresenter(final AuthInteractor authInteractor,
                        final DeviceHelper deviceHelper) {
        this.authInteractor = authInteractor;
        this.deviceHelper = deviceHelper;
    }


    void verifyPin(final PinCode addedPin, final PinCode confirmationPin) {
        if (addedPin.equals(confirmationPin)) {
            authInteractor.setPinCode(confirmationPin);
            ifViewAttached(ConfirmPinView::onValidPinTyped);
        } else {
            ifViewAttached(ConfirmPinView::onInvalidPinTyped);
        }
    }

    void navigateForward() {
        if (deviceHelper.hasFingerprintHadrware() && deviceHelper.hasEnrolledFingerprints()) {
            ifViewAttached(ConfirmPinView::onFingerprintActivityShouldLoad);
        } else {
            ifViewAttached(ConfirmPinView::onMainActivityShouldLoad);
        }
    }
}
