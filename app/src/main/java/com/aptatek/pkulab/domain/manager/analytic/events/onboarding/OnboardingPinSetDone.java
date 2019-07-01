package com.aptatek.pkulab.domain.manager.analytic.events.onboarding;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.Objects;

public class OnboardingPinSetDone extends AnalyticsEvent {

    public OnboardingPinSetDone(final long elapsedScreenTimeSec, final long elapsedScreenTimeSec1) {
        super("onboarding_pin_set_done", null);
        this.elapsedScreenTimeSec = elapsedScreenTimeSec1;
    }

    private final long elapsedScreenTimeSec;

    @Nullable
    @Override
    public Pair<String, String> getAdditionalInfo() {
        return new Pair<>("ellapsed_screentime_sec", String.valueOf(elapsedScreenTimeSec));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OnboardingPinSetDone that = (OnboardingPinSetDone) o;
        return elapsedScreenTimeSec == that.elapsedScreenTimeSec;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), elapsedScreenTimeSec);
    }

    @Override
    public String toString() {
        return "OnboardingPinSetDone{" +
                "elapsedScreenTimeSec=" + elapsedScreenTimeSec +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
