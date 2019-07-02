package com.aptatek.pkulab.domain.manager.analytic.events.onboarding;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class OnboardingMultipleReaderFound extends AnalyticsEvent {
    public OnboardingMultipleReaderFound() {
        super("onboarding_mutliple_reader_found", null, EventCategory.USER_BEHAVIOUR);
    }
}
