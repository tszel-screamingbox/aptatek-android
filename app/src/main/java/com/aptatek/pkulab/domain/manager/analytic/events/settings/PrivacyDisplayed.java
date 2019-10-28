package com.aptatek.pkulab.domain.manager.analytic.events.settings;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class PrivacyDisplayed extends AnalyticsEvent {

    public PrivacyDisplayed(final long elapsedScreenTimeSec) {
        super("settings_privacy_policy_displayed", System.currentTimeMillis(), EventCategory.USER_BEHAVIOUR);
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
        return "PrivacyDisplayed{" +
                "elapsedScreenTime=" + elapsedScreenTime +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
