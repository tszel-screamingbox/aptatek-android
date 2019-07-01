package com.aptatek.pkulab.domain.manager.analytic.events.onboarding;

import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class OnboardingReaderConnected extends AnalyticsEvent {

    public OnboardingReaderConnected() {
        super("onboarding_reader_connected", null);
    }

}
