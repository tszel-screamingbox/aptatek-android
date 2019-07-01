package com.aptatek.pkulab.domain.manager.analytic.events.onboarding;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.Objects;

public class OnbardingFingerprintAuth extends AnalyticsEvent {

    public OnbardingFingerprintAuth(final boolean enabled) {
        super("onboarding_fingerprint_auth", null);
        this.enabled = enabled;
    }

    private final boolean enabled;

    @Nullable
    @Override
    public Pair<String, String> getAdditionalInfo() {
        return new Pair<>("enabled", Boolean.toString(enabled));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OnbardingFingerprintAuth that = (OnbardingFingerprintAuth) o;
        return enabled == that.enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enabled);
    }

    @Override
    public String toString() {
        return "OnbardingFingerprintAuth{" +
                "enabled=" + enabled +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
