package com.aptatek.pkuapp.view.parentalgate.welcome;

import android.support.annotation.NonNull;

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

}
