package com.aptatek.pkulab.domain.manager.analytic.events.readerconnection;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class OpenPermissionSettings extends AnalyticsEvent {

    public OpenPermissionSettings() {
        super("permission_not_granted_settings", null, EventCategory.ERROR);
    }

    @Override
    public String toString() {
        return "OpenPermissionSettings{" +
                "eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
