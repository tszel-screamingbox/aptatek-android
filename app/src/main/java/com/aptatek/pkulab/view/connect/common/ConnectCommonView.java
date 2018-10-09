package com.aptatek.pkulab.view.connect.common;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.view.connect.ConnectReaderScreen;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ConnectCommonView extends MvpView {

    void showScreen(@NonNull ConnectReaderScreen screen);

    void navigateBack();

}
