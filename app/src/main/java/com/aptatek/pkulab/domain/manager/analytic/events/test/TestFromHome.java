package com.aptatek.pkulab.domain.manager.analytic.events.test;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class TestFromHome extends AnalyticsEvent {

    private final int phoneBatteryPercent;

    public TestFromHome(final int phoneBatteryPercent) {
        super("testing_started_from_home", null, EventCategory.USER_BEHAVIOUR);
        this.phoneBatteryPercent = phoneBatteryPercent;
    }

    public int getPhoneBatteryPercent() {
        return phoneBatteryPercent;
    }

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("phone_battery", String.valueOf(phoneBatteryPercent));
        return map;
    }

    @Override
    public String toString() {
        return "TestFromHome{" +
                "phoneBatteryPercent=" + phoneBatteryPercent +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
