package com.aptatek.aptatek.view.main;

import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.respository.chart.ChartDTO;
import com.aptatek.aptatek.domain.respository.chart.Measure;
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
        emptyItem = new ChartVM(new ChartDTO(0, date, new ArrayList<>(), 0, 0, 0));
        notEmptyItem = new ChartVM(new ChartDTO(1, date, Collections.singletonList(new Measure(20, 20)), 0, 0, 0));
    }

    @Test
    public void testItemUpdateWithEmptyMeasure() {
        presenter.itemChanged(emptyItem);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        verify(view).updateTitles(dayOfWeek(cal.get(Calendar.DAY_OF_WEEK)), formatDate(date, MainActivityPresenter.PATTERN_DAY));
    }

    @Test
    public void testItemUpdate() {
        presenter.itemChanged(notEmptyItem);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        verify(view).updateTitles(dayOfWeek(cal.get(Calendar.DAY_OF_WEEK)), formatDate(date, MainActivityPresenter.PATTERN_WITH_TIME));
    }
}
