package com.aptatek.aptatek.view.pin.set;


import com.aptatek.aptatek.data.PinCode;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

class SetPinActivityPresenter extends MvpBasePresenter<SetPinActivityView> {

    private PinCode pinCode;
    private Disposable disposable;

    @Inject
    SetPinActivityPresenter() {
    }

    void setPinCode(PinCode pin) {

        if (pinCode == null) {
            this.pinCode = pin;
            ifViewAttached(SetPinActivityView::onShowConfirmationTexts);
            return;
        }

        boolean isValidPin = pinCode.isTheSame(pin);

        disposable = Observable.interval(1, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    disposable.dispose();
                    if (isValidPin) {
                        ifViewAttached(SetPinActivityView::onMainActivityShouldLoad);
                    } else {
                        pinCode = null;
                        ifViewAttached(SetPinActivityView::onResetPinActivityShouldLoad);
                    }
                });

        if (isValidPin) {
            ifViewAttached(SetPinActivityView::onValidFingerPrintTyped);
        } else {
            ifViewAttached(SetPinActivityView::onInvalidFingerPrintTyped);
        }
    }
}