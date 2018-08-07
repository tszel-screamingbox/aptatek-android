package com.aptatek.pkuapp.data.datasource;

import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    public void testHasRunningIncubationReturnsFalseWhenNoTestsWereRun() throws Exception {
        when(preferenceManager.getIncubationStart()).thenReturn(0L);

        assert !incubationDataSource.hasRunningIncubation();
    }

    @Test
    public void testHasRunningIncubationReturnsTrueWhenTestIsRunning() throws Exception {
        when(preferenceManager.getIncubationStart()).thenReturn(System.currentTimeMillis() - 2000L);

        assert incubationDataSource.hasRunningIncubation();
    }

    @Test
    public void testHasRunningIncubationReturnsFalseWhenIncubationPeriodExpired() throws Exception {
        when(preferenceManager.getIncubationStart()).thenReturn(System.currentTimeMillis() - Constants.DEFAULT_INCUBATION_PERIOD - 2000L); // expired 2 seconds ago

        assert !incubationDataSource.hasRunningIncubation();
    }

    @Test
    public void testStartIncubation() throws Exception {
        incubationDataSource.startIncubation();

        verify(preferenceManager).setIncubationStart(ArgumentMatchers.anyLong());
    }

    @Test
    public void testStopIncubation() throws Exception {
        incubationDataSource.stopIncubation();

        verify(preferenceManager).setIncubationStart(ArgumentMatchers.anyLong());
    }

    @Test
    public void testGetIncubationStart() throws Exception {
        final long testValue = 2L;
        when(preferenceManager.getIncubationStart()).thenReturn(testValue);
        assert incubationDataSource.getIncubationStart() == testValue;
    }

}
