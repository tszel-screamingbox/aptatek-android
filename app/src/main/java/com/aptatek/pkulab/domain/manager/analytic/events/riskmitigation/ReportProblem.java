package com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class ReportProblem extends AnalyticsEvent {

    public enum ReportType {DATA, CONNECTION, OTHER}

    public ReportProblem(final ReportType type) {
        super("risk_report_issue", System.currentTimeMillis(), EventCategory.RISK_MITIGATION);
        this.type = type;
    }

    private final ReportType type;

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("report_type", String.valueOf(type));
        return map;
    }

    @Override
    public String toString() {
        return "ReportProblem{" +
                "reportType=" + type +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
