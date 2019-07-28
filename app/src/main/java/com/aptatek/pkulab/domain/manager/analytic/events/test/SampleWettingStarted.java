package com.aptatek.pkulab.domain.manager.analytic.events.test;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class SampleWettingStarted extends AnalyticsEvent {

    public SampleWettingStarted() {
        super("testing_sample_wetting_start", null, EventCategory.USER_BEHAVIOUR);
    }

    @Override
    public String toString() {
        return "SampleWettingStarted{" +
                "eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
