package com.aptatek.aptatek.presenter.test.samplewetting;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.samplewetting.SampleWettingInteractor;
import com.aptatek.aptatek.domain.model.Countdown;
import com.aptatek.aptatek.view.test.samplewetting.SampleWettingPresenter;
import com.aptatek.aptatek.view.test.samplewetting.SampleWettingView;

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

public class SampleWettingPresenterTest {

    private final String TEST_STRING = "hello";

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    SampleWettingInteractor sampleWettingInteractor;

    @Mock
    SampleWettingView view;

    private SampleWettingPresenter presenter;

    private final FlowableProcessor<Countdown> countdownProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<Integer> progressProcessor = BehaviorProcessor.create();

    @BeforeClass
    public static void beforeClass() throws Exception {
        final Scheduler immediate = new Scheduler() {

            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
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
        when(sampleWettingInteractor.getWettingCountdown()).thenReturn(countdownProcessor);
        when(sampleWettingInteractor.getWettingProgress()).thenReturn(progressProcessor);

        presenter = new SampleWettingPresenter(resourceInteractor, sampleWettingInteractor);
        presenter.attachView(view);
    }

    @Test
    public void testInitUi() throws Exception {
        presenter.initUi();

        verify(view).setCancelBigVisible(true);
        verify(view).setCircleCancelVisible(false);
        verify(view).setNavigationButtonVisible(false);
        verify(view).setTitle(TEST_STRING);
        verify(view).setMessage(TEST_STRING);
    }

    @Test
    public void testAttachViewCallsInteractor() throws Exception {
        verify(sampleWettingInteractor).getWettingCountdown();
        verify(sampleWettingInteractor).getWettingProgress();
    }

    @Test
    public void testCountdownUpdateCallsView() throws Exception {
        countdownProcessor.onNext(Countdown.builder().setRemainingFormattedText(TEST_STRING).setRemainingMillis(1000L).build());

        Thread.sleep(1000L);

        verify(view).showCountdown(TEST_STRING);
    }

    @Test
    public void testProgressUpdateCallsView() throws Exception {
        progressProcessor.onNext(30);

        Thread.sleep(1000L);

        verify(view).showImage(ArgumentMatchers.anyInt());
    }

}
