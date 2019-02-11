package com.aptatek.pkulab.view.test;

import android.support.annotation.NonNull;

public interface TestActivityCommonView {

    void showNextScreen();

    void showPreviousScreen();

    void setBottomBarVisible(boolean visible);

    void setBatteryIndicatorVisible(boolean visible);

    void setBatteryPercentageText(@NonNull String percentageText);

    void setProgressVisible(boolean visible);

    void setProgressPercentage(int percentage);

    void setDisclaimerViewVisible(boolean visible);

    void setDisclaimerMessage(@NonNull String message);

}
