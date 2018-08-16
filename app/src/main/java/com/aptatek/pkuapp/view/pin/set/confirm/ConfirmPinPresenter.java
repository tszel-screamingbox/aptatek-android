package com.aptatek.pkuapp.view.pin.set.confirm;

import android.support.annotation.Nullable;

import com.aptatek.pkuapp.data.PinCode;
import com.aptatek.pkuapp.device.DeviceHelper;
import com.aptatek.pkuapp.domain.interactor.auth.AuthInteractor;
import com.aptatek.pkuapp.view.base.idle.SimpleIdlingResource;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
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


    void verifyPin(final PinCode addedPin, final PinCode confirmationPin, @Nullable final SimpleIdlingResource idlingResource) {
        if (addedPin.equals(confirmationPin)) {
            if (idlingResource != null) {
                idlingResource.setIdleState(false);
            }
            disposable = authInteractor.setPinCode(confirmationPin)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() ->
                    {
                        if (idlingResource != null) {
                            idlingResource.setIdleState(true);
                        }
                        ifViewAttached(ConfirmPinView::onValidPinTyped);
                    }, Timber::e);
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
