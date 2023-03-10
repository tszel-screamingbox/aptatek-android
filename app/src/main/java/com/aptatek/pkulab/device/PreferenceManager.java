package com.aptatek.pkulab.device;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.aptatek.pkulab.view.test.TestScreens;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceManager {

    private static final class Constants {
        static final String SHARED_PREFERENCES_NAME = "com.aptatek.aptatek";
    }

    public static final String PREF_INCUBATION_START = "aptatek.test.incubation.start";
    public static final String PREF_WETTING_START = "aptatek.test.wetting.start";
    public static final String PREF_ENCRYPTED_PIN = "aptatek.encrypted.pin";
    public static final String PREF_FINGERPRINT_SCAN = "aptatek.fingerprint.scan";
    public static final String PREF_PARENTAL_GATE_PASSED = "aptatek.encrypted.parental.passed";
    public static final String PREF_PKU_RANGE_NORMAL_CEIL = "aptatek.range.normal.ceil";
    public static final String PREF_PKU_RANGE_NORMAL_FLOOR = "aptatek.range.normal.floor";
    public static final String PREF_PKU_RANGE_UNIT = "aptatek.range.unit";
    public static final String PREF_PAIRED = "aptatek.device.paired";

    public static final String PREF_PAIRED_ID = "aptatek.device.paired.id";
    public static final String PREF_TEST_STATUS = "aptatek.test.status";
    public static final String PREF_TEST_UNFINISHED = "aptatek.test.unfinished";
    public static final String PREF_DB_ENCRYPTED_WITH_PIN = "aptatek.database.encrypted";
    public static final String PREF_DB_CYPHER_UPDATED = "aptatek.database.cypher.upadted";
    public static final String PREF_APP_KILLED_DURING_TEST = "aptatek.test.app.killed.during.test.timestamp";
    public static final String PREF_TEST_START = "aptatek.test.flow.start";

    private final SharedPreferences sharedPreferences;

    @Inject
    public PreferenceManager(@ApplicationContext final Context applicationContext) {
        sharedPreferences = applicationContext.getSharedPreferences(PreferenceManager.Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setAppKilledTimestamp(final long timestamp) {
        sharedPreferences.edit().putLong(PREF_APP_KILLED_DURING_TEST, timestamp).apply();
    }

    public long getAppKilledTimestamp() {
        return sharedPreferences.getLong(PREF_APP_KILLED_DURING_TEST, 0L);
    }

    public void setTestContinueStatus(final boolean status) {
        sharedPreferences.edit().putBoolean(PREF_TEST_UNFINISHED, status).apply();
    }

    public boolean isTestContinueNeed() {
        return sharedPreferences.getBoolean(PREF_TEST_UNFINISHED, false);
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

    public void setParentalPassed(final boolean parentalPassed) {
        sharedPreferences.edit().putBoolean(PREF_PARENTAL_GATE_PASSED, parentalPassed).apply();
    }

    public boolean isParentalPassed() {
        return sharedPreferences.getBoolean(PREF_PARENTAL_GATE_PASSED, false);
    }

    public void setWettingStart(final long timestamp) {
        sharedPreferences.edit().putLong(PREF_WETTING_START, timestamp).apply();
    }

    public long getWettingStart() {
        return sharedPreferences.getLong(PREF_WETTING_START, 0L);
    }

    public void setPkuRangeNormalCeil(final float value) {
        sharedPreferences.edit().putFloat(PREF_PKU_RANGE_NORMAL_CEIL, value).apply();
    }

    public float getPkuRangeNormalCeil() {
        return sharedPreferences.getFloat(PREF_PKU_RANGE_NORMAL_CEIL, -1f);
    }

    public void setPkuRangeNormalFloor(final float value) {
        sharedPreferences.edit().putFloat(PREF_PKU_RANGE_NORMAL_FLOOR, value).apply();
    }

    public float getPkuRangeNormalFloor() {
        return sharedPreferences.getFloat(PREF_PKU_RANGE_NORMAL_FLOOR, -1f);
    }

    public void setPkuRangeUnit(final PkuLevelUnits unit) {
        sharedPreferences.edit().putInt(PREF_PKU_RANGE_UNIT, unit.ordinal()).apply();
    }

    public PkuLevelUnits getPkuRangeUnit() {
        final int ordinal = sharedPreferences.getInt(PREF_PKU_RANGE_UNIT, -1);
        return ordinal != -1 ? PkuLevelUnits.values()[ordinal] : null;
    }

    @Nullable
    public String getPairedDevice() {
        return sharedPreferences.getString(PREF_PAIRED, null);
    }

    @Nullable
    public String getPairedDeviceName() {
        return sharedPreferences.getString(PREF_PAIRED_ID, null);
    }

    public void setPairedDevice(@Nullable final String device) {
        sharedPreferences.edit().putString(PREF_PAIRED, device).apply();
    }

    public void setPairedDeviceName(@Nullable final String deviceId) {
        sharedPreferences.edit().putString(PREF_PAIRED_ID, deviceId).apply();
    }

    public TestScreens getTestStatus() {
        final int screenIndex = sharedPreferences.getInt(PREF_TEST_STATUS, -1);
        final TestScreens[] testScreens = TestScreens.values();

        if (screenIndex >= 0 && screenIndex < testScreens.length) {
            return testScreens[screenIndex];
        }

        throw new IllegalStateException("No running test!");
    }

    public void setTestStatus(final TestScreens testStatus) {
        sharedPreferences.edit().putInt(PREF_TEST_STATUS, testStatus.ordinal()).apply();
    }

    public boolean isDbEncryptedWithPin() {
        return sharedPreferences.getBoolean(PREF_DB_ENCRYPTED_WITH_PIN, false);
    }

    public void setPrefDbEncryptedWithPin() {
        sharedPreferences.edit().putBoolean(PREF_DB_ENCRYPTED_WITH_PIN, true).apply();
    }

    public boolean isDbCypherUpdated() {
        return sharedPreferences.getBoolean(PREF_DB_CYPHER_UPDATED, false);
    }

    public void setPrefDbCypherUpdated() {
        sharedPreferences.edit().putBoolean(PREF_DB_CYPHER_UPDATED, true).apply();
    }

    public void setTestFlowStart() {
        sharedPreferences.edit().putLong(PREF_TEST_START, System.currentTimeMillis()).apply();
    }

    public long getTestFlowStart() {
        return sharedPreferences.getLong(PREF_TEST_START, 0L);
    }

    public void clearPreference(final String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void clearAllPreference() {
        sharedPreferences.edit().clear().apply();
    }
}
