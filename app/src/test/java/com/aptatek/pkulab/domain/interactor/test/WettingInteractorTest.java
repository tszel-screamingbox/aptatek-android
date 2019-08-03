package com.aptatek.pkulab.domain.interactor.test;

import com.aptatek.pkulab.domain.interactor.countdown.CountdownTimeFormatter;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingDataSource;
import com.aptatek.pkulab.domain.interactor.wetting.WettingError;
import com.aptatek.pkulab.domain.interactor.wetting.WettingNotRunningError;
import com.aptatek.pkulab.domain.interactor.wetting.WettingStatus;
import com.aptatek.pkulab.domain.model.Countdown;
import com.aptatek.pkulab.util.Constants;

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
 * Tests for the WettingInteractor class
 *
 * @test.layer domain
 * @test.feature SampleWetting
 * @test.type unit
 */
public class WettingInteractorTest {
    
    @Mock
    WettingDataSource wettingDataSource;
    
    @Mock
    CountdownTimeFormatter timeFormatter;
    
    private WettingInteractor interactor;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        interactor = new WettingInteractor(wettingDataSource, timeFormatter, analyticsManager);
    }

    /**
     * Tests the proper behavior: the getWettingStatus() method should rely on the DataSource to get the wetting status.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testGetWettingStatusCallsDataSource() throws Exception {
        interactor.getWettingStatus().test();
        verify(wettingDataSource).getWettingStatus();
    }

    /**
     * Tests the proper behavior: when the DataSource returns valid status, the interactor's getWettingStatus() should emit the same value without error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testGetWettingStatusReturnsProperValue() throws Exception {
        final WettingStatus testValue = WettingStatus.RUNNING;

        when(wettingDataSource.getWettingStatus()).thenReturn(testValue);
        final TestObserver<WettingStatus> test = interactor.getWettingStatus().test();
        test.assertNoErrors();
        test.assertValue(testValue);
    }

    /**
     * Tests the proper behavior: when the DataSource throws exception, the interactor's getWettingStatus() should signal an exception.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testRunningWettingEmitsErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(wettingDataSource.getWettingStatus()).thenThrow(testError);
        final TestObserver<WettingStatus> test = interactor.getWettingStatus().test();
        test.assertError(testError);
    }

    /**
     * Tests the proper behavior: the startWetting() method should be relayed to the DataSource.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStartWettingCallsDataSource() throws Exception {
        interactor.startWetting().test();
        verify(wettingDataSource).startWetting();
    }

    /**
     * Tests the proper behavior: when the DataSource's startWetting() method runs without error, the interactor's stream should terminate without error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStartWettingTerminatesOnSuccess() throws Exception {
        final TestObserver<Void> test = interactor.startWetting().test();
        test.assertNoErrors();
        test.assertComplete();
    }

    /**
     * Tests the proper behavior: when the DataSource's startWetting() method throws exception, the interactor's stream should signal an error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStartWettingTerminatesWithErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        doThrow(testError).when(wettingDataSource).startWetting();
        final TestObserver<Void> test = interactor.startWetting().test();
        test.await();
        test.assertError(testError);
    }

    /**
     * Tests the proper behavior: the resetWetting() method should be relayed to the DataSource.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStopWettingCallsDataSource() throws Exception {
        interactor.resetWetting().test();
        verify(wettingDataSource).resetWetting();
    }

    /**
     * Tests the proper behavior: when the DataSource's resetWetting() method runs without error, the interactor's stream should terminate without error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStopWettingTerminatesOnSuccess() throws Exception {
        final TestObserver<Void> test = interactor.resetWetting().test();
        test.assertNoErrors();
        test.assertComplete();
    }

    /**
     * Tests the proper behavior: when the DataSource's resetWetting() method throws exception, the interactor's stream should signal error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testStopWettingTerminatesWithErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        doThrow(testError).when(wettingDataSource).resetWetting();
        final TestObserver<Void> test = interactor.resetWetting().test();
        test.assertError(testError);
    }

    /**
     * Tests the proper behavior: the getWettingCountdown() method should rely on the DataSource.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCountdownCallsDataSourceMethods() throws Exception {
        when(wettingDataSource.getWettingStatus()).thenReturn(WettingStatus.RUNNING);
        when(wettingDataSource.getWettingStart()).thenReturn(System.currentTimeMillis() - 1000L);

        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        test.assertNoErrors();
        test.assertNotComplete();

        Thread.sleep(1000L);

        verify(wettingDataSource).getWettingStatus();
        verify(wettingDataSource).getWettingStart();
    }

    /**
     * Tests the proper behavior: when there is no wetting in progress, the getWettingCountdown() should signal error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCountdownTerminatesWhenNoWettingIsRunning() throws Exception {
        when(wettingDataSource.getWettingStatus()).thenReturn(WettingStatus.NOT_STARTED);

        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        test.assertError(WettingNotRunningError.class);
    }

    /**
     * Tests the proper behavior: when the wetting is finally ready, the getWettingCountdown() should terminate without error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCountdownTerminatesOnWettingFinish() throws Exception {
        final String testValue = "test";
        when(wettingDataSource.getWettingStatus()).thenReturn(WettingStatus.RUNNING);
        when(wettingDataSource.getWettingStart()).thenReturn(System.currentTimeMillis() - Constants.DEFAULT_WETTING_PERIOD + 200L);
        doReturn(testValue).when(timeFormatter).getFormattedRemaining(ArgumentMatchers.anyLong());

        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        test.await();
        test.assertNoErrors();
        test.assertComplete();
        test.assertValueAt(0, value -> value.getRemainingFormattedText().equals(testValue));
    }

    /**
     * Tests the proper behavior: when the DataSource throws exception during wetting, the getWettingCountdown() should signal an error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCountdownTerminatesOnGetWettingStatusException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(wettingDataSource.getWettingStatus()).thenThrow(testError);

        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        test.assertError(WettingError.class);
    }

    /**
     * Tests the proper behavior: when the DataSource throws exception during wetting, the getWettingCountdown() should signal an error.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCountdownTerminatesOnGetStartException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(wettingDataSource.getWettingStatus()).thenReturn(WettingStatus.RUNNING);
        when(wettingDataSource.getWettingStart()).thenThrow(testError);

        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        test.assertError(WettingError.class);
    }

}
