package com.aptatek.pkulab.view.connect.onboarding.common;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.view.connect.onboarding.ConnectReaderScreen;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface BaseConnectOnboardingScreenView extends MvpView {

    void showScreen(@NonNull ConnectReaderScreen screen);

    void navigateBack();

}
