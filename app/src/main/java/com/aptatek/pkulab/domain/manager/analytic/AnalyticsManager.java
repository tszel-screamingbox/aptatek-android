package com.aptatek.pkulab.domain.manager.analytic;

import com.amplitude.api.Amplitude;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalyticsManager implements IAnalyticsManager {

    @Inject
    AnalyticsManager() {
    }

    @Override
    public void logEvent(final String message) {
        Amplitude.getInstance().logEvent(message);
    }
}
