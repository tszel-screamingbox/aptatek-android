package com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

public class FaqPresented extends AnalyticsEvent {

    public FaqPresented() {
        super("risk_faq_presented", System.currentTimeMillis(), EventCategory.RISK_MITIGATION);
    }

    @Override
    public String toString() {
        return "FaqPresented{" +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
