package com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class FaqClosed extends AnalyticsEvent {

    public FaqClosed(final long elapsedScreenTimeSec) {
        super("risk_faq_closed", System.currentTimeMillis(), EventCategory.RISK_MITIGATION);
        this.elapsedScreenTime = elapsedScreenTimeSec;
    }

    private final long elapsedScreenTime;

    @Nullable
    @Override
    public Pair<String, String> getAdditionalInfo() {
        return new Pair<>("ellapsed_screentime_sec", String.valueOf(elapsedScreenTime));
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
