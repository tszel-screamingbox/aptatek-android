package com.aptatek.pkuapp.domain.interactor.auth;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.aptatek.pkuapp.data.PinCode;
import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.domain.manager.FingerprintManager;
import com.aptatek.pkuapp.domain.manager.keystore.KeyStoreError;
import com.aptatek.pkuapp.domain.manager.keystore.KeyStoreManager;

import javax.inject.Inject;

import timber.log.Timber;

public class AuthInteractor {

    private final FingerprintManager fingerprintManager;
    private final KeyStoreManager keyStoreManager;
    private final PreferenceManager preferencesManager;

    private CancellationSignal cancelSignal;
    private Callback callback;

    @Inject
    AuthInteractor(final FingerprintManager fingerprintManager,
                   final PreferenceManager preferencesManager,
                   final KeyStoreManager keyStoreManager) {
        this.fingerprintManager = fingerprintManager;
        this.preferencesManager = preferencesManager;
        this.keyStoreManager = keyStoreManager;
    }


    public void setPinCode(final PinCode pinCode) {
        try {
            final String encryptedPin = keyStoreManager.encrypt(pinCode);
            preferencesManager.setEncryptedPin(encryptedPin);
        } catch (final KeyStoreError e) {
            Timber.e(e, "Failed to set pincode");
        }
    }

    public boolean isValidPinCode(final PinCode pinCode) {
        try {
            final PinCode storedPin = keyStoreManager.decrypt(preferencesManager.getEncryptedPin());
            return storedPin != null && storedPin.equals(pinCode);
        } catch (final KeyStoreError keyStoreError) {
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
