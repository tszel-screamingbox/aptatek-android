package com.aptatek.aptatek.domain.interactor.test;

import com.aptatek.aptatek.domain.interactor.incubation.IncubationDataSource;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationError;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationNotRunningError;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationTimeFormatter;
import com.aptatek.aptatek.domain.model.IncubationCountdown;
import com.aptatek.aptatek.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IncubationInteractorTest {

    private IncubationInteractor incubationInteractor;

    @Mock
    IncubationDataSource incubationDataSource;

    @Mock
    IncubationTimeFormatter incubationTimeFormatter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        incubationInteractor = new IncubationInteractor(incubationDataSource, incubationTimeFormatter);
    }

    @Test
    public void testHasRunningIncubationCallsDataSource() throws Exception {
        incubationInteractor.hasRunningIncubation().test();
        verify(incubationDataSource).hasRunningIncubation();
    }

    @Test
    public void testHasRunningIncubationReturnsProperValue() throws Exception {
        final boolean testValue = true;

        when(incubationDataSource.hasRunningIncubation()).thenReturn(testValue);
        final TestObserver<Boolean> test = incubationInteractor.hasRunningIncubation().test();
        test.assertNoErrors();
        test.assertValue(testValue);
    }

    @Test
    public void testRunningIncubationEmitsErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(incubationDataSource.hasRunningIncubation()).thenThrow(testError);
        final TestObserver<Boolean> test = incubationInteractor.hasRunningIncubation().test();
        test.assertError(testError);
    }

    @Test
    public void testStartIncubationCallsDataSource() throws Exception {
        incubationInteractor.startIncubation().test();
        verify(incubationDataSource).startIncubation();
    }

    @Test
    public void testStartIncubationTerminatesOnSuccess() throws Exception {
        final TestObserver<Void> test = incubationInteractor.startIncubation().test();
        test.assertNoErrors();
        test.assertComplete();
    }

    @Test
    public void testStartIncubationTerminatesWithErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        doThrow(testError).when(incubationDataSource).startIncubation();
        final TestObserver<Void> test = incubationInteractor.startIncubation().test();
        test.await();
        test.assertError(testError);
    }

    @Test
    public void testStopIncubationCallsDataSource() throws Exception {
        incubationInteractor.stopIncubation().test();
        verify(incubationDataSource).stopIncubation();
    }

    @Test
    public void testStopIncubationTerminatesOnSuccess() throws Exception {
        final TestObserver<Void> test = incubationInteractor.stopIncubation().test();
        test.assertNoErrors();
        test.assertComplete();
    }

    @Test
    public void testStopIncubationTerminatesWithErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        doThrow(testError).when(incubationDataSource).stopIncubation();
        final TestObserver<Void> test = incubationInteractor.stopIncubation().test();
        test.assertError(testError);
    }

    @Test
    public void testCountdownCallsDataSourceMethods() throws Exception {
        when(incubationDataSource.hasRunningIncubation()).thenReturn(true);
        when(incubationDataSource.getIncubationStart()).thenReturn(System.currentTimeMillis());

        incubationInteractor.getIncubationCountdown().test();
        verify(incubationDataSource).hasRunningIncubation();
        verify(incubationDataSource).getIncubationStart();
    }

    @Test
    public void testCountdownTerminatesWhenNoIncubationIsRunning() throws Exception {
        when(incubationDataSource.hasRunningIncubation()).thenReturn(false);

        final TestSubscriber<IncubationCountdown> test = incubationInteractor.getIncubationCountdown().test();
        test.assertError(IncubationNotRunningError.class);
    }

    @Test
    public void testCountdownTerminatesOnIncubationFinish() throws Exception {
        final String testValue = "test";
        when(incubationDataSource.hasRunningIncubation()).thenReturn(true);
        when(incubationDataSource.getIncubationStart()).thenReturn(System.currentTimeMillis() - Constants.DEFAULT_INCUBATION_PERIOD + 200L);
        when(incubationTimeFormatter.getFormattedRemaining(ArgumentMatchers.anyLong())).thenReturn(testValue);

        final TestSubscriber<IncubationCountdown> test = incubationInteractor.getIncubationCountdown().test();
        test.await();
        test.assertNoErrors();
        test.assertComplete();
        test.assertValueAt(0, value -> value.getRemainingFormattedText().equals(testValue));
    }

    @Test
    public void testCountdownTerminatesOnHasRunningIncubationException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(incubationDataSource.hasRunningIncubation()).thenThrow(testError);

        final TestSubscriber<IncubationCountdown> test = incubationInteractor.getIncubationCountdown().test();
        test.assertError(IncubationError.class);
    }

    @Test
    public void testCountdownTerminatesOnGetStartException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(incubationDataSource.hasRunningIncubation()).thenReturn(true);
        when(incubationDataSource.getIncubationStart()).thenThrow(testError);

        final TestSubscriber<IncubationCountdown> test = incubationInteractor.getIncubationCountdown().test();
        test.assertError(IncubationError.class);
    }

}
