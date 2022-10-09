package com.aptatek.pkulab.view.test.collectblood;

import android.text.Spanned;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

public interface CollectBloodView extends TestFragmentBaseView {

    void setInfoMessage(@NonNull Spanned text);
}
