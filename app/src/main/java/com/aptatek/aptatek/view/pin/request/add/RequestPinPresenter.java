package com.aptatek.aptatek.view.pin.request.add;

import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.domain.interactor.auth.AuthInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;


class RequestPinPresenter extends MvpBasePresenter<RequestPinView> {

    private final AuthInteractor authInteractor;

    @Inject
    RequestPinPresenter(final AuthInteractor authInteractor) {
        this.authInteractor = authInteractor;
    }


    void verifyPinCode(final PinCode pinCode) {
        if (authInteractor.isValidPinCode(pinCode)) {
            ifViewAttached(RequestPinView::onMainActivityShouldLoad);
        } else {
            ifViewAttached(RequestPinView::onInvalidPinTyped);
        }
    }
}
