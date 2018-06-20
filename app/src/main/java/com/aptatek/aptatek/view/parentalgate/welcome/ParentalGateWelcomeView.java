package com.aptatek.aptatek.view.parentalgate.welcome;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ParentalGateWelcomeView extends MvpView {

    void showButton(boolean visible);

    void setButtonText(@NonNull String text);

    void showBirthDateField(boolean visible);

    void setBirthDateText(@NonNull String text);

    void showAgeField(boolean visible);

    void showKeypad(boolean visible);

    void setKeypadActionText(@NonNull String text);

    void setKeypadActionEnabled(boolean enabled);

}
