package com.aptatek.pkulab.domain.manager.analytic.events.test;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class MixSampleDone extends AnalyticsEvent {

    private final long elapsedTime;

    public MixSampleDone(final long elapsedTime) {
        super("testing_mix_sample_done", null, EventCategory.USER_BEHAVIOUR);
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
        return "MixSampleDone{" +
                "elapsedTime=" + elapsedTime +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
