package com.aptatek.pkuapp.view.main;

import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.cube.CubeInteractor;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkuapp.domain.model.CubeData;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuLevelUnits;
import com.aptatek.pkuapp.view.main.adapter.ChartVM;
import com.aptatek.pkuapp.view.main.adapter.DailyChartFormatter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

// TODO write proper tests for this class...
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

    private final Date date = new Date();
    private MainActivityPresenter presenter;
    private ChartVM emptyItem;
    private ChartVM notEmptyItem;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        doReturn(TEST_STRING).when(dailyChartFormatter).formatDate(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean());
        doReturn(TEST_STRING).when(dailyChartFormatter).getNameOfDay(ArgumentMatchers.anyLong());
        doReturn(TEST_STRING).when(resourceInteractor).getStringResource(ArgumentMatchers.anyInt());

        presenter = new MainActivityPresenter(cubeInteractor, resourceInteractor, rangeInteractor, dailyChartFormatter);
        presenter.attachView(view);
        emptyItem = ChartVM.builder()
                .setDate(date)
                .setMeasures(new ArrayList<>())
                .setBubbleYAxis(0)
                .setStartLineYAxis(0)
                .setEndLineYAxis(0)
                .setNumberOfMeasures(0)
                .setZoomed(false)
                .setColorRes(0)
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
                .setBubbleYAxis(0)
                .setZoomed(true)
                .setNumberOfMeasures(1)
                .setStartLineYAxis(0)
                .setEndLineYAxis(0)
                .setColorRes(0)
                .setExpandedBackgroundRes(0)
                .setCollapsedBackgroundRes(0)
                .build();
    }

    @Test
    public void testItemUpdateWithEmptyMeasure() {
        presenter.itemZoomIn(emptyItem);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        verify(view).updateTitles(TEST_STRING, TEST_STRING);
    }

    @Test
    public void testItemUpdate() {
        presenter.itemZoomIn(notEmptyItem);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        verify(view).updateTitles(TEST_STRING, TEST_STRING);
    }
}