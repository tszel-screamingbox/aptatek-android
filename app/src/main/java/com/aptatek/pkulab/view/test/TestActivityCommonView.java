package com.aptatek.pkulab.view.test;

public interface TestActivityCommonView {

    void showNextScreen();

    void showPreviousScreen();

    void setBottomBarVisible(boolean visible);

    void setBatteryIndicatorVisible(boolean visible);

    void setBatteryPercentage(int percentage);

    void setProgressVisible(boolean visible);

    void setProgressPercentage(int percentage);

}
