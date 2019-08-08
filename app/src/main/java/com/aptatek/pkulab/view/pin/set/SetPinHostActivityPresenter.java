package com.aptatek.pkulab.view.pin.set;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.onboarding.OnboardingPinSetDone;
import com.aptatek.pkulab.domain.manager.analytic.events.onboarding.OnboardingPinSetFailed;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class SetPinHostActivityPresenter extends MvpBasePresenter<SetPinHostActivityView> {

    private final IAnalyticsManager analyticsManager;
    private long attachTimestamp = 0L;

    @Inject
    SetPinHostActivityPresenter(final IAnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
    }

    @Override
    public void attachView(final @Nullable SetPinHostActivityView view) {
        super.attachView(view);

        if (attachTimestamp == 0L) {
            attachTimestamp = System.currentTimeMillis();
        }
    }

    void onPinSet() {
        analyticsManager.logEvent(new OnboardingPinSetDone(System.currentTimeMillis() - attachTimestamp));
    }

    void onPinSetFailed() {
        analyticsManager.logEvent(new OnboardingPinSetFailed());
    }
}
