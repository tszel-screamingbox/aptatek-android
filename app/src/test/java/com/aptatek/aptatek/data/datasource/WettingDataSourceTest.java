package com.aptatek.aptatek.data.datasource;

import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WettingDataSourceTest {

    @Mock
    PreferenceManager preferenceManager;

    private WettingDataSourceImpl wettingDataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        wettingDataSource = new WettingDataSourceImpl(preferenceManager);
    }

    @Test
    public void testHasRunningIncubationReturnsFalseWhenNoTestsWereRun() throws Exception {
        when(preferenceManager.getIncubationStart()).thenReturn(0L);

        assert !wettingDataSource.hasRunningWetting();
    }

    @Test
    public void testHasRunningWettingReturnsTrueWhenTestIsRunning() throws Exception {
        when(preferenceManager.getWettingStart()).thenReturn(System.currentTimeMillis() - 2000L);

        assert wettingDataSource.hasRunningWetting();
    }

    @Test
    public void testHasRunningWettingReturnsFalseWhenWettingPeriodExpired() throws Exception {
        when(preferenceManager.getWettingStart()).thenReturn(System.currentTimeMillis() - Constants.DEFAULT_WETTING_PERIOD - 2000L); // expired 2 seconds ago

        assert !wettingDataSource.hasRunningWetting();
    }

    @Test
    public void testStartWetting() throws Exception {
        wettingDataSource.startWetting();

        verify(preferenceManager).setWettingStart(ArgumentMatchers.anyLong());
    }

    @Test
    public void testStopWetting() throws Exception {
        wettingDataSource.stopWetting();

        verify(preferenceManager).setWettingStart(ArgumentMatchers.anyLong());
    }

    @Test
    public void testGetWettingStart() throws Exception {
        final long testValue = 2L;
        when(preferenceManager.getWettingStart()).thenReturn(testValue);
        assert wettingDataSource.getWettingStart() == testValue;
    }

}
