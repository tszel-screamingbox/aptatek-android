package com.aptatek.pkulab.view.connect.common;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.connect.ConnectReaderScreen;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public abstract class BaseConnectScreenFragment<V extends BaseConnectScreenView, P extends MvpPresenter<V>> extends BaseFragment<V, P> implements BaseConnectScreenView {

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void showScreen(@NonNull final ConnectReaderScreen screen) {
        if (getActivity() instanceof BaseConnectScreenView) {
            ((BaseConnectScreenView) getActivity()).showScreen(screen);
        }
    }

    @Override
    public void navigateBack() {
        if (getActivity() instanceof BaseConnectScreenView) {
            ((BaseConnectScreenView) getActivity()).navigateBack();
        }
    }

}
