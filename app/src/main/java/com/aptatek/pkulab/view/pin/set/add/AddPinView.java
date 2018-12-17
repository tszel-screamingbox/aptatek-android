package com.aptatek.pkulab.view.pin.set.add;

import com.hannesdorfmann.mosby3.mvp.MvpView;


interface AddPinView extends MvpView {
    void forward(byte[] pinBytes);
    void invalidPinFormat(String message);
}
