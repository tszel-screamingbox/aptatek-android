package com.aptatek.pkulab.domain.manager;

import android.support.test.runner.AndroidJUnit4;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.test.TestScreens;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
        assertNull(preferenceManager.getEncryptedPin());
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
        preferenceManager.setWettingStart(INCUBATION_START);
        preferenceManager.setParentalPassed(true);
        preferenceManager.setPkuRangeNormalFloor(Constants.DEFAULT_PKU_NORMAL_FLOOR);
        preferenceManager.setPkuRangeNormalCeil(Constants.DEFAULT_PKU_NORMAL_CEIL);
        preferenceManager.setPkuRangeUnit(Constants.DEFAULT_PKU_LEVEL_UNIT);
        preferenceManager.setRangeDialogShown(true);
        preferenceManager.setPairedDevice(TEST);
        preferenceManager.setTestStatus(TestScreens.BREAK_FOIL);

        preferenceManager.clearPreference(PreferenceManager.PREF_ENCRYPTED_PIN);
        preferenceManager.clearPreference(PreferenceManager.PREF_INCUBATION_START);
        preferenceManager.clearPreference(PreferenceManager.PREF_FINGERPRINT_SCAN);
        preferenceManager.clearPreference(PreferenceManager.PREF_WETTING_START);
        preferenceManager.clearPreference(PreferenceManager.PREF_PARENTAL_GATE_PASSED);
        preferenceManager.clearPreference(PreferenceManager.PREF_PKU_RANGE_NORMAL_CEIL);
        preferenceManager.clearPreference(PreferenceManager.PREF_PKU_RANGE_NORMAL_FLOOR);
        preferenceManager.clearPreference(PreferenceManager.PREF_PKU_RANGE_UNIT);
        preferenceManager.clearPreference(PreferenceManager.PREF_PKU_RANGE_DIALOG);
        preferenceManager.clearPreference(PreferenceManager.PREF_PAIRED);
        preferenceManager.clearPreference(PreferenceManager.PREF_TEST_STATUS);

        assertNull(preferenceManager.getEncryptedPin());
        assertEquals(preferenceManager.getIncubationStart(), 0L);
        assertFalse(preferenceManager.isFingerprintScanEnabled());
        assertEquals(preferenceManager.getWettingStart(), 0L);
        assertFalse(preferenceManager.isParentalPassed());
        assertEquals(preferenceManager.getPkuRangeNormalCeil(), -1f, 0.1f);
        assertEquals(preferenceManager.getPkuRangeNormalFloor(), -1f, 0.1f);
        assertNull(preferenceManager.getPkuRangeUnit());
        assertFalse(preferenceManager.isRangeDialogShown());
        assertNull(preferenceManager.getPairedDevice());
        assertEquals(preferenceManager.getTestStatus(), TestScreens.TURN_READER_ON);
    }

    @Test
    public void testClearAll() {
        preferenceManager.setIncubationStart(INCUBATION_START);
        preferenceManager.setEncryptedPin(TEST);
        preferenceManager.enableFingerprintScan(true);
        preferenceManager.setRangeDialogShown(true);

        preferenceManager.clearAllPreference();

        assertNull(preferenceManager.getEncryptedPin());
        assertEquals(preferenceManager.getIncubationStart(), 0L);
        assertTrue(!preferenceManager.isFingerprintScanEnabled());
        assertTrue(!preferenceManager.isRangeDialogShown());
    }

    @Test
    public void testSetRangeDialogShown() {
        preferenceManager.setRangeDialogShown(true);
        assertTrue(preferenceManager.isRangeDialogShown());
    }

    @Test
    public void testSetParentalPassed() {
        preferenceManager.setParentalPassed(true);
        assertTrue(preferenceManager.isParentalPassed());
    }

    @Test
    public void testWettingStartSet() {
        final long now = System.currentTimeMillis();
        preferenceManager.setWettingStart(now);
        assertEquals(preferenceManager.getWettingStart(), now);
    }

    @Test
    public void testWettingStartDefault() {
        assertEquals(preferenceManager.getWettingStart(), 0L);
    }

    @Test
    public void testPkuNormalFloorSet() {
        final float value = 310;
        preferenceManager.setPkuRangeNormalFloor(value);
        assertEquals(preferenceManager.getPkuRangeNormalFloor(), value, 0.1f);
    }

    @Test
    public void testPkuNormalFloorDefault() {
        assertEquals(preferenceManager.getPkuRangeNormalFloor(), -1f, 0.1f);
    }

    @Test
    public void testPkuNormalCeilSet() {
        final float value = 550;
        preferenceManager.setPkuRangeNormalCeil(value);
        assertEquals(preferenceManager.getPkuRangeNormalCeil(), value, 0.1f);
    }

    @Test
    public void testPkuNormalCeilDefault() {
        assertEquals(preferenceManager.getPkuRangeNormalCeil(), -1f, 0.1f);
    }

    @Test
    public void testPkuUnitSet() {
        final PkuLevelUnits milliGram = PkuLevelUnits.MILLI_GRAM;
        preferenceManager.setPkuRangeUnit(milliGram);
        assertEquals(preferenceManager.getPkuRangeUnit(), milliGram);
    }

    @Test
    public void testPkuUnitDefault() {
        assertNull(preferenceManager.getPkuRangeUnit());
    }

    @Test
    public void testGetPairedDeviceDefault() {
        assertNull(preferenceManager.getPairedDevice());
    }

    @Test
    public void testPairedDeviceSet() {
        preferenceManager.setPairedDevice(TEST);
        assertEquals(preferenceManager.getPairedDevice(), TEST);
    }

    @Test
    public void testTestStatusDefault() {
        assertEquals(preferenceManager.getTestStatus(), TestScreens.TURN_READER_ON);
    }

    @Test
    public void testTestStatusSet() {
        preferenceManager.setTestStatus(TestScreens.SELF_TEST);
        assertEquals(preferenceManager.getTestStatus(), TestScreens.SELF_TEST);
    }

}