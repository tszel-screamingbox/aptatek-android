package com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation;

import static com.aptatek.pkulab.domain.manager.analytic.EventCategory.RISK_MITIGATION;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UnfinishedTest extends AnalyticsEvent {

    public UnfinishedTest(final String eventName) {
        super(eventName, System.currentTimeMillis(), RISK_MITIGATION);
    }

    public UnfinishedTest(final String eventName, final boolean isConnected) {
        super(eventName, System.currentTimeMillis(), RISK_MITIGATION);
        this.isConnected = isConnected;
    }

    private boolean isConnected;

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("was_chamber_connected", Boolean.toString(isConnected));
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final UnfinishedTest that = (UnfinishedTest) o;
        return isConnected == that.isConnected;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isConnected);
    }

    @Override
    public String toString() {
        return eventName +
                "{connected=" + isConnected +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
