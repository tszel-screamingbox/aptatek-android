package com.aptatek.aptatek.view.test.takesample;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.view.test.base.TestFragmentBaseView;

interface TakeSampleView extends TestFragmentBaseView {

    void showAgeSwitcherText(@NonNull String text);

    void loadVideo(@NonNull String video);

}
