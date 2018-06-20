package com.aptatek.aptatek.view.pin.auth.add;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.device.DeviceHelper;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.auth.AuthInteractor;
import com.aptatek.aptatek.domain.interactor.auth.Callback;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import timber.log.Timber;


class AuthPinPresenter extends MvpBasePresenter<AuthPinView> {


    private final AuthInteractor authInteractor;
    private final DeviceHelper deviceHelper;
    private final ResourceInteractor resourceInteractor;

    @Inject
    AuthPinPresenter(final AuthInteractor authInteractor,
                     final DeviceHelper deviceHelper,
                     final ResourceInteractor resourceInteractor) {
        this.authInteractor = authInteractor;
        this.deviceHelper = deviceHelper;
        this.resourceInteractor = resourceInteractor;
    }

    void initView() {
        if (deviceHelper.isFingperprintAuthAvailable()) {
            ifViewAttached(AuthPinView::onFingerprintAvailable);
        } else {
            ifViewAttached(AuthPinView::onFingerprintDisabled);
        }
    }

    void startListening() {
        if (!deviceHelper.isFingperprintAuthAvailable()) {
            Timber.d("Fingerprint authentication is not available on this device");
            return;
        }
        authInteractor.listenFingerPrintScanner(new Callback() {
            @Override
            public void onSucceeded() {
                ifViewAttached(AuthPinView::onValidFingerprintDetected);
            }

            @Override
            public void onInvalidFingerprintDetected() {
                final String invalidMessage = resourceInteractor.getStringResource(R.string.auth_pin_message_fingerprint_invalid);
                ifViewAttached(view -> view.onInvalidFingerprintDetected(invalidMessage));
            }

            @Override
            public void onErrorOccurred(final String message) {
                ifViewAttached(view -> view.onInvalidFingerprintDetected(message));
            }
        });
    }

    void stopListening() {
        authInteractor.cancelFingerprintAuth();
    }

    void verifyPinCode(final PinCode pinCode) {
        if (authInteractor.isValidPinCode(pinCode)) {
            ifViewAttached(AuthPinView::onValidPinTyped);
        } else {
            ifViewAttached(AuthPinView::onInvalidPinTyped);
        }
    }
}
