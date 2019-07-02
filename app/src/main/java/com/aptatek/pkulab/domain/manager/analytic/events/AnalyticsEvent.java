package com.aptatek.pkulab.domain.manager.analytic.events;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;

import java.util.Objects;

public abstract class AnalyticsEvent {

    public final String eventName;

    public final Long timestamp;

    public final EventCategory eventCategory;

    protected AnalyticsEvent(final String eventName, final Long timestamp, final EventCategory eventCategory) {
        this.eventName = eventName;
        this.timestamp = timestamp;
        this.eventCategory = eventCategory;
    }

    public String getEventName() {
        return eventName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
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
                Objects.equals(timestamp, that.timestamp) &&
                eventCategory == that.eventCategory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName, timestamp, eventCategory);
    }

    @Override
    public String toString() {
        return "AnalyticsEvent{" +
                "eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
