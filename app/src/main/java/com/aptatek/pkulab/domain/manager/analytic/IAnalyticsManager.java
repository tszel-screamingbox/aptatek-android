package com.aptatek.pkulab.domain.manager.analytic;

public interface IAnalyticsManager {

    void logEvent(final String eventName, final String eventInfo, final EventCategory category);

    void logElapsedTime(final String eventName, final int seconds, final EventCategory category);
}
