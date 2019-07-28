package com.aptatek.pkulab.domain.manager.analytic.events.test;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class TestingScreenDisplayed extends AnalyticsEvent {

    public TestingScreenDisplayed() {
        super("testing_reader_testing_started", null, EventCategory.USER_BEHAVIOUR);
    }

    @Override
    public String toString() {
        return "TestingScreenDisplayed{" +
                "eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
