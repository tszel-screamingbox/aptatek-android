package com.aptatek.pkulab.view.parentalgate.verification;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.view.parentalgate.welcome.AgeVerificationResult;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

public class ParentalGateVerificationPresenter extends MvpBasePresenter<ParentalGateVerificationView> {

    private final PreferenceManager preferenceManager;

    @Inject
    public ParentalGateVerificationPresenter(final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public void initUi(@NonNull final AgeVerificationResult result) {
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
