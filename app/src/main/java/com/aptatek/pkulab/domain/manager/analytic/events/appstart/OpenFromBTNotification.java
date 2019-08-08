package com.aptatek.pkulab.domain.manager.analytic.events.appstart;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class OpenFromBTNotification extends AnalyticsEvent {

    // event_type: [end_of_self_checking, end_of_sample_wetting, end_of_test, error]
    private final String eventType;

    public OpenFromBTNotification(final String eventType) {
        super("open_app_from_bt_notification", null, EventCategory.USER_BEHAVIOUR);

        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("event_type", eventType);
        return map;
    }

    @Override
    public String toString() {
        return "OpenFromBTNotification{" +
                "eventType='" + eventType + '\'' +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
