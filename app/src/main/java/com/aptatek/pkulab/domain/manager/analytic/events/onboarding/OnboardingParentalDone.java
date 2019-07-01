package com.aptatek.pkulab.domain.manager.analytic.events.onboarding;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class OnboardingParentalDone extends AnalyticsEvent {

    protected OnboardingParentalDone(final long elapsedScreenTimeSec) {
        super("onboarding_age_gate_done", null);
        this.elapsedScreenTime = elapsedScreenTimeSec;
    }

    private final long elapsedScreenTime;

    @Nullable
    @Override
    public Pair<String, String> getAdditionalInfo() {
        return new Pair<>("ellapsed_screentime_sec", String.valueOf(elapsedScreenTime));
    }

    @Override
    public String toString() {
        return "OnboardingParentalDone{" +
                "elapsedScreenTime=" + elapsedScreenTime +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
