package com.aptatek.pkulab.domain.manager.analytic.events.readerconnection;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class DeniedPermission extends AnalyticsEvent {

    public DeniedPermission() {
        super("permission_not_granted_bl", null, EventCategory.USER_BEHAVIOUR);
    }

    @Override
    public String toString() {
        return "DeniedPermission{" +
                "eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
