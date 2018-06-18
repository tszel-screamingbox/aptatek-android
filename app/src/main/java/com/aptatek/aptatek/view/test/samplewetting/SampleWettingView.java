package com.aptatek.aptatek.view.test.samplewetting;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.view.test.base.TestFragmentBaseView;

public interface SampleWettingView extends TestFragmentBaseView {

    void showCountdown(@NonNull String countdownRemaining);

    void showImage(@DrawableRes int imageRes);

}
