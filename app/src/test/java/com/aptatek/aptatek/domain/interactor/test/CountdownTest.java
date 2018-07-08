package com.aptatek.aptatek.domain.interactor.test;

import com.aptatek.aptatek.domain.interactor.countdown.Countdown;

import org.junit.Test;

import io.reactivex.subscribers.TestSubscriber;

public class CountdownTest {

    @Test
    public void testCountdown() throws Exception {
        final TestSubscriber<Long> test = Countdown.countdown(200L,
                tick -> tick > 5,
                tick -> tick
        ).test();

        test.await();
        test.assertComplete();
        test.assertNoErrors();
    }

}
