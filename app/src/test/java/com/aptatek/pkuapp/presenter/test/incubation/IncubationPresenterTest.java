package com.aptatek.pkuapp.presenter.test.incubation;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.model.Countdown;
import com.aptatek.pkuapp.view.test.incubation.IncubationPresenter;
import com.aptatek.pkuapp.view.test.incubation.IncubationView;

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
 * Tests for the IncubationPresenter class.
 *
 * @test.layer presentation
 * @test.feature BloodIsProcessing
 * @test.type unit
 */
public class IncubationPresenterTest {

    private static final String TEST_STRING = "test";

    @Mock
    IncubationInteractor incubationInteractor;

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    IncubationView view;

    private IncubationPresenter presenter;

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
        when(incubationInteractor.getIncubationCountdown()).thenReturn(countdownProcessor);

        presenter = new IncubationPresenter(resourceInteractor, incubationInteractor);
        presenter.attachView(view);
    }

    /**
     * Tests the proper behavior: initUi() should trigger changes on UI: the initial state should be rendered.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testInitUi() throws Exception {
        presenter.initUi();

        verify(view).setTitle(TEST_STRING);
        verify(view).setMessage(TEST_STRING);
        verify(view).setNavigationButtonVisible(true);
        verify(view).setNavigationButtonText(TEST_STRING);
        verify(view).setCircleCancelVisible(true);
        verify(view).setCancelBigVisible(false);
    }

    /**
     * Tests the proper behavior: when the screen is displayed, the presenter should interact with the interactor to get the current countdown state.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testAttachViewCallsInteractor() throws Exception {
        verify(incubationInteractor).getIncubationCountdown();
    }

    /**
     * Tests the proper behavior: when the interactor's countdown stream emits a new item, the UI should be updated: the countdown text should render the new value.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testUpdateCountdown() throws Exception {
        countdownProcessor.onNext(Countdown.builder().setRemainingFormattedText(TEST_STRING).setRemainingMillis(0L).build());

        verify(view).showCountdownText(TEST_STRING);
    }

}
