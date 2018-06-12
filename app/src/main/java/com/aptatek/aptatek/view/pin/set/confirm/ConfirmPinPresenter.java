package com.aptatek.aptatek.view.pin.set.confirm;

import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.domain.interactor.auth.AuthInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


class ConfirmPinPresenter extends MvpBasePresenter<ConfirmPinView> {

    private static final int TIMER_PERIOD_IN_SEC = 1;

    private final AuthInteractor authInteractor;

    @Inject
    ConfirmPinPresenter(final AuthInteractor authInteractor) {
        this.authInteractor = authInteractor;
    }


    void verifyPin(final PinCode addedPin, final PinCode confirmationPin) {
        if (addedPin.equals(confirmationPin)) {
            setPinCode(confirmationPin);
        } else {
            differentPins();
        }
    }

    private void setPinCode(final PinCode pin) {
        Observable.empty().delay(TIMER_PERIOD_IN_SEC, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    authInteractor.setPinCode(pin);
                    ifViewAttached(ConfirmPinView::onMainActivityShouldLoad);
                })
                .subscribe();
        ifViewAttached(ConfirmPinView::onValidPinTyped);
    }

    private void differentPins() {
        Observable.empty().delay(TIMER_PERIOD_IN_SEC, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> ifViewAttached(ConfirmPinView::onPinSetFragmentShouldLoad))
                .subscribe();
        ifViewAttached(ConfirmPinView::onInvalidPinTyped);
    }
}