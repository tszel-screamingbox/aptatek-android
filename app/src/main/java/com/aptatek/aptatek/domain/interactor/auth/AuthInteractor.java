package com.aptatek.aptatek.domain.interactor.auth;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.domain.manager.FingerprintManager;
import com.aptatek.aptatek.domain.manager.SharedPreferencesManager;
import com.aptatek.aptatek.domain.manager.keystore.KeyStoreError;
import com.aptatek.aptatek.domain.manager.keystore.KeyStoreManager;

import javax.inject.Inject;

import timber.log.Timber;

public class AuthInteractor {

    private final FingerprintManager fingerprintManager;
    private final KeyStoreManager keyStoreManager;
    private final SharedPreferencesManager sharedPreferencesManager;

    private CancellationSignal cancelSignal;
    private Callback callback;

    @Inject
    AuthInteractor(final FingerprintManager fingerprintManager,
                   final SharedPreferencesManager sharedPreferencesManager,
                   final KeyStoreManager keyStoreManager) {
        this.fingerprintManager = fingerprintManager;
        this.sharedPreferencesManager = sharedPreferencesManager;
        this.keyStoreManager = keyStoreManager;
    }


    public void setPinCode(final PinCode pinCode) {
        try {
            final String encryptedPin = keyStoreManager.encrypt(pinCode);
            sharedPreferencesManager.setEncryptedPin(encryptedPin);
        } catch (KeyStoreError e) {
            Timber.e(e, "Failed to set pincode");
        }
    }

    public boolean isValidPinCode(final PinCode pinCode) {
        try {
            final PinCode storedPin = keyStoreManager.decrypt(sharedPreferencesManager.getEncryptedPin());
            return storedPin != null && storedPin.equals(pinCode);
        } catch (KeyStoreError keyStoreError) {
            Timber.e(keyStoreError.getCause());
            return false;
        }
    }

    public void changePinCode() {
        //TODO: implement
    }

    public void listenFingerPrintScanner(final Callback authCallback) {
        callback = authCallback;
        cancelSignal = new CancellationSignal();
        fingerprintManager.authenticate(authenticationCallback, cancelSignal);
    }

    private FingerprintManagerCompat.AuthenticationCallback authenticationCallback = new FingerprintManagerCompat.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(final int errMsgId, final CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            Timber.d("Error occurred during fingerprint authentication: %s", errString.toString());
            if (callback != null) {
                callback.onErrorOccurred(errString.toString());
            }
        }

        @Override
        public void onAuthenticationHelp(final int helpMsgId, final CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
            Timber.d("Help message for fingerprint authentication: %s", helpString.toString());
            if (callback != null) {
                callback.onErrorOccurred(helpString.toString());
            }
        }

        @Override
        public void onAuthenticationSucceeded(final FingerprintManagerCompat.AuthenticationResult result) {
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
