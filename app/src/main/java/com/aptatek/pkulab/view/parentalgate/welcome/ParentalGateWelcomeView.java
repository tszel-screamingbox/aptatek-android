package com.aptatek.pkulab.view.parentalgate.welcome;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ParentalGateWelcomeView extends MvpView {

    void setShowButton(boolean visible);

    void setButtonText(@NonNull String text);

    void showDatePicker();

    void setShowBirthDateField(boolean visible);

    void setBirthDateText(@NonNull String text);

    void setShowAgeField(boolean visible);

    void setAgeText(@NonNull String text);

    void setShowKeypad(boolean visible);

    void setKeypadActionText(@NonNull String text);

    void showResult(@NonNull AgeVerificationResult result);

    void showToastWithMessage(@StringRes int resId);

}
