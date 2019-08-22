package com.aptatek.pkulab.domain.manager.analytic.events.appstart;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class AppStart extends AnalyticsEvent {

    public AppStart(final String eventName, final EventCategory category) {
        super(eventName, System.currentTimeMillis(), category);
    }
}
