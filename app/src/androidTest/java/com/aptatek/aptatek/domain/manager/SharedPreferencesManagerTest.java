package com.aptatek.aptatek.domain.manager;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SharedPreferencesManagerTest {

    private SharedPreferencesManager sharedPreferencesManager;
    private static final String TEST = "test_text";

    @Before
    public void setUp() {
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
    }

    @Test
    public void testPersist() {
        sharedPreferencesManager.setEncryptedPin(TEST);
        assertEquals(sharedPreferencesManager.getEncryptedPin(), TEST);
    }

    @Test
    public void testGetDefaultValue() {
        assertEquals(sharedPreferencesManager.getEncryptedPin(), null);
    }

    @Test
    public void testClearByKey() {
        sharedPreferencesManager.setEncryptedPin(TEST);
        sharedPreferencesManager.clearPreference(SharedPreferencesManager.PREF_PARAM_ENCRYPTED_PIN);
        assertEquals(sharedPreferencesManager.getEncryptedPin(), null);
    }

    @Test
    public void testClearAll() {
        sharedPreferencesManager.setEncryptedPin(TEST);
        sharedPreferencesManager.clearAllPreference();
        assertEquals(sharedPreferencesManager.getEncryptedPin(), null);
    }
}