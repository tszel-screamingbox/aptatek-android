package com.aptatek.pkuapp.view.test.base;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.view.test.TestActivityView;

public interface TestFragmentBaseView extends TestActivityView {

    void setTitle(@NonNull String title);

    void setMessage(@NonNull String message);

    boolean onNavigateBackPressed();

    boolean onNavigateForwardPressed();

}
