package com.aptatek.pkulab.domain.manager.analytic.events.readerconnection;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class TurnReaderOnDisplayed extends AnalyticsEvent {

    public TurnReaderOnDisplayed() {
        super("reader_turn_on_displayed", null, EventCategory.USER_BEHAVIOUR);
    }

    @Override
    public String toString() {
        return "TurnReaderOnDisplayed{" +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
