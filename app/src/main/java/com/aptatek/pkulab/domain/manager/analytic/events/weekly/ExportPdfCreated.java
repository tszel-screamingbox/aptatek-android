package com.aptatek.pkulab.domain.manager.analytic.events.weekly;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class ExportPdfCreated extends AnalyticsEvent {

    private final String months;

    public ExportPdfCreated(final int months) {
        super("pdf_export_created", null, EventCategory.USER_BEHAVIOUR);
        this.months = mapMonthsToString(months);
    }

    private String mapMonthsToString(final int months) {
        switch (months) {
            case 1: {
                return "last_m";
            }
            case 3: {
                return "last_3_m";
            }
            case 6: {
                return "last_6_m";
            }
            case 12: {
                return "last_y";
            }
            default: {
                return "Unknown range [months]: " + months;
            }
        }
    }

    public String getMonths() {
        return months;
    }

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("selected_export_range", months);
        return map;
    }

    @Override
    public String toString() {
        return "ExportPdfCreated{" +
                "months='" + months + '\'' +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
