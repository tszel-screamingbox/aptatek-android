package com.aptatek.pkulab.domain.manager.analytic;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.amplitude.api.Amplitude;
import com.amplitude.api.AmplitudeClient;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class AnalyticsManager implements IAnalyticsManager {

    private static final class Constants {
        static final String EVENT_CATEGORY = "Event Category";
        static final String TIMESTAMP = "Timestamp";
        static final String EVENT_INFO = "Event Info";
        static final String ELAPSED_TIME = "Elapsed Screen Time (sec)";
    }

    @Inject
    AnalyticsManager() {
        this.amplitude = Amplitude.getInstance();
    }
    
    private final AmplitudeClient amplitude;

    @Override
    public void logEvent(final AnalyticsEvent event) {
        logEvent(event.eventName, infoToString(event.getAdditionalInfo()), event.eventCategory, event.timestamp == null ? 0L : event.timestamp);
    }

    @Nullable
    private String infoToString(@Nullable final Pair<String, String> info) {
        if (info == null) return null;

        return String.format("%s: %s", info.first, info.second);
    }

    @Override
    public void logEvent(final String eventName, final String info, final EventCategory category) {
        logEvent(eventName, info, category, System.currentTimeMillis());
    }

    private void logEvent(final String eventName, @Nullable final String info, final EventCategory category, final long timestamp) {
        try {

            final SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.US);
            final String date = dt.format(new Date(timestamp == 0 ? System.currentTimeMillis() : timestamp));

            final JSONObject eventProperties = new JSONObject();
            eventProperties.put(Constants.EVENT_CATEGORY, category.getKey());
            if (info != null) {
                eventProperties.put(Constants.EVENT_INFO, info);
            }
            eventProperties.put(Constants.TIMESTAMP, date);

            amplitude.logEvent(eventName, eventProperties);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    @Override
    public void logElapsedTime(final String eventName, final int seconds, final EventCategory category) {
        try {

            final SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.US);
            final String date = dt.format(new Date(System.currentTimeMillis()));

            final JSONObject eventProperties = new JSONObject();
            eventProperties.put(Constants.EVENT_CATEGORY, category.getKey());
            eventProperties.put(Constants.ELAPSED_TIME, seconds);
            eventProperties.put(Constants.TIMESTAMP, date);

            amplitude.logEvent(eventName, eventProperties);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }
}
