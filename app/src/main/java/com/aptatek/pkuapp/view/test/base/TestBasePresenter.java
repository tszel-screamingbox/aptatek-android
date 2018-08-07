package com.aptatek.pkuapp.view.test.base;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

public abstract class TestBasePresenter<V extends TestFragmentBaseView> extends MvpBasePresenter<V> {

    public abstract void initUi();

}
