package com.aptatek.aptatek.domain.manager;

import android.support.test.runner.AndroidJUnit4;

import com.aptatek.aptatek.device.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PreferencesManagerTest {

    private PreferenceManager preferenceManager;

    private static final String TEST = "test_text";
    private static final long INCUBATION_START = 100;

    @Before
    public void setUp() {
        preferenceManager = new PreferenceManager(getContext());
    }

    @Test
    public void testPersistEnryptPin() {
        preferenceManager.setEncryptedPin(TEST);
        assertEquals(preferenceManager.getEncryptedPin(), TEST);
    }

    @Test
    public void testGetDefaultValue() {
        assertEquals(preferenceManager.getEncryptedPin(), null);
    }

    @Test
    public void testPersistIncubationStart() {
        preferenceManager.setIncubationStart(INCUBATION_START);
        assertEquals(preferenceManager.getIncubationStart(), INCUBATION_START);
    }

    @Test
    public void testEnableFingerprint() {
        preferenceManager.enableFingerprintScan(true);
        assertTrue(preferenceManager.isFingerprintScanEnabled());
    }

    @Test
    public void testDisableFingerprint() {
        preferenceManager.enableFingerprintScan(false);
        assertTrue(!preferenceManager.isFingerprintScanEnabled());
    }

    @Test
    public void testClearByKey() {
        preferenceManager.setEncryptedPin(TEST);
        preferenceManager.setIncubationStart(INCUBATION_START);
        preferenceManager.enableFingerprintScan(true);

        preferenceManager.clearPreference(PreferenceManager.PREF_ENCRYPTED_PIN);
        preferenceManager.clearPreference(PreferenceManager.PREF_INCUBATION_START);
        preferenceManager.clearPreference(PreferenceManager.PREF_FINGERPRINT_SCAN);

        assertEquals(preferenceManager.getEncryptedPin(), null);
        assertEquals(preferenceManager.getIncubationStart(), 0L);
        assertTrue(!preferenceManager.isFingerprintScanEnabled());
    }

    @Test
    public void testClearAll() {
        preferenceManager.setIncubationStart(INCUBATION_START);
        preferenceManager.setEncryptedPin(TEST);
        preferenceManager.enableFingerprintScan(true);

        preferenceManager.clearAllPreference();

        assertEquals(preferenceManager.getEncryptedPin(), null);
        assertEquals(preferenceManager.getIncubationStart(), 0L);
        assertTrue(!preferenceManager.isFingerprintScanEnabled());
    }
}