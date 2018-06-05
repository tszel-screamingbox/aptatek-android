package com.aptatek.aptatek.view.test.takesample;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.view.test.base.TestFragmentBaseView;

public interface TakeSampleView extends TestFragmentBaseView {

    void showAgeSwitcherText(@NonNull String text);

    void loadVideo(@NonNull Uri video);

    void showVideoThumbnail(@NonNull Bitmap thumbnail);

}
