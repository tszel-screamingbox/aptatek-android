package com.aptatek.aptatek.view.splash;

import com.aptatek.aptatek.domain.manager.KeyStoreManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class SplashActivityPresenter extends MvpBasePresenter<SplashActivityView> {

    private final KeyStoreManager keyStoreManager;

    @Inject
    SplashActivityPresenter(final KeyStoreManager keyStoreManager) {
        this.keyStoreManager = keyStoreManager;
    }

    void switchToNextActivity() {
        if (keyStoreManager.aliasExists()) {
            ifViewAttached(SplashActivityView::onRequestPinActivityShouldLoad);
        } else {
            ifViewAttached(SplashActivityView::onSetPinActivityShouldLoad);
        }
    }
}
