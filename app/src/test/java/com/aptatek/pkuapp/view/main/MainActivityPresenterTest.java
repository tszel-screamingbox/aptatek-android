package com.aptatek.pkuapp.view.main;

import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.cube.CubeInteractor;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationStatus;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkuapp.domain.interactor.samplewetting.SampleWettingInteractor;
import com.aptatek.pkuapp.domain.interactor.samplewetting.WettingStatus;
import com.aptatek.pkuapp.domain.model.CubeData;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuLevelUnits;
import com.aptatek.pkuapp.view.main.adapter.chart.ChartVM;
import com.aptatek.pkuapp.view.main.adapter.daily.DailyChartFormatter;

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

// TODO write proper tests for this class...
/**
 * @test.layer View / Main
 * @test.feature MainActivity, BubbleChart
 * @test.type Unit tests
 */
public class MainActivityPresenterTest {

    private static final String TEST_STRING = "hello";

    @Mock
    private ResourceInteractor resourceInteractor;
    @Mock
    private MainActivityView view;
    @Mock
    private CubeInteractor cubeInteractor;
    @Mock
    private PkuRangeInteractor rangeInteractor;
    @Mock
    private DailyChartFormatter dailyChartFormatter;
    @Mock
    private IncubationInteractor incubationInteractor;
    @Mock
    private SampleWettingInteractor sampleWettingInteractor;

    private final Date date = new Date();
    private MainActivityPresenter presenter;
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
        doReturn(Single.just(IncubationStatus.NOT_STARTED)).when(incubationInteractor).getIncubationStatus();
        doReturn(Single.just(WettingStatus.NOT_STARTED)).when(sampleWettingInteractor).getWettingStatus();

        presenter = new MainActivityPresenter(cubeInteractor, resourceInteractor, rangeInteractor, dailyChartFormatter, incubationInteractor, sampleWettingInteractor);
        presenter.attachView(view);
        emptyItem = ChartVM.builder()
                .setDate(date)
                .setMeasures(new ArrayList<>())
                .setNumberOfMeasures(0)
                .setZoomed(false)
                .setColorRes(0)
                .setState(0)
                .setExpandedBackgroundRes(0)
                .setCollapsedBackgroundRes(0)
                .build();

        final long now = System.currentTimeMillis();
        notEmptyItem = ChartVM.builder()
                .setDate(date)
                .setMeasures(Collections.singletonList(CubeData.builder()
                        .setPkuLevel(PkuLevel.create(20.0f, PkuLevelUnits.MILLI_GRAM))
                        .setTimestamp(now)
                        .setId(now)
                        .setCubeId(String.valueOf(now))
                        .build()))
                .setZoomed(true)
                .setNumberOfMeasures(1)
                .setColorRes(0)
                .setState(0)
                .setExpandedBackgroundRes(0)
                .setCollapsedBackgroundRes(0)
                .build();
    }

    /**
     * Bubble chart zooming animation with empty measure.
     *
     * @test.expected {@link  MainActivityView#updateTitles(String, String)   updateTitles(String, String)  }
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
     * @test.expected {@link  MainActivityView#updateTitles(String, String)   updateTitles(String, String)  }
     * method is called, without any error.
     */
    @Test
    public void testItemUpdate() {
        presenter.itemZoomIn(notEmptyItem);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        verify(view).updateTitles(TEST_STRING, TEST_STRING);
    }
}
