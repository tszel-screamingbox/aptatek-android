package com.aptatek.pkuapp.view.parentalgate;

import com.aptatek.pkuapp.view.base.BaseFragment;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ParentalGateView extends MvpView {

    void showScreen(BaseFragment fragment);

    void navigateBack();
}
