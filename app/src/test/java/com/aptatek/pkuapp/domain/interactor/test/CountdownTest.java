package com.aptatek.pkuapp.domain.interactor.test;

import com.aptatek.pkuapp.domain.interactor.countdown.Countdown;

import org.junit.Test;

import io.reactivex.subscribers.TestSubscriber;

/**
 * Tests for the CountDown class.
 *
 * @test.layer presentation
 * @test.feature Test Flows
 * @test.type domain
 */
public class CountdownTest {

    /**
     * Tests the proper behavior: countdown() should periodically emit elements until it finally meets the terminating condition.
     *
     * @test.input
     * @test.expected
     */
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
