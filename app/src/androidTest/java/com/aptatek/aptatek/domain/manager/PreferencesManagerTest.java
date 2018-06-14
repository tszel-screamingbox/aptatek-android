package com.aptatek.aptatek.domain.manager;

import android.support.test.runner.AndroidJUnit4;

import com.aptatek.aptatek.device.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PreferencesManagerTest {

    private PreferenceManager sharedPreferencesManager;

    private static final String TEST = "test_text";
    private static final long INCUBATION_START = 100;

    @Before
    public void setUp() {
        sharedPreferencesManager = new PreferenceManager(getContext());
    }

    @Test
    public void testPersistEnryptPin() {
        sharedPreferencesManager.setEncryptedPin(TEST);
        assertEquals(sharedPreferencesManager.getEncryptedPin(), TEST);
    }

    @Test
    public void testGetDefaultValue() {
        assertEquals(sharedPreferencesManager.getEncryptedPin(), null);
    }

    @Test
    public void testPersistIncubationStart() {
        sharedPreferencesManager.setIncubationStart(INCUBATION_START);
        assertEquals(sharedPreferencesManager.getIncubationStart(), INCUBATION_START);
    }


    @Test
    public void testClearByKey() {
        sharedPreferencesManager.setEncryptedPin(TEST);
        sharedPreferencesManager.setIncubationStart(INCUBATION_START);
        sharedPreferencesManager.clearPreference(PreferenceManager.PREF_ENCRYPTED_PIN);
        sharedPreferencesManager.clearPreference(PreferenceManager.PREF_INCUBATION_START);
        assertEquals(sharedPreferencesManager.getEncryptedPin(), null);
        assertEquals(sharedPreferencesManager.getIncubationStart(), 0L);
    }

    @Test
    public void testClearAll() {
        sharedPreferencesManager.setIncubationStart(INCUBATION_START);
        sharedPreferencesManager.setEncryptedPin(TEST);
        sharedPreferencesManager.clearAllPreference();
        assertEquals(sharedPreferencesManager.getEncryptedPin(), null);
        assertEquals(sharedPreferencesManager.getIncubationStart(), 0L);
    }
}