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

/**
 * Tests for the IncubationInteractor class.
 *
 * @test.layer domain
 * @test.feature BloodIsProcessing
 * @test.type unit
 */
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

    /**
     * Tests the proper behavior: the getIncubationStatus() should get its data from the DataSource.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testHasRunningIncubationCallsDataSource() throws Exception {
        incubationInteractor.getIncubationStatus().test();
        verify(incubationDataSource).getIncubationStatus();
    }

    /**
     * Tests the proper behavior: when the DataSource returns valid data, the getIncubationStatus() should emit the data without error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testGetIncubationStatusReturnsProperValue() throws Exception {
        final IncubationStatus testValue = IncubationStatus.RUNNING;

        when(incubationDataSource.getIncubationStatus()).thenReturn(testValue);
        final TestObserver<IncubationStatus> test = incubationInteractor.getIncubationStatus().test();
        test.assertNoErrors();
        test.assertValue(testValue);
    }

    /**
     * Tests the proper behavior: when the DataSource throws exception, the getIncubationStatus() should signal an error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testGetIncubationStatusEmitsErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(incubationDataSource.getIncubationStatus()).thenThrow(testError);
        final TestObserver<IncubationStatus> test = incubationInteractor.getIncubationStatus().test();
        test.assertError(testError);
    }

    /**
     * Tests the proper behavior: the startIncubation() should be relayed to DataSource.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStartIncubationCallsDataSource() throws Exception {
        incubationInteractor.startIncubation().test();
        verify(incubationDataSource).startIncubation();
    }

    /**
     * Tests the proper behavior: when the DataSource successfully starts incubation, the startIncubation() stream should terminate without error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStartIncubationTerminatesOnSuccess() throws Exception {
        final TestObserver<Void> test = incubationInteractor.startIncubation().test();
        test.assertNoErrors();
        test.assertComplete();
    }

    /**
     * Tests the proper behavior: when the DataSource throws exception on incubation start, the startIncubation() should signal an error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStartIncubationTerminatesWithErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        doThrow(testError).when(incubationDataSource).startIncubation();
        final TestObserver<Void> test = incubationInteractor.startIncubation().test();
        test.await();
        test.assertError(testError);
    }

    /**
     * Tests the proper behavior: the resetIncubation() should be relayed to DataSource.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStopIncubationCallsDataSource() throws Exception {
        incubationInteractor.resetIncubation().test();
        verify(incubationDataSource).resetIncubation();
    }

    /**
     * Tests the proper behavior: when the DataSource successfully resets incubation, the resetIncubation() should terminate without error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStopIncubationTerminatesOnSuccess() throws Exception {
        final TestObserver<Void> test = incubationInteractor.resetIncubation().test();
        test.assertNoErrors();
        test.assertComplete();
    }

    /**
     * Tests the proper behavior: when the DataSource throws exception on reset incubation, the resetIncubation() should signal an error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStopIncubationTerminatesWithErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        doThrow(testError).when(incubationDataSource).resetIncubation();
        final TestObserver<Void> test = incubationInteractor.resetIncubation().test();
        test.assertError(testError);
    }

    /**
     * Tests the proper behavior: getIncubationCountdown() should get its data from DataSource.
     *
     * @test.input
     * @test.expected
     */
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

    /**
     * Tests the proper behavior: when the incubation is not yet started, the getIncubationStatus() should signal error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCountdownTerminatesWhenNoIncubationIsRunning() throws Exception {
        when(incubationDataSource.getIncubationStatus()).thenReturn(IncubationStatus.NOT_STARTED);

        final TestSubscriber<Countdown> test = incubationInteractor.getIncubationCountdown().test();
        test.assertError(IncubationNotRunningError.class);
    }

    /**
     * Tests the proper behavior: the incubation finally finishes, the getIncubationCountdown() should also finish without error.
     *
     * @test.input
     * @test.expected
     */
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

    /**
     * Tests the proper behavior: when an exception occurrs during incubation, the getIncubationCountdown() should signal an error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCountdownTerminatesOnHasRunningIncubationException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(incubationDataSource.getIncubationStatus()).thenThrow(testError);

        final TestSubscriber<Countdown> test = incubationInteractor.getIncubationCountdown().test();
        test.assertError(IncubationError.class);
    }

    /**
     * Tests the proper behavior: when an exception occurrs during incubation, the getIncubationCountdown() should signal an error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCountdownTerminatesOnGetStartException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(incubationDataSource.getIncubationStatus()).thenReturn(IncubationStatus.RUNNING);
        when(incubationDataSource.getIncubationStart()).thenThrow(testError);

        final TestSubscriber<Countdown> test = incubationInteractor.getIncubationCountdown().test();
        test.assertError(IncubationError.class);
    }

}
