package com.aptatek.pkulab.domain.manager.analytic.events.appstart;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.Objects;

public class AppStartFromWetting extends AnalyticsEvent {

    private final long elapsedTimeSec;

    public AppStartFromWetting(final long elapsedTimeSec) {
        super("open_app_from_wetting_timer", System.currentTimeMillis(), EventCategory.USER_BEHAVIOUR);
        this.elapsedTimeSec = elapsedTimeSec;
    }

    @Nullable
    @Override
    public Pair<String, String> getAdditionalInfo() {
        return new Pair<>("elapsed_time_sec", String.valueOf(elapsedTimeSec));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AppStartFromWetting that = (AppStartFromWetting) o;
        return elapsedTimeSec == that.elapsedTimeSec;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), elapsedTimeSec);
    }

    @Override
    public String toString() {
        return "AppStartFromWetting{" +
                "elapsedTimeSec=" + elapsedTimeSec +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
