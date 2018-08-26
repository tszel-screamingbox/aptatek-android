package com.aptatek.pkuapp.view.pin.set.confirm;

import com.aptatek.pkuapp.data.PinCode;
import com.aptatek.pkuapp.device.DeviceHelper;
import com.aptatek.pkuapp.domain.interactor.auth.AuthInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


class ConfirmPinPresenter extends MvpBasePresenter<ConfirmPinView> {

    private final AuthInteractor authInteractor;
    private final DeviceHelper deviceHelper;

    private Disposable disposable;

    @Inject
    ConfirmPinPresenter(final AuthInteractor authInteractor,
                        final DeviceHelper deviceHelper) {
        this.authInteractor = authInteractor;
        this.deviceHelper = deviceHelper;
    }


    void verifyPin(final PinCode addedPin, final PinCode confirmationPin) {
        if (addedPin.equals(confirmationPin)) {
            ifViewAttached(ConfirmPinView::onValidPinTyped);
            disposable = authInteractor.setPinCode(confirmationPin)
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::navigateForward, Timber::e);
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

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
