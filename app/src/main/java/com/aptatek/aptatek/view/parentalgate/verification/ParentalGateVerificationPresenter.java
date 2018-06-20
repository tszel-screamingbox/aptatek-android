package com.aptatek.aptatek.view.parentalgate.verification;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.view.parentalgate.welcome.AgeVerificationResult;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

public class ParentalGateVerificationPresenter extends MvpBasePresenter<ParentalGateVerificationView> {

    private final PreferenceManager preferenceManager;

    @Inject
    ParentalGateVerificationPresenter(PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    void initUi(@NonNull final AgeVerificationResult result) {
        ifViewAttached(attachedView -> {
            attachedView.showImage(result.getIconRes());
            attachedView.showTitle(result.getTitle());
            attachedView.showMessage(result.getMessage());
            attachedView.showButton(result.isShowButton());

            if (!result.isShowButton()) {
                preferenceManager.setParentalPassed(true);
                attachedView.finishAfterDelay();
            }
        });
    }
}
