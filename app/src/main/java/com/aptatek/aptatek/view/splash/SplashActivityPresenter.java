package com.aptatek.aptatek.view.splash;

import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.manager.keystore.KeyStoreManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class SplashActivityPresenter extends MvpBasePresenter<SplashActivityView> {

    private final KeyStoreManager keyStoreManager;
    private final PreferenceManager preferenceManager;

    @Inject
    SplashActivityPresenter(final KeyStoreManager keyStoreManager, final PreferenceManager preferenceManager) {
        this.keyStoreManager = keyStoreManager;
        this.preferenceManager = preferenceManager;
    }

    void switchToNextActivity() {
        ifViewAttached(attachedView -> {
            if (!preferenceManager.getParentalPassed()) {
                attachedView.onParentalGateShouldLoad();
            } else if (keyStoreManager.aliasExists()) {
                attachedView.onRequestPinActivityShouldLoad();
            } else {
                attachedView.onSetPinActivityShouldLoad();
            }
        });
    }
}
