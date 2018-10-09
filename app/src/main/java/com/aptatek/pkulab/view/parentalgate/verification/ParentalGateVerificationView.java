package com.aptatek.pkulab.view.parentalgate.verification;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ParentalGateVerificationView extends MvpView {

    void showImage(@DrawableRes int imageRes);

    void showTitle(@NonNull String title);

    void showMessage(@NonNull String message);

    void showButton(boolean visible);

    void finishAfterDelay();

}
