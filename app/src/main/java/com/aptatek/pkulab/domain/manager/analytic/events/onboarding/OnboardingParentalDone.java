package com.aptatek.pkulab.domain.manager.analytic.events.onboarding;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class OnboardingParentalDone extends AnalyticsEvent {

    public OnboardingParentalDone(final long elapsedScreenTimeSec) {
        super("onboarding_age_gate_done", null, EventCategory.USER_BEHAVIOUR);
        this.elapsedScreenTime = elapsedScreenTimeSec;
    }

    private final long elapsedScreenTime;

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("ellapsed_screentime_sec", String.valueOf(elapsedScreenTime));
        return map;
    }

    @Override
    public String toString() {
        return "OnboardingParentalDone{" +
                "elapsedScreenTime=" + elapsedScreenTime +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
