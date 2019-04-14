package com.aptatek.pkulab.domain.interactor;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingStatus;
import com.aptatek.pkulab.domain.model.Countdown;
import com.aptatek.pkulab.injection.component.DaggerAndroidTestComponent;
import com.aptatek.pkulab.injection.module.ApplicationModule;
import com.aptatek.pkulab.injection.module.test.TestModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

/**
 * Tests for the WettingInteractor class.
 *
 * @test.layer domain
 * @test.feature BloodIsProcessin
 * @test.type integration
 */
@RunWith(AndroidJUnit4.class)
public class WettingInteractorTest {

    @Inject
    WettingInteractor interactor;

    @Before
    public void setUp() throws Exception {
        DaggerAndroidTestComponent.builder()
                .applicationModule(new ApplicationModule(((Application) InstrumentationRegistry.getTargetContext().getApplicationContext())))
                .build()
                .plus(new TestModule())
                .inject(this);
    }

    @After
    public void cleanUp() throws Exception {
        interactor.resetWetting().test();
    }

    /**
     * Tests whether the first wetting status is RUNNING right after a successful startWetting() call.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testHasRunning() throws Exception {
        interactor.startWetting().test();
        final TestObserver<WettingStatus> test = interactor.getWettingStatus().test();
        test.assertValue(WettingStatus.RUNNING);
        test.assertComplete();
    }

    /**
     * Tests whether the first wetting status is NOT_STARTED when there is no wetting running.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testNotStarted() throws Exception {
        interactor.resetWetting().test();
        final TestObserver<WettingStatus> test = interactor.getWettingStatus().test();
        test.assertValue(WettingStatus.NOT_STARTED);
        test.assertComplete();
    }

    /**
     * Tests whether the wetting countdown starts to count down after a successful wetting start.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCountdown() throws Exception {
        interactor.startWetting().test();
        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();

        test.await(5, TimeUnit.SECONDS);

        test.assertNotComplete();
        test.assertNoErrors();
        test.assertValueAt(0, value -> value.getRemainingFormattedText().startsWith("04:"));
    }

}