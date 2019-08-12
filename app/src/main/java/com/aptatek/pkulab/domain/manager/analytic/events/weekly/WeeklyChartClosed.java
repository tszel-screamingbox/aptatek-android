package com.aptatek.pkulab.domain.manager.analytic.events.weekly;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class WeeklyChartClosed extends AnalyticsEvent {

    private final long elapsedTime;

    public WeeklyChartClosed(final long elapsedTime) {
        super("weekly_chart_closed", null, EventCategory.USER_BEHAVIOUR);
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
        return "WeeklyChartClosed{" +
                "elapsedTime=" + elapsedTime +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
