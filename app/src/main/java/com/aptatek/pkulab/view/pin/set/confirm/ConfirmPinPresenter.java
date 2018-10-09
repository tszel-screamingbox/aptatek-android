package com.aptatek.pkulab.view.pin.set.confirm;

import com.aptatek.pkulab.data.PinCode;
import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.interactor.auth.AuthInteractor;
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
            ifViewAttached(ConfirmPinView::onConnectReaderShouldLoad);
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
