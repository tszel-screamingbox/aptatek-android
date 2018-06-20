package com.aptatek.aptatek.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceManager {

    private static final class Constants {
        static final String SHARED_PREFERENCES_NAME = "com.aptatek.aptatek";
    }

    public static final String PREF_INCUBATION_START = "aptatek.test.incubation.starter";
    public static final String PREF_ENCRYPTED_PIN = "aptatek.encrypted.pin";
    public static final String PREF_FINGERPRINT_SCAN = "aptatek.fingerprint.scan";
    public static final String KEY_PREF_ENCRYPTED_BIRTH_DATE = "aptatek.encrypted.birthdate";

    private final SharedPreferences sharedPreferences;

    @Inject
    public PreferenceManager(@ApplicationContext final Context applicationContext) {
        sharedPreferences = applicationContext.getSharedPreferences(PreferenceManager.Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setIncubationStart(final long timestamp) {
        sharedPreferences.edit().putLong(PREF_INCUBATION_START, timestamp).apply();
    }

    public long getIncubationStart() {
        return sharedPreferences.getLong(PREF_INCUBATION_START, 0L);
    }

    public void setEncryptedPin(final String encryptedPin) {
        sharedPreferences.edit().putString(PREF_ENCRYPTED_PIN, encryptedPin).apply();
    }

    public String getEncryptedPin() {
        return sharedPreferences.getString(PREF_ENCRYPTED_PIN, null);
    }

    public void enableFingerprintScan(final boolean enable) {
        sharedPreferences.edit().putBoolean(PREF_FINGERPRINT_SCAN, enable).apply();
    }

    public boolean isFingerprintScanEnabled() {
        return sharedPreferences.getBoolean(PREF_FINGERPRINT_SCAN, false);
    }

    public void setEncryptedBirthDate(final String encryptedBirthDate) {
        sharedPreferences.edit().putString(KEY_PREF_ENCRYPTED_BIRTH_DATE, encryptedBirthDate).apply();
    }

    @Nullable
    public String getEncryptedBirthDate() {
        return sharedPreferences.getString(KEY_PREF_ENCRYPTED_BIRTH_DATE, null);
    }

    public void clearPreference(final String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void clearAllPreference() {
        sharedPreferences.edit().clear().apply();
    }
}
