package com.aptatek.pkulab.view.test;

import androidx.annotation.NonNull;

public interface TestActivityCommonView {

    void showNextScreen();

    void showPreviousScreen();

    void setBottomBarVisible(boolean visible);

    void setBatteryIndicatorVisible(boolean visible);

    void setBatteryPercentage(int percentage);

    void setDisclaimerViewVisible(boolean visible);

    void setDisclaimerMessage(@NonNull String message);

    void setNextButtonVisible(boolean visible);
}
