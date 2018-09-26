package com.aptatek.pkuapp.view.connect;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ConnectReaderView extends MvpView {

    void showScreen(@NonNull ConnectReaderScreen screen);

}
