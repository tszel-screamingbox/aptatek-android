package com.aptatek.aptatek.domain.interactor.auth;


import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.aptatek.aptatek.domain.manager.FingerprintManager;

import java.security.GeneralSecurityException;

import javax.inject.Inject;

import timber.log.Timber;

public class AuthInteractor extends FingerprintManagerCompat.AuthenticationCallback {

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

    public void startFingerprintAuth(Callback authCallback) throws GeneralSecurityException {
        callback = authCallback;
        cancelSignal = new CancellationSignal();
        fingerprintManager.authenticate(this, cancelSignal);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);
        Timber.d("Error occurred during fingerprint scanning: %s", errString.toString());
        if (callback != null) {
            callback.errorOccurred(errString.toString());
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);
        Timber.d("Help message for fingerprint scanning: %s", helpString.toString());
        if (callback != null) {
            callback.errorOccurred(helpString.toString());
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        Timber.d("Successfully authenticated");
        if (callback != null) {
            callback.succeed();
        }
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Timber.d("Invalid fingerprint");
        if (callback != null) {
            callback.invalidFingerprint();
        }
    }

    public void cancelFingerprintAuth() {
        if (cancelSignal != null && !cancelSignal.isCanceled()) {
            Timber.d("Cancelling fingerprint scan");
            cancelSignal.cancel();
        }
    }
}
