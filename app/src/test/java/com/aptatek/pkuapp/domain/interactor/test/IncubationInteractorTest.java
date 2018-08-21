package com.aptatek.pkuapp.domain.interactor.test;

import com.aptatek.pkuapp.domain.interactor.countdown.CountdownTimeFormatter;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationDataSource;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationError;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationNotRunningError;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationStatus;
import com.aptatek.pkuapp.domain.model.Countdown;
import com.aptatek.pkuapp.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IncubationInteractorTest {

    private IncubationInteractor incubationInteractor;

    @Mock
    IncubationDataSource incubationDataSource;

    @Mock
    CountdownTimeFormatter countdownTimeFormatter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        incubationInteractor = new IncubationInteractor(incubationDataSource, countdownTimeFormatter);
    }

    @Test
    public void testHasRunningIncubationCallsDataSource() throws Exception {
        incubationInteractor.getIncubationStatus().test();
        verify(incubationDataSource).getIncubationStatus();
    }

    @Test
    public void testGetIncubationStatusReturnsProperValue() throws Exception {
        final IncubationStatus testValue = IncubationStatus.RUNNING;

        when(incubationDataSource.getIncubationStatus()).thenReturn(testValue);
        final TestObserver<IncubationStatus> test = incubationInteractor.getIncubationStatus().test();
        test.assertNoErrors();
        test.assertValue(testValue);
    }

    @Test
    public void testGetIncubationStatusEmitsErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(incubationDataSource.getIncubationStatus()).thenThrow(testError);
        final TestObserver<IncubationStatus> test = incubationInteractor.getIncubationStatus().test();
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
        incubationInteractor.resetIncubation().test();
        verify(incubationDataSource).resetIncubation();
    }

    @Test
    public void testStopIncubationTerminatesOnSuccess() throws Exception {
        final TestObserver<Void> test = incubationInteractor.resetIncubation().test();
        test.assertNoErrors();
        test.assertComplete();
    }

    @Test
    public void testStopIncubationTerminatesWithErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        doThrow(testError).when(incubationDataSource).resetIncubation();
        final TestObserver<Void> test = incubationInteractor.resetIncubation().test();
        test.assertError(testError);
    }

    @Test
    public void testCountdownCallsDataSourceMethods() throws Exception {
        when(incubationDataSource.getIncubationStatus()).thenReturn(IncubationStatus.RUNNING);
        doReturn(System.currentTimeMillis() - 1000L).when(incubationDataSource).getIncubationStart();

        final TestSubscriber<Countdown> test = incubationInteractor.getIncubationCountdown().test();
        test.assertNoErrors();
        test.assertNotComplete();

        Thread.sleep(1000L);

        verify(incubationDataSource).getIncubationStatus();
        verify(incubationDataSource).getIncubationStart();
    }

    @Test
    public void testCountdownTerminatesWhenNoIncubationIsRunning() throws Exception {
        when(incubationDataSource.getIncubationStatus()).thenReturn(IncubationStatus.NOT_STARTED);

        final TestSubscriber<Countdown> test = incubationInteractor.getIncubationCountdown().test();
        test.assertError(IncubationNotRunningError.class);
    }

    @Test
    public void testCountdownTerminatesOnIncubationFinish() throws Exception {
        final String testValue = "test";
        when(incubationDataSource.getIncubationStatus()).thenReturn(IncubationStatus.RUNNING);
        doReturn(System.currentTimeMillis() - Constants.DEFAULT_INCUBATION_PERIOD + 200L).when(incubationDataSource).getIncubationStart();
        doReturn(testValue).when(countdownTimeFormatter).getFormattedRemaining(ArgumentMatchers.anyLong());

        final TestSubscriber<Countdown> test = incubationInteractor.getIncubationCountdown().test();
        test.await();
        test.assertNoErrors();
        test.assertComplete();
        test.assertValueAt(0, value -> value.getRemainingFormattedText().equals(testValue));
    }

    @Test
    public void testCountdownTerminatesOnHasRunningIncubationException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(incubationDataSource.getIncubationStatus()).thenThrow(testError);

        final TestSubscriber<Countdown> test = incubationInteractor.getIncubationCountdown().test();
        test.assertError(IncubationError.class);
    }

    @Test
    public void testCountdownTerminatesOnGetStartException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(incubationDataSource.getIncubationStatus()).thenReturn(IncubationStatus.RUNNING);
        when(incubationDataSource.getIncubationStart()).thenThrow(testError);

        final TestSubscriber<Countdown> test = incubationInteractor.getIncubationCountdown().test();
        test.assertError(IncubationError.class);
    }

}
