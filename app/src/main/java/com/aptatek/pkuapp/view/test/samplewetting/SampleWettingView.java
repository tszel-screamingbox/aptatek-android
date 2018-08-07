package com.aptatek.pkuapp.view.test.samplewetting;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;

public interface SampleWettingView extends TestFragmentBaseView {

    void showCountdown(@NonNull String countdownRemaining);

    void showImage(@DrawableRes int imageRes);

}
