package com.aptatek.pkulab.view.connect.common;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public interface BaseConnectPresenter<V extends BaseConnectView> extends MvpPresenter<V> {

    void checkPermissions();

}
