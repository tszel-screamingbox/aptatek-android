package com.aptatek.pkulab.data.datasource;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.wetting.WettingStatus;
import com.aptatek.pkulab.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
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
    public void testGetWettingStatusReturnsNotStartedWhenNoTestsWereRun() throws Exception {
        when(preferenceManager.getIncubationStart()).thenReturn(0L);

        assertTrue(wettingDataSource.getWettingStatus() == WettingStatus.NOT_STARTED);
    }

    @Test
    public void testGetWettingStatusReturnsRunningWhenTestIsRunning() throws Exception {
        when(preferenceManager.getWettingStart()).thenReturn(System.currentTimeMillis() - 2000L);

        assertTrue(wettingDataSource.getWettingStatus() == WettingStatus.RUNNING);
    }

    @Test
    public void testGetWettingStatusReturnsFinishedWhenWettingPeriodExpired() throws Exception {
        when(preferenceManager.getWettingStart()).thenReturn(System.currentTimeMillis() - Constants.DEFAULT_WETTING_PERIOD - 2000L); // expired 2 seconds ago

        assertTrue(wettingDataSource.getWettingStatus() == WettingStatus.FINISHED);
    }

    @Test
    public void testStartWetting() throws Exception {
        wettingDataSource.startWetting();

        verify(preferenceManager).setWettingStart(ArgumentMatchers.anyLong());
    }

    @Test
    public void testStopWetting() throws Exception {
        wettingDataSource.resetWetting();

        verify(preferenceManager).setWettingStart(ArgumentMatchers.anyLong());
    }

    @Test
    public void testGetWettingStart() throws Exception {
        final long testValue = 2L;
        when(preferenceManager.getWettingStart()).thenReturn(testValue);
        assert wettingDataSource.getWettingStart() == testValue;
    }

}
