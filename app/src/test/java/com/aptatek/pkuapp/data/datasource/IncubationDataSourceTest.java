package com.aptatek.pkuapp.data.datasource;

import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationStatus;
import com.aptatek.pkuapp.util.Constants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IncubationDataSourceTest {

    @Mock
    PreferenceManager preferenceManager;

    private IncubationDataSourceImpl incubationDataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        incubationDataSource = new IncubationDataSourceImpl(preferenceManager);
    }

    @Test
    public void testGetIncubationStatusReturnsNotStartedWhenNoTestsWereRun() throws Exception {
        when(preferenceManager.getIncubationStart()).thenReturn(0L);

        assertTrue(incubationDataSource.getIncubationStatus() == IncubationStatus.NOT_STARTED);
    }

    @Test
    public void testGetIncubationStatusReturnsRunningWhenTestIsRunning() throws Exception {
        when(preferenceManager.getIncubationStart()).thenReturn(System.currentTimeMillis() - 2000L);

        assertTrue(incubationDataSource.getIncubationStatus() == IncubationStatus.RUNNING);
    }

    @Test
    public void testGetIncubationStatusReturnsFinishedWhenIncubationPeriodExpired() throws Exception {
        when(preferenceManager.getIncubationStart()).thenReturn(System.currentTimeMillis() - Constants.DEFAULT_INCUBATION_PERIOD - 2000L); // expired 2 seconds ago

        assertTrue(incubationDataSource.getIncubationStatus() == IncubationStatus.FINISHED);
    }

    @Test
    public void testStartIncubation() throws Exception {
        incubationDataSource.startIncubation();

        verify(preferenceManager).setIncubationStart(ArgumentMatchers.anyLong());
    }

    @Test
    public void testStopIncubation() throws Exception {
        incubationDataSource.resetIncubation();

        verify(preferenceManager).setIncubationStart(ArgumentMatchers.anyLong());
    }

    @Test
    public void testGetIncubationStart() throws Exception {
        final long testValue = 2L;
        when(preferenceManager.getIncubationStart()).thenReturn(testValue);
        assert incubationDataSource.getIncubationStart() == testValue;
    }

}
