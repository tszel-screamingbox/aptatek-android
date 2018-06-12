package com.aptatek.aptatek.view.pin.set.confirm;

import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.device.DeviceHelper;
import com.aptatek.aptatek.domain.interactor.auth.AuthInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


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
            setPinCode(confirmationPin);
        } else {
            differentPins();
        }
    }

    private void setPinCode(final PinCode pin) {
        Observable.empty().delay(TIMER_PERIOD_IN_SEC, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    authInteractor.setPinCode(pin);
                    navigateForward();
                    ifViewAttached(ConfirmPinView::onMainActivityShouldLoad);
                })
                .subscribe();
        ifViewAttached(ConfirmPinView::onValidPinTyped);
    }

    private void navigateForward() {
        if (deviceHelper.hasFingerprintHadrware() && deviceHelper.hasEnrolledFingerprints()) {
            ifViewAttached(ConfirmPinView::onFingerprintActivityShouldLoad);
        } else {
            ifViewAttached(ConfirmPinView::onMainActivityShouldLoad);
        }
    }

    private void differentPins() {
        Observable.empty().delay(TIMER_PERIOD_IN_SEC, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> ifViewAttached(ConfirmPinView::onPinSetFragmentShouldLoad))
                .subscribe();
        ifViewAttached(ConfirmPinView::onInvalidPinTyped);
    }
}