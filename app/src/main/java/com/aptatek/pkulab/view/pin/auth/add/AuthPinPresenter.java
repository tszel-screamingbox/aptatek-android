package com.aptatek.pkulab.view.pin.auth.add;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.data.PinCode;
import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.auth.AuthInteractor;
import com.aptatek.pkulab.domain.interactor.auth.Callback;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


class AuthPinPresenter extends MvpBasePresenter<AuthPinView> {

    private static final int PIN_CODE_ATTEMPT_ERROR_LIMIT = 5;

    private final AuthInteractor authInteractor;
    private final DeviceHelper deviceHelper;
    private final ResourceInteractor resourceInteractor;

    private int attemptCount = 0;

    private Disposable disposable;

    @Inject
    AuthPinPresenter(final AuthInteractor authInteractor,
                     final DeviceHelper deviceHelper,
                     final ResourceInteractor resourceInteractor) {
        this.authInteractor = authInteractor;
        this.deviceHelper = deviceHelper;
        this.resourceInteractor = resourceInteractor;
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
                attemptCount++;
                final String invalidMessage = resourceInteractor.getStringResource(R.string.auth_pin_message_fingerprint_invalid);
                ifViewAttached(view -> view.onInvalidFingerprintDetected(invalidMessage));
                if (attemptCount == PIN_CODE_ATTEMPT_ERROR_LIMIT) {
                    attemptCount = 0;
                    ifViewAttached(AuthPinView::showAlertDialog);
                }
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
        attemptCount++;
        disposable = authInteractor.checkPinCode(pinCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> ifViewAttached(AuthPinView::onValidPinTyped),
                        throwable -> ifViewAttached(view -> {
                            if (attemptCount == PIN_CODE_ATTEMPT_ERROR_LIMIT) {
                                attemptCount = 0;
                                view.showAlertDialog();
                            } else {
                                view.onInvalidPinTyped();
                            }
                        }));
    }


    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
