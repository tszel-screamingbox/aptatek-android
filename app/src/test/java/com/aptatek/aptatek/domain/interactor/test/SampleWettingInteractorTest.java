package com.aptatek.aptatek.domain.interactor.test;

import com.aptatek.aptatek.domain.interactor.countdown.CountdownTimeFormatter;
import com.aptatek.aptatek.domain.interactor.samplewetting.SampleWettingInteractor;
import com.aptatek.aptatek.domain.interactor.samplewetting.WettingDataSource;
import com.aptatek.aptatek.domain.interactor.samplewetting.WettingError;
import com.aptatek.aptatek.domain.interactor.samplewetting.WettingNotRunningError;
import com.aptatek.aptatek.domain.model.Countdown;
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

public class SampleWettingInteractorTest {
    
    @Mock
    WettingDataSource wettingDataSource;
    
    @Mock
    CountdownTimeFormatter timeFormatter;
    
    private SampleWettingInteractor interactor;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        interactor = new SampleWettingInteractor(wettingDataSource, timeFormatter);
    }

    @Test
    public void testHasRunningWettingCallsDataSource() throws Exception {
        interactor.hasRunningWetting().test();
        verify(wettingDataSource).hasRunningWetting();
    }

    @Test
    public void testHasRunningWettingReturnsProperValue() throws Exception {
        final boolean testValue = true;

        when(wettingDataSource.hasRunningWetting()).thenReturn(testValue);
        final TestObserver<Boolean> test = interactor.hasRunningWetting().test();
        test.assertNoErrors();
        test.assertValue(testValue);
    }

    @Test
    public void testRunningWettingEmitsErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(wettingDataSource.hasRunningWetting()).thenThrow(testError);
        final TestObserver<Boolean> test = interactor.hasRunningWetting().test();
        test.assertError(testError);
    }

    @Test
    public void testStartWettingCallsDataSource() throws Exception {
        interactor.startWetting().test();
        verify(wettingDataSource).startWetting();
    }

    @Test
    public void testStartWettingTerminatesOnSuccess() throws Exception {
        final TestObserver<Void> test = interactor.startWetting().test();
        test.assertNoErrors();
        test.assertComplete();
    }

    @Test
    public void testStartWettingTerminatesWithErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        doThrow(testError).when(wettingDataSource).startWetting();
        final TestObserver<Void> test = interactor.startWetting().test();
        test.await();
        test.assertError(testError);
    }

    @Test
    public void testStopWettingCallsDataSource() throws Exception {
        interactor.stopWetting().test();
        verify(wettingDataSource).stopWetting();
    }

    @Test
    public void testStopWettingTerminatesOnSuccess() throws Exception {
        final TestObserver<Void> test = interactor.stopWetting().test();
        test.assertNoErrors();
        test.assertComplete();
    }

    @Test
    public void testStopWettingTerminatesWithErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        doThrow(testError).when(wettingDataSource).stopWetting();
        final TestObserver<Void> test = interactor.stopWetting().test();
        test.assertError(testError);
    }

    @Test
    public void testCountdownCallsDataSourceMethods() throws Exception {
        when(wettingDataSource.hasRunningWetting()).thenReturn(true);
        when(wettingDataSource.getWettingStart()).thenReturn(System.currentTimeMillis());

        interactor.getWettingCountdown().test();
        verify(wettingDataSource).hasRunningWetting();
        verify(wettingDataSource).getWettingStart();
    }

    @Test
    public void testCountdownTerminatesWhenNoWettingIsRunning() throws Exception {
        when(wettingDataSource.hasRunningWetting()).thenReturn(false);

        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        test.assertError(WettingNotRunningError.class);
    }

    @Test
    public void testCountdownTerminatesOnWettingFinish() throws Exception {
        final String testValue = "test";
        when(wettingDataSource.hasRunningWetting()).thenReturn(true);
        when(wettingDataSource.getWettingStart()).thenReturn(System.currentTimeMillis() - Constants.DEFAULT_WETTING_PERIOD + 200L);
        when(timeFormatter.getFormattedRemaining(ArgumentMatchers.anyLong())).thenReturn(testValue);

        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        test.await();
        test.assertNoErrors();
        test.assertComplete();
        test.assertValueAt(0, value -> value.getRemainingFormattedText().equals(testValue));
    }

    @Test
    public void testCountdownTerminatesOnHasRunningWettingException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(wettingDataSource.hasRunningWetting()).thenThrow(testError);

        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        test.assertError(WettingError.class);
    }

    @Test
    public void testCountdownTerminatesOnGetStartException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(wettingDataSource.hasRunningWetting()).thenReturn(true);
        when(wettingDataSource.getWettingStart()).thenThrow(testError);

        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        test.assertError(WettingError.class);
    }

    @Test
    public void testProgressDefault() throws Exception {
        final TestSubscriber<Integer> test = interactor.getWettingProgress().test();
        test.assertNoErrors();
        test.assertValueAt(0, 0);
    }

    @Test
    public void testProgressUpdatesOnWettingTick() throws Exception {
        final String testValue = "test";
        when(wettingDataSource.hasRunningWetting()).thenReturn(true);
        when(wettingDataSource.getWettingStart()).thenReturn(System.currentTimeMillis() - (Constants.DEFAULT_WETTING_PERIOD + 200L));
        when(timeFormatter.getFormattedRemaining(ArgumentMatchers.anyLong())).thenReturn(testValue);

        interactor.getWettingCountdown().test().awaitTerminalEvent();

        final TestSubscriber<Integer> test = interactor.getWettingProgress().test();
        test.assertNoErrors();
        test.assertValueAt(0, 0);
    }

}
