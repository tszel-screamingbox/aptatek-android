package com.aptatek.pkuapp.domain.interactor.test;

import com.aptatek.pkuapp.domain.interactor.countdown.CountdownTimeFormatter;
import com.aptatek.pkuapp.domain.interactor.samplewetting.SampleWettingInteractor;
import com.aptatek.pkuapp.domain.interactor.samplewetting.WettingDataSource;
import com.aptatek.pkuapp.domain.interactor.samplewetting.WettingError;
import com.aptatek.pkuapp.domain.interactor.samplewetting.WettingNotRunningError;
import com.aptatek.pkuapp.domain.interactor.samplewetting.WettingStatus;
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
    public void testGetWettingStatusCallsDataSource() throws Exception {
        interactor.getWettingStatus().test();
        verify(wettingDataSource).getWettingStatus();
    }

    @Test
    public void testGetWettingStatusReturnsProperValue() throws Exception {
        final WettingStatus testValue = WettingStatus.RUNNING;

        when(wettingDataSource.getWettingStatus()).thenReturn(testValue);
        final TestObserver<WettingStatus> test = interactor.getWettingStatus().test();
        test.assertNoErrors();
        test.assertValue(testValue);
    }

    @Test
    public void testRunningWettingEmitsErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(wettingDataSource.getWettingStatus()).thenThrow(testError);
        final TestObserver<WettingStatus> test = interactor.getWettingStatus().test();
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
        interactor.resetWetting().test();
        verify(wettingDataSource).resetWetting();
    }

    @Test
    public void testStopWettingTerminatesOnSuccess() throws Exception {
        final TestObserver<Void> test = interactor.resetWetting().test();
        test.assertNoErrors();
        test.assertComplete();
    }

    @Test
    public void testStopWettingTerminatesWithErrorOnException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        doThrow(testError).when(wettingDataSource).resetWetting();
        final TestObserver<Void> test = interactor.resetWetting().test();
        test.assertError(testError);
    }

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

    @Test
    public void testCountdownTerminatesWhenNoWettingIsRunning() throws Exception {
        when(wettingDataSource.getWettingStatus()).thenReturn(WettingStatus.NOT_STARTED);

        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        test.assertError(WettingNotRunningError.class);
    }

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

    @Test
    public void testCountdownTerminatesOnGetWettingStatusException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(wettingDataSource.getWettingStatus()).thenThrow(testError);

        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        test.assertError(WettingError.class);
    }

    @Test
    public void testCountdownTerminatesOnGetStartException() throws Exception {
        final Throwable testError = new RuntimeException("hello");
        when(wettingDataSource.getWettingStatus()).thenReturn(WettingStatus.RUNNING);
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
        when(wettingDataSource.getWettingStatus()).thenReturn(WettingStatus.RUNNING);
        when(wettingDataSource.getWettingStart()).thenReturn(System.currentTimeMillis() - (Constants.DEFAULT_WETTING_PERIOD + 200L));
        when(timeFormatter.getFormattedRemaining(ArgumentMatchers.anyLong())).thenReturn(testValue);

        interactor.getWettingCountdown().test().awaitTerminalEvent();

        final TestSubscriber<Integer> test = interactor.getWettingProgress().test();
        test.assertNoErrors();
        test.assertValueAt(0, 0);
    }

}
