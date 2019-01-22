package com.aptatek.pkulab.view.pin.auth.add;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;


interface AuthPinView extends MvpView {

    void onValidPinTyped();

    void onInvalidPinTyped();

    void showAlertDialog();

    void onInvalidFingerprintDetected(@NonNull String message);

    void onValidFingerprintDetected();
}
