package com.aptatek.pkuapp.view.test.wetting;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;

public interface WettingView extends TestFragmentBaseView {

    void showCountdown(@NonNull String countdownRemaining);

    void onCountdownFinished();

}
