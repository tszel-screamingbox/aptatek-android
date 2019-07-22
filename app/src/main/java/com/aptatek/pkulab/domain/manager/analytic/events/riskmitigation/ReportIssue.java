package com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class ReportIssue extends AnalyticsEvent {

    public enum ReportType {DATA, CONNECTION, OTHER}

    public ReportIssue(final ReportType type) {
        super("risk_report_issue", System.currentTimeMillis(), EventCategory.RISK_MITIGATION);
        this.type = type;
    }

    private final ReportType type;

    @Nullable
    @Override
    public Pair<String, String> getAdditionalInfo() {
        return new Pair<>("report_type", String.valueOf(type));
    }

    @Override
    public String toString() {
        return "ReportIssue{" +
                "reportType=" + type +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
