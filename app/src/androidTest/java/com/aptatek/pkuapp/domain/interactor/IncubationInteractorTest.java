package com.aptatek.pkuapp.domain.interactor;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.pkuapp.domain.model.Countdown;
import com.aptatek.pkuapp.injection.component.DaggerAndroidTestComponent;
import com.aptatek.pkuapp.injection.module.ApplicationModule;
import com.aptatek.pkuapp.injection.module.test.TestModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

/**
 * Tests for the IncubationInteractor class.
 *
 * @test.layer domain
 * @test.feature BloodIsProcessin
 * @test.type integration
 */
@RunWith(AndroidJUnit4.class)
public class IncubationInteractorTest {

    @Inject
    IncubationInteractor interactor;

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
        interactor.resetIncubation().test();
    }

    /**
     * Tests whether the first incubation status is RUNNING right after a successful startIncubation() call.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testHasRunning() throws Exception {
        interactor.startIncubation().test();
        final TestObserver<IncubationStatus> test = interactor.getIncubationStatus().test();
        test.assertValue(IncubationStatus.RUNNING);
        test.assertComplete();
    }

    /**
     * Tests whether the first incubation status is NOT_STARTED when there is no running incubation.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testHasRunningExpired() throws Exception {
        interactor.resetIncubation().test();
        final TestObserver<IncubationStatus> test = interactor.getIncubationStatus().test();
        test.assertValue(IncubationStatus.NOT_STARTED);
        test.assertComplete();
    }

    /**
     * Tests whether the incubation countdown starts to count down right after startIncubation call.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCountdown() throws Exception {
        interactor.startIncubation().test();
        final TestSubscriber<Countdown> test = interactor.getIncubationCountdown().test();

        test.await(5, TimeUnit.SECONDS);

        test.assertNotComplete();
        test.assertNoErrors();
        test.assertValueAt(0, value -> value.getRemainingFormattedText().startsWith("29:"));
    }

}