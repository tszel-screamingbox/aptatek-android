package com.aptatek.aptatek.domain.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

import javax.inject.Inject;

/**
 * Wrapper for shared preferences easy access
 */

public class SharedPreferencesManager {

    private static final class Constants {
        static final String SHARED_PREFERENCES_NAME = "com.aptatek.aptatek";
    }

    public static final String PREF_PARAM_ENCRYPTED_PIN = "encrypted_pin";
    public static final String PREF_PARAM_FINGERPRINT_SCAN = "fingerprint_scan";

    private SharedPreferences preferences;

    @Inject
    public SharedPreferencesManager(@ApplicationContext final Context context) {
        preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setEncryptedPin(final String encryptedPin) {
        preferences.edit().putString(PREF_PARAM_ENCRYPTED_PIN, encryptedPin).apply();
    }

    public String getEncryptedPin() {
        return preferences.getString(PREF_PARAM_ENCRYPTED_PIN, null);
    }

    public void enableFingerprintScan(final boolean enable) {
        preferences.edit().putBoolean(PREF_PARAM_FINGERPRINT_SCAN, enable).apply();
    }

    public boolean isFingerprintScanEnabled() {
        return preferences.getBoolean(PREF_PARAM_FINGERPRINT_SCAN, false);
    }

    public void clearPreference(final String key) {
        preferences.edit().remove(key).apply();
    }

    public void clearAllPreference() {
        preferences.edit().clear().apply();
    }

}
