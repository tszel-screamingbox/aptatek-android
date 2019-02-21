package com.aptatek.pkulab.view.main.home;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingStatus;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.view.main.home.adapter.chart.ChartVM;
import com.aptatek.pkulab.view.main.home.adapter.daily.DailyChartFormatter;
import com.aptatek.pkulab.view.rangeinfo.PkuValueFormatter;
import com.aptatek.pkulab.view.test.TestScreens;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import io.reactivex.Single;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @test.layer View / Main
 * @test.feature MainHostActivity, BubbleChart
 * @test.type Unit tests
 */
public class HomeFragmentPresenterTest {

    private static final String TEST_STRING = "hello";
    private static final float BATTERY_NORMAL = 0.6f;
    private static final float BATTERY_LOW = 0.1f;

    @Mock
    private ResourceInteractor resourceInteractor;
    @Mock
    private HomeFragmentView view;
    @Mock
    private TestResultInteractor testResultInteractor;
    @Mock
    private PkuRangeInteractor rangeInteractor;
    @Mock
    private DailyChartFormatter dailyChartFormatter;
    @Mock
    private WettingInteractor wettingInteractor;
    @Mock
    private PreferenceManager preferenceManager;
    @Mock
    private TestInteractor testInteractor;
    @Mock
    private PkuValueFormatter pkuValueFormatter;

    private final Date date = new Date();
    private HomeFragmentPresenter presenter;
    private ChartVM emptyItem;
    private ChartVM notEmptyItem;

    /**
     * Setting up the required presenter and defining mocked component's behaviour
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        doReturn(TEST_STRING).when(dailyChartFormatter).formatDate(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean());
        doReturn(TEST_STRING).when(dailyChartFormatter).getNameOfDay(ArgumentMatchers.anyLong());
        doReturn(TEST_STRING).when(resourceInteractor).getStringResource(ArgumentMatchers.anyInt());
        doReturn(Single.just(TestScreens.TURN_READER_ON)).when(testInteractor).getLastScreen();
        doReturn(Single.just(WettingStatus.NOT_STARTED)).when(wettingInteractor).getWettingStatus();

        presenter = new HomeFragmentPresenter(testResultInteractor, resourceInteractor, rangeInteractor, dailyChartFormatter, wettingInteractor, preferenceManager, testInteractor, pkuValueFormatter);
        presenter.attachView(view);
        emptyItem = ChartVM.builder()
                .setDate(date)
                .setMeasures(new ArrayList<>())
                .setNumberOfMeasures(0)
                .setZoomed(false)
                .setColorRes(0)
                .setState(0)
                .build();

        final long now = System.currentTimeMillis();
        notEmptyItem = ChartVM.builder()
                .setDate(date)
                .setMeasures(Collections.singletonList(TestResult.builder()
                        .setPkuLevel(PkuLevel.create(20.0f, PkuLevelUnits.MILLI_GRAM))
                        .setTimestamp(now)
                        .setId(String.valueOf(now))
                        .setReaderId(String.valueOf(now))
                        .build()))
                .setZoomed(true)
                .setNumberOfMeasures(1)
                .setColorRes(0)
                .setState(0)
                .build();
    }

    /**
     * Bubble chart zooming animation with empty measure.
     *
     * @test.expected {@link  HomeFragmentView#updateTitles(String, String)   updateTitles(String, String)  }
     * method is called, without any error.
     */
    @Test
    public void testItemUpdateWithEmptyMeasure() {
        presenter.itemZoomIn(emptyItem);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        verify(view).updateTitles(TEST_STRING, TEST_STRING);
    }

    /**
     * Bubble chart zooming animation.
     *
     * @test.expected {@link  HomeFragmentView#updateTitles(String, String)   updateTitles(String, String)  }
     * method is called, without any error.
     */
    @Test
    public void testItemUpdate() {
        presenter.itemZoomIn(notEmptyItem);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        verify(view).updateTitles(TEST_STRING, TEST_STRING);
    }

    /**
     * Showing range settings dialog.
     *
     * @test.expected {@link  HomeFragmentView#showRangeDialog()   showRangeDialog()  }
     * method is called, without any error.
     */
    @Test
    public void testRangeDialog() {
        when(preferenceManager.isRangeDialogShown()).thenReturn(false);
        presenter.initView();
        verify(view).showRangeDialog();
    }
}
