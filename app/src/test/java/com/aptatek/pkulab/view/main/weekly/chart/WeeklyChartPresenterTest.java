package com.aptatek.pkulab.view.main.weekly.chart;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.cube.TestResultInteractor;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
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


/**
 * @test.layer View / Weekly
 * @test.feature Weekly chart
 * @test.type Unit tests
 */
public class WeeklyChartPresenterTest {

    @Mock
    private WeeklyChartView view;
    @Mock
    private TestResultInteractor testResultInteractor;
    @Mock
    private WeeklyChartDataTransformer weeklyChartDataTransformer;

    private WeeklyChartPresenter presenter;
    private BubbleDataSet bubbleDataSet;
    private List<TestResult> testResultList;
    private List<BubbleEntry> bubbleEntries;

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
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setSingleSchedulerHandler(scheduler -> immediate);
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

    /**
     * Setting up the required presenter and defining mocked component's behaviour
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        testResultList = new ArrayList<>();
        bubbleEntries = new ArrayList<>();

        final long now = System.currentTimeMillis();
        final TestResult testResult = TestResult.builder()
                .setTimestamp(now)
                .setId(String.valueOf(now))
                .setReaderId(UUID.randomUUID().toString())
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


        testResultList.add(testResult);
        bubbleEntries.add(createBubbleEntryFor(chartEntryData));
        bubbleDataSet = new BubbleDataSet(bubbleEntries, null);

        when(testResultInteractor.listBetween(anyLong(), anyLong())).thenReturn(Single.just(testResultList));
        when(weeklyChartDataTransformer.transform(ArgumentMatchers.any(TestResult.class))).thenReturn(Single.just(chartEntryData));
        when(weeklyChartDataTransformer.transformEntries(ArgumentMatchers.anyList())).thenReturn(Single.just(bubbleDataSet));

        presenter = new WeeklyChartPresenter(testResultInteractor, weeklyChartDataTransformer);
        presenter.attachView(view);
    }

    /**
     * Getting BubbleDataSet for BubbleChart
     *
     * @test.input Numbers of week before (0)
     * @test.expected {@link com.aptatek.pkulab.view.main.weekly.chart.WeeklyChartView#displayChartData(BubbleDataSet bubbleDataSet) displayChartData} method is called, without any error.
     */
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
