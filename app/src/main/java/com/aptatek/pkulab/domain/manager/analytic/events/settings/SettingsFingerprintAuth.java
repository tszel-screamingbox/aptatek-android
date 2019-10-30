package com.aptatek.pkulab.domain.manager.analytic.events.settings;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingsFingerprintAuth extends AnalyticsEvent {

    public SettingsFingerprintAuth(final boolean enabled) {
        super("settings_fingerprint_auth", null, EventCategory.USER_BEHAVIOUR);
        this.enabled = enabled;
    }

    private final boolean enabled;

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("enabled", Boolean.toString(enabled));
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SettingsFingerprintAuth that = (SettingsFingerprintAuth) o;
        return enabled == that.enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enabled);
    }

    @Override
    public String toString() {
        return "SettingsFingerprintAuth{" +
                "enabled=" + enabled +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}