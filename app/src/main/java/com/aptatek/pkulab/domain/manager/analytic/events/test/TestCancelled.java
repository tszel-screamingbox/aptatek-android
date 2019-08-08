package com.aptatek.pkulab.domain.manager.analytic.events.test;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class TestCancelled extends AnalyticsEvent {

    private final String screenName;

    public TestCancelled(final String screenName) {
        super("testing_cancelled", null, EventCategory.USER_BEHAVIOUR);
        this.screenName = screenName;
    }

    public String getScreenName() {
        return screenName;
    }

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("screen_name", screenName);
        return map;
    }

    @Override
    public String toString() {
        return "TestCancelled{" +
                "screenName='" + screenName + '\'' +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
