package com.aptatek.pkuapp.view.weekly;

import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.cube.CubeInteractor;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkuapp.domain.model.CubeData;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuLevelUnits;
import com.aptatek.pkuapp.domain.model.PkuRangeInfo;
import com.aptatek.pkuapp.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.reactivex.Single;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @test.layer View / Weekly
 * @test.feature Weekly Chart
 * @test.type Unit tests
 */
public class WeeklyResultActivityPresenterTest {

    private static final String TEST_STRING = "test";

    @Mock
    private WeeklyResultActivityView view;
    @Mock
    private CubeInteractor cubeInteractor;
    @Mock
    private ResourceInteractor resourceInteractor;
    @Mock
    private PkuRangeInteractor pkuRangeInteractor;
    @Mock
    private WeeklyChartDateFormatter weeklyChartDateFormatter;

    private WeeklyResultActivityPresenter presenter;
    private List<CubeData> cubeDataList = new ArrayList<>();

    /**
     * Setting up the required presenter and defining mocked component's behaviour
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final PkuRangeInfo rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_NORMAL_FLOOR)
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();
        final CubeData cubeData = CubeData.builder()
                .setId(new Random().nextLong())
                .setCubeId(UUID.randomUUID().toString())
                .setPkuLevel(PkuLevel.create(10, PkuLevelUnits.MICRO_MOL))
                .setTimestamp(new Date().getTime())
                .build();
        cubeDataList.add(cubeData);


        doReturn(TEST_STRING).when(weeklyChartDateFormatter).getWeeklyChartTitle(ArgumentMatchers.anyInt());
        doReturn(TEST_STRING).when(resourceInteractor).getStringResource(ArgumentMatchers.anyInt());
        when(pkuRangeInteractor.getInfo()).thenReturn(Single.just(rangeInfo));
        when(cubeInteractor.listAll()).thenReturn(Single.just(cubeDataList));

        presenter = new WeeklyResultActivityPresenter(
                cubeInteractor,
                resourceInteractor,
                pkuRangeInteractor,
                weeklyChartDateFormatter);
        presenter.attachView(view);
    }


    /**
     * Setting subtitle for the screen
     *
     * @test.input Page number for the subtitle
     * @test.expected {@link  com.aptatek.pkuapp.view.weekly.WeeklyResultActivityView#onSubtitleChanged(String)}  onSubtitleChanged} method is called, without any error.
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
     * @test.expected {@link  com.aptatek.pkuapp.view.weekly.WeeklyResultActivityView#onLoadNextPage(int)}  onLoadNextPage},
     * {@link  com.aptatek.pkuapp.view.weekly.WeeklyResultActivityView#onUpdateLeftArrow(boolean)}  onUpdateLeftArrow}
     * and {@link  com.aptatek.pkuapp.view.weekly.WeeklyResultActivityView#onUpdateRightArrow(boolean) onUpdateRightArrow} method are called, without any error.
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
     * @test.expected {@link  com.aptatek.pkuapp.view.weekly.WeeklyResultActivityView#onUpdateLeftArrow(boolean)}  onUpdateLeftArrow}
     * and {@link  com.aptatek.pkuapp.view.weekly.WeeklyResultActivityView#onUpdateRightArrow(boolean) onUpdateRightArrow} method are called, without any error.
     */
    @Test
    public void testUpdateArrow() {
        presenter.loadValidWeeks();
        presenter.updateArrows(presenter.getValidWeeks().size() - 1);
        verify(view).onUpdateLeftArrow(false);
        verify(view).onUpdateRightArrow(false);
    }
}
