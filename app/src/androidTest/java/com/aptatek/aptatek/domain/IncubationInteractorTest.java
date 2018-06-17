package com.aptatek.aptatek.domain;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.aptatek.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.aptatek.domain.model.Countdown;
import com.aptatek.aptatek.injection.component.DaggerTestComponent;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.test.TestModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

@RunWith(AndroidJUnit4.class)
public class IncubationInteractorTest {

    @Inject
    IncubationInteractor interactor;

    @Before
    public void setUp() throws Exception {
        DaggerTestComponent.builder()
                .applicationModule(new ApplicationModule(((Application) InstrumentationRegistry.getTargetContext().getApplicationContext())))
                .build()
                .plus(new TestModule())
                .inject(this);
    }

    @After
    public void cleanUp() throws Exception {
        interactor.stopIncubation().test();
    }

    @Test
    public void testHasRunning() throws Exception {
        interactor.startIncubation().test();
        final TestObserver<Boolean> test = interactor.hasRunningIncubation().test();
        test.assertValue(true);
        test.assertComplete();
    }

    @Test
    public void testHasRunningExpired() throws Exception {
        interactor.stopIncubation().test();
        final TestObserver<Boolean> test = interactor.hasRunningIncubation().test();
        test.assertValue(false);
        test.assertComplete();
    }

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
