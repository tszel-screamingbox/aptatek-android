package com.aptatek.pkulab.domain.manager.analytic.events.onboarding;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OnboardingReaderConnectFailed extends AnalyticsEvent {
    public OnboardingReaderConnectFailed(final String errorReason) {
        super("onboarding_reader_connection_error", null, EventCategory.ERROR);
        this.errorReason = errorReason;
    }

    private final String errorReason;

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("error_reason", errorReason);
        return map;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OnboardingReaderConnectFailed that = (OnboardingReaderConnectFailed) o;
        return Objects.equals(errorReason, that.errorReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), errorReason);
    }

    @Override
    public String toString() {
        return "OnboardingReaderConnectFailed{" +
                "errorReason='" + errorReason + '\'' +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
