package com.aptatek.pkulab.view.pin.set.add;

import com.aptatek.pkulab.data.PinCode;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;


class AddPinPresenter extends MvpBasePresenter<AddPinView> {

    @Inject
    AddPinPresenter() {
    }

    void finishedTyping(final PinCode pinCode) {
        ifViewAttached(view -> {
            final String exception = "123456";

            final PinCode exceptionPin = new PinCode(exception.toCharArray());

            if (exceptionPin.equals(pinCode)) {
                view.invalidPinFormat("Wrong pin format");
            } else {
                view.forward(pinCode.getBytes());
            }
        });
    }
}
