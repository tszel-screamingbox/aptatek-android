package com.aptatek.pkulab.domain.manager.analytic.events.onboarding;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class OnboardingPinSetFailed extends AnalyticsEvent {

    public OnboardingPinSetFailed() {
        super("onboarding_pin_set_failed", null, EventCategory.USER_BEHAVIOUR.USER_BEHAVIOUR);
    }
}
