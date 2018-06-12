package com.aptatek.aptatek.device;

import android.content.Context;
import android.content.SharedPreferences;

import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceManager {

    private static final String PREF_INCUBATION_START = "aptatek.test.incubation.start";

    private final SharedPreferences sharedPreferences;

    @Inject
    PreferenceManager(@ApplicationContext final Context applicationContext) {
        this.sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    public void setIncubationStart(final long timestamp) {
        sharedPreferences.edit().putLong(PREF_INCUBATION_START, timestamp).apply();
    }

    public long getIncubationStart() {
        return sharedPreferences.getLong(PREF_INCUBATION_START, 0L);
    }
}
