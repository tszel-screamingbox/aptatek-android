package com.aptatek.pkuapp.view.fingerprint;

import com.aptatek.pkuapp.device.PreferenceManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class FingerprintActivityPresenter extends MvpBasePresenter<FingerprintActivityView> {

    private final PreferenceManager sharedPreferencesManager;

    @Inject
    FingerprintActivityPresenter(final PreferenceManager sharedPreferencesManager) {
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    void disableFingerprintScan() {
        sharedPreferencesManager.enableFingerprintScan(false);
    }

    void enableFingerprintScan() {
        sharedPreferencesManager.enableFingerprintScan(true);
    }
}
