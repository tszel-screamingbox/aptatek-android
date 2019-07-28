package com.aptatek.pkulab.domain.manager.analytic.events.onboarding;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OnboardingNoReaderAvailable extends AnalyticsEvent {
    public OnboardingNoReaderAvailable(final long elapsedScreenTimeSec) {
        super("onboarding_no_reader_available", null, EventCategory.ERROR);
        this.elapsedScreenTimeSec = elapsedScreenTimeSec;
    }

    private final long elapsedScreenTimeSec;

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("ellapsed_screentime_sec", String.valueOf(elapsedScreenTimeSec));
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OnboardingNoReaderAvailable that = (OnboardingNoReaderAvailable) o;
        return elapsedScreenTimeSec == that.elapsedScreenTimeSec;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), elapsedScreenTimeSec);
    }

    @Override
    public String toString() {
        return "OnboardingNoReaderAvailable{" +
                "elapsedScreenTimeSec=" + elapsedScreenTimeSec +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
