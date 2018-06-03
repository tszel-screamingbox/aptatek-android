package com.aptatek.aptatek.domain.interactor.auth;


import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.aptatek.aptatek.domain.manager.FingerprintManager;

import javax.inject.Inject;

import timber.log.Timber;

public class AuthInteractor {

    private final FingerprintManager fingerprintManager;
    private CancellationSignal cancelSignal;
    private Callback callback;

    @Inject
    public AuthInteractor(FingerprintManager fingerprintManager) {
        this.fingerprintManager = fingerprintManager;
    }


    public void setPinCode() {
        //TODO: implement
    }

    public boolean isValidPinCode() {
        //TODO: implement
        return true;
    }

    public void changePinCode() {
        //TODO: implement
    }

    public void listenFingerPrintScanner(Callback authCallback) {
        callback = authCallback;
        cancelSignal = new CancellationSignal();
        fingerprintManager.authenticate(authenticationCallback, cancelSignal);
    }

    private FingerprintManagerCompat.AuthenticationCallback authenticationCallback = new FingerprintManagerCompat.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            Timber.d("Error occurred during fingerprint authentication: %s", errString.toString());
            if (callback != null) {
                callback.onErrorOccurred(errString.toString());
            }
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
            Timber.d("Help message for fingerprint authentication: %s", helpString.toString());
            if (callback != null) {
                callback.onErrorOccurred(helpString.toString());
            }
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            Timber.d("Successfully authenticated");
            if (callback != null) {
                callback.onSucceeded();
            }
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Timber.d("Invalid fingerprint");
            if (callback != null) {
                callback.onInvalidFingerprintDetected();
            }
        }
    };

    public void cancelFingerprintAuth() {
        if (cancelSignal != null && !cancelSignal.isCanceled()) {
            Timber.d("Cancelling fingerprint scan");
            cancelSignal.cancel();
        }
    }
}
