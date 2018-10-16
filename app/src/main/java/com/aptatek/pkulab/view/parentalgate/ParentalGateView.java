package com.aptatek.pkulab.view.parentalgate;

import com.aptatek.pkulab.view.base.BaseFragment;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ParentalGateView extends MvpView {

    void showScreen(BaseFragment fragment);

    void navigateBack();
}
