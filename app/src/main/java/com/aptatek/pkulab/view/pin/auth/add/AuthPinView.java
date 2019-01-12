package com.aptatek.pkulab.view.pin.auth.add;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;


interface AuthPinView extends MvpView {

    void onValidPinTyped();

    void onInvalidPinTyped(boolean hasReachedAttemptLimit);

    void onInvalidFingerprintDetected(@NonNull String message);

    void onValidFingerprintDetected();

    void onFingerprintAvailable();

    void onFingerprintDisabled();
}
