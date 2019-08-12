package com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class FaqClosed extends AnalyticsEvent {

    public FaqClosed(final long elapsedScreenTimeSec) {
        super("risk_faq_closed", System.currentTimeMillis(), EventCategory.RISK_MITIGATION);
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
        return "FaqClosed{" +
                "elapsedScreenTime=" + elapsedScreenTime +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
