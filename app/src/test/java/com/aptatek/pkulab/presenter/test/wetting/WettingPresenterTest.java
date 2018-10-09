package com.aptatek.pkulab.presenter.test.wetting;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.model.Countdown;
import com.aptatek.pkulab.view.test.wetting.WettingPresenter;
import com.aptatek.pkulab.view.test.wetting.WettingView;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the WettingPresenter class.
 *
 * @test.layer presentation
 * @test.feature SampleWetting
 * @test.type unit
 */
public class WettingPresenterTest {

    private final String TEST_STRING = "hello";

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    WettingInteractor wettingInteractor;

    @Mock
    WettingView view;

    private WettingPresenter presenter;

    private final FlowableProcessor<Countdown> countdownProcessor = BehaviorProcessor.create();

    @BeforeClass
    public static void beforeClass() throws Exception {
        final Scheduler immediate = new Scheduler() {

            @Override
            public Disposable scheduleDirect(@NonNull final Runnable run, final long delay, @NonNull final TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> immediate);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        RxJavaPlugins.reset();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn(TEST_STRING);
        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt(), ArgumentMatchers.anyVararg())).thenReturn(TEST_STRING);
        when(wettingInteractor.getWettingCountdown()).thenReturn(countdownProcessor);

        presenter = new WettingPresenter(resourceInteractor, wettingInteractor);
        presenter.attachView(view);
    }

    /**
     * Tests the proper behavior: the initUi() method should trigger changes on UI: the initial state should be rendered
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testInitUi() throws Exception {
        presenter.initUi();

        verify(view).setTitle(TEST_STRING);
        verify(view).setMessage(TEST_STRING);

        // TODO write proper tests...
    }

    /**
     * Tests the proper behavior: when the screen is displayed, the presenter should check the current state of sample wetting via the interactor.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testAttachViewCallsInteractor() throws Exception {
        verify(wettingInteractor).getWettingCountdown();
    }

    /**
     * Tests the proper behavior: when the interactor's countdown stream emits a new item, the UI should be updated: the countdown text should display the new value.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCountdownUpdateCallsView() throws Exception {
        countdownProcessor.onNext(Countdown.builder().setRemainingFormattedText(TEST_STRING).setRemainingMillis(1000L).build());

        Thread.sleep(1000L);

        verify(view).showCountdown(TEST_STRING);
    }

}
