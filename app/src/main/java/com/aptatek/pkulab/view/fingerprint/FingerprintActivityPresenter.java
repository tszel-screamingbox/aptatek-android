package com.aptatek.pkulab.view.fingerprint;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.onboarding.OnboardingFingerprintAuth;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class FingerprintActivityPresenter extends MvpBasePresenter<FingerprintActivityView> {

    private final PreferenceManager sharedPreferencesManager;
    private final IAnalyticsManager analyticsManager;

    @Inject
    FingerprintActivityPresenter(final PreferenceManager sharedPreferencesManager, final IAnalyticsManager analyticsManager) {
        this.sharedPreferencesManager = sharedPreferencesManager;
        this.analyticsManager = analyticsManager;
    }

    void disableFingerprintScan() {
        sharedPreferencesManager.enableFingerprintScan(false);
        analyticsManager.logEvent(new OnboardingFingerprintAuth(false));
    }

    void enableFingerprintScan() {
        sharedPreferencesManager.enableFingerprintScan(true);
        analyticsManager.logEvent(new OnboardingFingerprintAuth(true));
    }
}
