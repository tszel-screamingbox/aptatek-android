package com.aptatek.aptatek.view.fingerprint;

import com.aptatek.aptatek.domain.manager.SharedPreferencesManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class FingerprintActivityPresenter extends MvpBasePresenter<FingerprintActivityView> {

    private final SharedPreferencesManager sharedPreferencesManager;


    @Inject
    FingerprintActivityPresenter(final SharedPreferencesManager sharedPreferencesManager) {
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    void disableFingerprintScan() {
        sharedPreferencesManager.enableFingerprintScan(false);
    }

    void enableFingerprintScan() {
        sharedPreferencesManager.enableFingerprintScan(true);
    }
}
