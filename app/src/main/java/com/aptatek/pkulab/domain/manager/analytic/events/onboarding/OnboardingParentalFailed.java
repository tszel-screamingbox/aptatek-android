package com.aptatek.pkulab.domain.manager.analytic.events.onboarding;

import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class OnboardingParentalFailed extends AnalyticsEvent {

    public OnboardingParentalFailed() {
        super("onboarding_age_gate_failed", null);
    }

}
