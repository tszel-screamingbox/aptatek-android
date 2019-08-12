package com.aptatek.pkulab.view.main.weekly;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.main.weekly.chart.PdfChartDataTransformer;
import com.aptatek.pkulab.view.main.weekly.csv.CsvExport;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsValueFormatter;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @test.layer View / Weekly
 * @test.feature Weekly Chart
 * @test.type Unit tests
 */
public class WeeklyResultFragmentPresenterTest {

    private static final String TEST_STRING = "test";

    @Mock
    private WeeklyResultFragmentView view;
    @Mock
    private TestResultInteractor testResultInteractor;
    @Mock
    private ResourceInteractor resourceInteractor;
    @Mock
    private PkuRangeInteractor pkuRangeInteractor;
    @Mock
    private WeeklyChartResourceFormatter weeklyChartResourceFormatter;
    @Mock
    private PdfChartDataTransformer pdfChartDataTransformer;
    @Mock
    private CsvExport csvExport;
    @Mock
    private PreferenceManager preferenceManager;
    @Mock
    private RangeSettingsValueFormatter valueFormatter;
    @Mock
    IAnalyticsManager analyticsManager;

    private WeeklyResultFragmentPresenter presenter;
    private List<TestResult> testResultList = new ArrayList<>();

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
                return new ExecutorScheduler.ExecutorWorker(Runnable::run, true);
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

    /**
     * Setting up the required presenter and defining mocked component's behaviour
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final PkuRangeInfo rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_INCREASED_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_INCREASED_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_INCREASED_FLOOR)
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();
        final long now = System.currentTimeMillis();
        final TestResult testResult = TestResult.builder()
                .setId(String.valueOf(now))
                .setReaderId(UUID.randomUUID().toString())
                .setPkuLevel(PkuLevel.create(10, PkuLevelUnits.MICRO_MOL))
                .setTimestamp(now)
                .build();
        testResultList.add(testResult);


        doReturn(TEST_STRING).when(weeklyChartResourceFormatter).getWeeklyChartTitle(ArgumentMatchers.anyInt());
        doReturn(TEST_STRING).when(resourceInteractor).getStringResource(ArgumentMatchers.anyInt());
        when(pkuRangeInteractor.getInfo()).thenReturn(Single.just(rangeInfo));
        when(testResultInteractor.listAll()).thenReturn(Flowable.just(testResultList));

        presenter = new WeeklyResultFragmentPresenter(
                testResultInteractor,
                resourceInteractor,
                pkuRangeInteractor,
                weeklyChartResourceFormatter,
                pdfChartDataTransformer,
                csvExport,
                preferenceManager,
                valueFormatter, analyticsManager);
        presenter.attachView(view);
    }


    /**
     * Setting subtitle for the screen
     *
     * @test.input Page number for the subtitle
     * @test.expected {@link  WeeklyResultFragmentView#onSubtitleChanged(String)}  onSubtitleChanged} method is called, without any error.
     */
    @Test
    public void testSubtitle() {
        presenter.loadValidWeeks();
        presenter.subTitle(0);
        verify(view).onSubtitleChanged(TEST_STRING);
    }

    /**
     * Testing page changing
     *
     * @test.input Page number for the screen
     * @test.expected {@link  WeeklyResultFragmentView#onLoadNextPage(int)}  onLoadNextPage},
     * {@link  WeeklyResultFragmentView#onUpdateLeftArrow(boolean)}  onUpdateLeftArrow}
     * and {@link  WeeklyResultFragmentView#onUpdateRightArrow(boolean) onUpdateRightArrow} method are called, without any error.
     */
    @Test
    public void testShowPage() {
        presenter.loadValidWeeks();
        final int pageNum = 3;
        presenter.showPage(pageNum);
        verify(view).onLoadNextPage(pageNum);
        verify(view).onUpdateLeftArrow(true);
        verify(view).onUpdateRightArrow(true);
    }


    /**
     * Testing chart's arrows update
     *
     * @test.input Page number for updating arrows visibility
     * @test.expected {@link  WeeklyResultFragmentView#onUpdateLeftArrow(boolean)}  onUpdateLeftArrow}
     * and {@link  WeeklyResultFragmentView#onUpdateRightArrow(boolean) onUpdateRightArrow} method are called, without any error.
     */
    @Test
    public void testUpdateArrow() {
        presenter.loadValidWeeks();
        presenter.updateArrows(presenter.getValidWeeks().size() - 1);
        verify(view).onUpdateLeftArrow(false);
        verify(view).onUpdateRightArrow(false);
    }
}
