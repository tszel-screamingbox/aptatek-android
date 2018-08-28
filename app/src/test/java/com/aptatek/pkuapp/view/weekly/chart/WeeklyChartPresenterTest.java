package com.aptatek.pkuapp.view.weekly.chart;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.interactor.cube.CubeInteractor;
import com.aptatek.pkuapp.domain.model.CubeData;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuLevelUnits;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeeklyChartPresenterTest {

    @Mock
    private WeeklyChartView view;
    @Mock
    private CubeInteractor cubeInteractor;
    @Mock
    private WeeklyChartDataTransformer weeklyChartDataTransformer;

    private WeeklyChartPresenter presenter;
    private BubbleDataSet bubbleDataSet;
    private List<CubeData> cubeDataList;
    private List<BubbleEntry> bubbleEntries;

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
    public static void afterClass() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        cubeDataList = new ArrayList<>();
        bubbleEntries = new ArrayList<>();

        final CubeData cubeData = CubeData.builder()
                .setTimestamp(new Date().getTime())
                .setId(new Random().nextLong())
                .setCubeId(UUID.randomUUID().toString())
                .setPkuLevel(PkuLevel.create(10f, PkuLevelUnits.MICRO_MOL))
                .build();
        final ChartEntryData chartEntryData = ChartEntryData.builder()
                .setX(10)
                .setY(10)
                .setSize(1f)
                .setLabel("label")
                .setLabelColor(0)
                .setBubbleColor(0)
                .build();


        cubeDataList.add(cubeData);
        bubbleEntries.add(createBubbleEntryFor(chartEntryData));
        bubbleDataSet = new BubbleDataSet(bubbleEntries, null);

        when(cubeInteractor.listBetween(anyLong(), anyLong())).thenReturn(Single.just(cubeDataList));
        when(weeklyChartDataTransformer.transform(ArgumentMatchers.any(CubeData.class))).thenReturn(Single.just(chartEntryData));
        when(weeklyChartDataTransformer.transformEntries(ArgumentMatchers.anyList())).thenReturn(Single.just(bubbleDataSet));

        presenter = new WeeklyChartPresenter(cubeInteractor, weeklyChartDataTransformer);
        presenter.attachView(view);
    }

    @Test
    public void testGetChartData() {
        presenter.getChartData(0);
        verify(view).displayChartData(bubbleDataSet);
    }

    private BubbleEntry createBubbleEntryFor(final ChartEntryData data) {
        final BubbleEntry bubbleEntry = new BubbleEntry(data.getX(), data.getY(), data.getSize());
        bubbleEntry.setData(data);
        return bubbleEntry;
    }
}
