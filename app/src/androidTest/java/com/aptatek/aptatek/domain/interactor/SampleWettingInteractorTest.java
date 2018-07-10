package com.aptatek.aptatek.domain.interactor;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.aptatek.domain.interactor.samplewetting.SampleWettingInteractor;
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
public class SampleWettingInteractorTest {

    @Inject
    SampleWettingInteractor interactor;

    @Before
    public void setUp() throws Exception {
        DaggerTestComponent.builder()
                .applicationModule(new ApplicationModule(((Application) InstrumentationRegistry.getTargetContext().getApplicationContext())))
                .testModule(new TestModule())
                .build()
                .inject(this);
    }

    @After
    public void cleanUp() throws Exception {
        interactor.stopWetting().test();
    }

    @Test
    public void testHasRunning() throws Exception {
        interactor.startWetting().test();
        final TestObserver<Boolean> test = interactor.hasRunningWetting().test();
        test.assertValue(true);
        test.assertComplete();
    }

    @Test
    public void testHasRunningExpired() throws Exception {
        interactor.stopWetting().test();
        final TestObserver<Boolean> test = interactor.hasRunningWetting().test();
        test.assertValue(false);
        test.assertComplete();
    }

    @Test
    public void testCountdown() throws Exception {
        interactor.startWetting().test();
        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();

        test.await(5, TimeUnit.SECONDS);

        test.assertNotComplete();
        test.assertNoErrors();
        test.assertValueAt(0, value -> value.getRemainingFormattedText().startsWith("09:"));
    }

    @Test
    public void testProgress() throws Exception {
        interactor.startWetting().test();
        final TestSubscriber<Countdown> test = interactor.getWettingCountdown().test();
        final TestSubscriber<Integer> testProgress = interactor.getWettingProgress().test();

        Thread.sleep(4000L);

        testProgress.assertNotComplete();
        testProgress.assertNoErrors();
        testProgress.assertValueAt(testProgress.valueCount() - 1, (value) -> value < 100 && value > 90);
    }

}