package com.aptatek.pkulab.domain.manager.analytic.events;

import android.util.Pair;

import androidx.annotation.Nullable;

import java.util.Objects;

public abstract class AnalyticsEvent {

    public final String eventName;

    public final Long timestamp;

    protected AnalyticsEvent(final String eventName, final Long timestamp) {
        this.eventName = eventName;
        this.timestamp = timestamp;
    }

    public String getEventName() {
        return eventName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Nullable
    public Pair<String, String> getAdditionalInfo() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalyticsEvent that = (AnalyticsEvent) o;
        return Objects.equals(eventName, that.eventName) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName, timestamp);
    }
}
