package com.aptatek.pkulab.view.connect.common;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

public interface BaseConnectView extends MvpView {

    void requestPermissions(@NonNull List<String> permissions);

}
