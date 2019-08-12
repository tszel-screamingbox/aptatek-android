package com.aptatek.pkulab.view.test;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static com.aptatek.pkulab.domain.interactor.wetting.WettingStatus.FINISHED;
import static com.aptatek.pkulab.domain.interactor.wetting.WettingStatus.RUNNING;
import static com.aptatek.pkulab.view.test.TestScreens.BREAK_FOIL;
import static com.aptatek.pkulab.view.test.TestScreens.CANCEL;
import static com.aptatek.pkulab.view.test.TestScreens.COLLECT_BLOOD;
import static com.aptatek.pkulab.view.test.TestScreens.CONNECT_IT_ALL;
import static com.aptatek.pkulab.view.test.TestScreens.TESTING;
import static com.aptatek.pkulab.view.test.TestScreens.TURN_READER_ON;
import static com.aptatek.pkulab.view.test.TestScreens.WETTING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @test.layer View / Test
 * @test.feature Test screens changing
 * @test.type Unit tests
 */
public class TestActivityPresenterTest {

    @Mock
    private WettingInteractor wettingInteractor;
    @Mock
    private TestActivityView view;
    @Mock
    private TestInteractor testInteractor;
    @Mock
    private IAnalyticsManager analyticsManager;
    private DeviceHelper deviceHelper;

    private TestActivityPresenter presenter;

    /**
     * Setting up the required presenter
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(Single.just(TestScreens.TURN_READER_ON)).when(testInteractor).getLastScreen();
        doReturn(Completable.complete()).when(testInteractor).setLastScreen(ArgumentMatchers.any());

        deviceHelper = mock(DeviceHelper.class, Mockito.RETURNS_DEEP_STUBS);
        when(deviceHelper.getPhoneBattery()).thenReturn(100);

        presenter = new TestActivityPresenter(wettingInteractor, testInteractor, deviceHelper, analyticsManager);
        presenter.attachView(view);
    }

    /**
     * Show proper screen.
     *
     * @test.expected Loading the next screen, {@link  TestActivityView#showScreen(TestScreens)  showScreen(TestScreens) }
     * method is called, without any error.
     */
    @Test
    public void showNextScreen() {
        presenter.onShowNextScreen(BREAK_FOIL);
        verify(view).showScreen(any());
    }

    /**
     * Show proper screen after finished test.
     *
     * @test.expected The test has finished, {@link  TestActivityView#showScreen(TestScreens)  showScreen(TestScreens) }
     * method is called, without any error.
     */
    @Test
    public void testFinishedTest() {
        when(wettingInteractor.getWettingStatus()).thenReturn(Single.just(FINISHED));
        presenter.showProperScreen();
        verify(view).showScreen(TURN_READER_ON);
    }

    /**
     * Show proper screen during running test.
     *
     * @test.expected The test is running, {@link  TestActivityView#showScreen(TestScreens)  showScreen(TestScreens) }
     * method is called, without any error.
     */
    @Test
    public void testRunningTest() {
        when(wettingInteractor.getWettingStatus()).thenReturn(Single.just(RUNNING));
        when(testInteractor.getLastScreen()).thenReturn(Single.just(TestScreens.WETTING));
        presenter.showProperScreen();
        verify(view).showScreen(WETTING);
    }

    /**
     * Show proper screen before starting a new test.
     *
     * @test.expected The test is running, {@link  TestActivityView#showScreen(TestScreens)  showScreen(TestScreens) }
     * method is called, without any error.
     */
    @Test
    public void testNotStartedTest() {
        when(testInteractor.getLastScreen()).thenReturn(Single.just(TestScreens.BREAK_FOIL));
        presenter.showProperScreen();
        verify(view).showScreen(BREAK_FOIL);
    }

    /**
     * Show proper screen when cancelling a running test.
     *
     * @test.expected A running test is being interrupted, {@link  TestActivityView#showScreen(TestScreens)  showScreen(TestScreens) }
     * method is called, without any error.
     */
    @Test
    public void testShowPreviousScreenDuringTesting() {
        presenter.onShowPreviousScreen(WETTING);
        verify(view).showScreen(CANCEL);
    }

    /**
     * Show proper screen when press cancel button (before starting a new test).
     *
     * @test.expected Going back to the previous screen, {@link  TestActivityView#showPreviousScreen()  showPreviousScreen() }
     * method is called, without any error.
     */
    @Test
    public void testShowPreviousScreen() {
        presenter.onShowPreviousScreen(COLLECT_BLOOD);
        verify(view).showPreviousScreen();
    }

    @Test
    public void dontShowLowBattery() {
        when(deviceHelper.isBatteryLow()).thenReturn(true);
        presenter.checkBattery(CONNECT_IT_ALL);
        verify(view, never()).showBatteryAlert();

        presenter.checkBattery(TESTING);
        verify(view, never()).showBatteryAlert();

        presenter.checkBattery(CANCEL);
        verify(view, never()).showBatteryAlert();
    }

    /**
     * Initialize RxJava components before testing.
     */
    @BeforeClass
    public static void beforeClass() {
        final Scheduler immediate = new Scheduler() {

            @Override
            public Disposable scheduleDirect(@NonNull final Runnable run, final long delay, @NonNull final TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run, true);
            }
        };

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> immediate);
    }

    /**
     * Reset RxJava components after testing.
     */
    @AfterClass
    public static void afterClass() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }
}