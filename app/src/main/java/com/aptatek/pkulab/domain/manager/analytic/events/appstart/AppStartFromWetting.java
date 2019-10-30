package com.aptatek.pkulab.domain.manager.analytic.events.appstart;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;


public class AppStartFromWetting extends AnalyticsEvent {


    public AppStartFromWetting() {
        super("open_app_from_wetting_timer", System.currentTimeMillis(), EventCategory.USER_BEHAVIOUR);
    }

    @Override
    public String toString() {
        return "AppStartFromWetting{" +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
