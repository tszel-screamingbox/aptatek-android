package com.aptatek.pkuapp.view.test.base;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface TestFragmentBaseView extends MvpView {

    void setTitle(@NonNull String title);

    void setMessage(@NonNull String message);

    void setAlertViewVisible(boolean visible);

    void setAlertMessage(@NonNull String message);

    void playVideo(@NonNull Uri uri, boolean shouldLoop);

    void setBottomBarVisible(boolean visible);

    void setBatteryIndicatorVisible(boolean visible);

    void setBatteryPercentageText(@NonNull String percentageText);

    void setProgressVisible(boolean visible);

    void setProgressPercentage(int percentage);

}
