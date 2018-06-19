package com.aptatek.aptatek.view.parentalgate;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ParentalGateView extends MvpView {

    void showScreen(@NonNull ParentalGateScreens screen);

}
