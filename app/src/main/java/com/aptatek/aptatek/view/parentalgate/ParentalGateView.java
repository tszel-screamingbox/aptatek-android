package com.aptatek.aptatek.view.parentalgate;

import com.aptatek.aptatek.view.base.BaseFragment;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ParentalGateView extends MvpView {

    void showScreen(BaseFragment fragment);

    void navigateBack();
}
