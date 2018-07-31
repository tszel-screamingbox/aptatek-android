package com.aptatek.aptatek.view.main;

import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.respository.manager.FakeCubeDataManager;
import com.aptatek.aptatek.util.ChartUtils;
import com.aptatek.aptatek.view.main.adapter.ChartVM;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static com.aptatek.aptatek.util.CalendarUtils.dayOfWeek;
import static com.aptatek.aptatek.util.CalendarUtils.formatDate;
import static org.mockito.Mockito.verify;

public class MainActivityPresenterTest {

    @Mock
    private ResourceInteractor resourceInteractor;
    @Mock
    private ChartUtils chartUtils;
    @Mock
    private FakeCubeDataManager fakeCubeDataManager;
    @Mock
    private MainActivityView view;

    private final Date date = new Date();
    private MainActivityPresenter presenter;
    private ChartVM emptyItem;
    private ChartVM notEmptyItem;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new MainActivityPresenter(fakeCubeDataManager, chartUtils, resourceInteractor);
        presenter.attachView(view);
        emptyItem = ChartVM.builder()
                .setId(0)
                .setDate(date)
                .setMeasures(new ArrayList<>())
                .setBubbleYAxis(0)
                .setStartLineYAxis(0)
                .setEndLineYAxis(0)
                .setNumberOfMeasures(0)
                .setZoomed(false)
                .build();

        notEmptyItem = ChartVM.builder()
                .setId(1)
                .setDate(date)
                .setMeasures(Collections.singletonList(PkuLevel.create(20.0f, PkuLevelUnits.MILLI_GRAM)))
                .setBubbleYAxis(0)
                .setZoomed(true)
                .setNumberOfMeasures(1)
                .setStartLineYAxis(0)
                .setEndLineYAxis(0).build();
    }

    @Test
    public void testItemUpdateWithEmptyMeasure() {
        presenter.itemZoomIn(emptyItem);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        verify(view).updateTitles(dayOfWeek(cal.get(Calendar.DAY_OF_WEEK)), formatDate(date, MainActivityPresenter.PATTERN_DAY));
    }

    @Test
    public void testItemUpdate() {
        presenter.itemZoomIn(notEmptyItem);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        verify(view).updateTitles(dayOfWeek(cal.get(Calendar.DAY_OF_WEEK)), formatDate(date, MainActivityPresenter.PATTERN_WITH_TIME));
    }
}
