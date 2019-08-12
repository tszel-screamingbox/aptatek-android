package com.aptatek.pkulab.domain.manager.analytic.events.test;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class CollectBloodDone extends AnalyticsEvent {

    private final long elapsedTime;

    public CollectBloodDone(final long elapsedTime) {
        super("testing_collect_blood_done", null, EventCategory.USER_BEHAVIOUR);
        this.elapsedTime = elapsedTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("ellapsed_screentime_sec", String.valueOf(elapsedTime));
        return map;
    }

    @Override
    public String toString() {
        return "CollectBloodDone{" +
                "elapsedTime=" + elapsedTime +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
