package com.aptatek.pkulab.domain.interactor.auth;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.aptatek.pkulab.data.PinCode;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.manager.FingerprintManager;
import com.aptatek.pkulab.domain.manager.keystore.KeyStoreError;
import com.aptatek.pkulab.domain.manager.keystore.KeyStoreManager;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import timber.log.Timber;

public class AuthInteractor {

    private final FingerprintManager fingerprintManager;
    private final KeyStoreManager keyStoreManager;
    private final PreferenceManager preferencesManager;
    private final File dbFile;

    private static final int CODE_FINGERPRINT_CANCELLED = 5;

    private CancellationSignal cancelSignal;
    private Callback callback;

    @Inject
    AuthInteractor(final FingerprintManager fingerprintManager,
                   final PreferenceManager preferencesManager,
                   final KeyStoreManager keyStoreManager,
                   final @Named("databaseFile") File dbFile) {
        this.fingerprintManager = fingerprintManager;
        this.preferencesManager = preferencesManager;
        this.keyStoreManager = keyStoreManager;
        this.dbFile = dbFile;
    }

    public Completable setPinCode(final PinCode pinCode) {
        return Completable.fromAction(() -> {
            try {
                final String encryptedPin = keyStoreManager.encrypt(pinCode);
                preferencesManager.setEncryptedPin(encryptedPin);
                preferencesManager.setPrefDbEncryptedWithPin();
            } catch (final KeyStoreError error) {
                Timber.e(error, "Failed to set pincode");
                throw new AuthException("Error during decrytpion", error);
            }
        });
    }

    public Completable checkPinCode(final PinCode pinCode) {
        return Completable.fromAction(() -> {
            try {
                final PinCode storedPin = keyStoreManager.decrypt(preferencesManager.getEncryptedPin());
                if (storedPin == null || !storedPin.equals(pinCode)) {
                    throw new AuthException("Invalid pincode");
                }

                if (!preferencesManager.isDbEncrpytedWithPin()) {
                    // delete database first
                    dbFile.delete();
                    preferencesManager.setPrefDbEncryptedWithPin();
                }
            } catch (final KeyStoreError keyStoreError) {
                throw new AuthException("Error during decrytpion", keyStoreError);
            }
        });
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
            if (callback != null && errMsgId != CODE_FINGERPRINT_CANCELLED) {
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
