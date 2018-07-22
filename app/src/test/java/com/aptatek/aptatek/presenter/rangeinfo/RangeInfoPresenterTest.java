package com.aptatek.aptatek.presenter.rangeinfo;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.aptatek.aptatek.util.Constants;
import com.aptatek.aptatek.view.rangeinfo.PkuValueFormatter;
import com.aptatek.aptatek.view.rangeinfo.RangeInfoPresenter;
import com.aptatek.aptatek.view.rangeinfo.RangeInfoUiModel;
import com.aptatek.aptatek.view.rangeinfo.RangeInfoView;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class RangeInfoPresenterTest {

    private RangeInfoPresenter presenter;

    @Mock
    PkuRangeInteractor rangeInteractor;

    @Mock
    PkuValueFormatter valueFormatter;

    @Mock
    RangeInfoView view;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new RangeInfoPresenter(rangeInteractor, valueFormatter);
        presenter.attachView(view);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Test
    public void testRefreshCallsInteractorAndFormatter() {
        final PkuRangeInfo rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_NORMAL_FLOOR)
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .build();
        doReturn(Single.just(rangeInfo)).when(rangeInteractor).getInfo();
        final String testValue = "hello";
        doReturn(testValue).when(valueFormatter).formatHigh(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).formatLow(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).formatNormal(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).formatVeryHigh(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).formatUnits(ArgumentMatchers.any(PkuRangeInfo.class));

        presenter.refresh();

        verify(rangeInteractor).getInfo();
        verify(valueFormatter).formatVeryHigh(rangeInfo);
        verify(valueFormatter).formatHigh(rangeInfo);
        verify(valueFormatter).formatNormal(rangeInfo);
        verify(valueFormatter).formatLow(rangeInfo);
        verify(valueFormatter).formatUnits(rangeInfo);

        verify(view).displayRangeInfo(RangeInfoUiModel.builder()
                .setHighValue(testValue)
                .setNormalValue(testValue)
                .setLowValue(testValue)
                .setVeryHighValue(testValue)
                .setUnitValue(testValue)
                .build());
    }

}
