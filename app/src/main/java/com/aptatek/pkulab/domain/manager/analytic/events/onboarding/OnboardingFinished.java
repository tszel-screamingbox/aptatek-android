package com.aptatek.pkulab.domain.manager.analytic.events.onboarding;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.Objects;

public class OnboardingFinished extends AnalyticsEvent {
    public OnboardingFinished(final long elapsedTimeSec) {
        super("onboarding_done", null, EventCategory.USER_BEHAVIOUR);
        this.elapsedTimeSec = elapsedTimeSec;
    }

    private final long elapsedTimeSec;

    @Nullable
    @Override
    public Pair<String, String> getAdditionalInfo() {
        return new Pair<>("elapsed_time_sec", String.valueOf(elapsedTimeSec));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OnboardingFinished that = (OnboardingFinished) o;
        return elapsedTimeSec == that.elapsedTimeSec;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), elapsedTimeSec);
    }

    @Override
    public String toString() {
        return "OnboardingFinished{" +
                "elapsedTimeSec=" + elapsedTimeSec +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
