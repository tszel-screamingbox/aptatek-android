package com.aptatek.aptatek.view.test.base;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.view.test.TestActivityView;

public interface TestFragmentBaseView extends TestActivityView {

    void setTitle(@NonNull String title);

    void setMessage(@NonNull String message);

}
