package com.aptatek.aptatek.presenter.rangesettigns;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.aptatek.aptatek.util.Constants;
import com.aptatek.aptatek.view.rangeinfo.RangeInfoUiModel;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsModel;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsPresenter;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsValueFormatter;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsView;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class RangeSettingsPresenterTest {

    private RangeSettingsPresenter presenter;

    @Mock
    PkuRangeInteractor rangeInteractor;

    @Mock
    RangeSettingsValueFormatter valueFormatter;

    @Mock
    RangeSettingsView view;

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

        presenter = new RangeSettingsPresenter(rangeInteractor, valueFormatter);
        presenter.attachView(view);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Test
    public void testRefresh() throws Exception {
        final PkuRangeInfo rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_NORMAL_FLOOR)
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();
        doReturn(Single.just(rangeInfo)).when(rangeInteractor).getInfo();
        final String testValue = "hello";
        doReturn(testValue).when(valueFormatter).getFormattedLow(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).getFormattedHigh(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).getFormattedVeryHigh(ArgumentMatchers.any(PkuRangeInfo.class));

        presenter.refresh();

        verify(rangeInteractor).getInfo();
        verify(valueFormatter).getFormattedLow(rangeInfo);
        verify(valueFormatter).getFormattedHigh(rangeInfo);
        verify(valueFormatter).getFormattedVeryHigh(rangeInfo);

        verify(view).displayRangeSettings(RangeSettingsModel.builder()
                .setLowText(valueFormatter.getFormattedLow(rangeInfo))
                .setHighText(valueFormatter.getFormattedHigh(rangeInfo))
                .setVeryHighText(valueFormatter.getFormattedVeryHigh(rangeInfo))
                .setNormalFloorMMolValue(Constants.DEFAULT_PKU_NORMAL_FLOOR)
                .setNormalCeilMMolValue(Constants.DEFAULT_PKU_NORMAL_CEIL)
                .setNormalAbsoluteFloorMMolValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteCeilMMolValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .setSelectedUnit(rangeInfo.getPkuLevelUnit())
                .build());
    }

    @Test
    public void testFormatTest() throws Exception {
        final String hello = "hello";
        doReturn(hello).when(valueFormatter).formatRegularValue(ArgumentMatchers.any(PkuLevel.class));

        final String s = presenter.formatValue(PkuLevel.create(100f, PkuLevelUnits.MICRO_MOL));
        assertEquals(s, hello);
    }

    @Test
    public void testChangeValues() throws Exception {
        doReturn(Completable.complete()).when(rangeInteractor).saveDisplayUnit(ArgumentMatchers.any(PkuLevelUnits.class));
        final String testValue = "hello";
        doReturn(testValue).when(valueFormatter).getFormattedLow(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).getFormattedHigh(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).getFormattedVeryHigh(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).formatRegularValue(ArgumentMatchers.any(PkuLevel.class));

        final PkuRangeInfo rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_NORMAL_FLOOR)
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();

        final float mmolFloor = 100f;
        final float mmolCeil = 300f;
        presenter.changeValues(mmolFloor, mmolCeil, PkuLevelUnits.MICRO_MOL);

        verify(rangeInteractor).saveDisplayUnit(PkuLevelUnits.MICRO_MOL);

        final RangeSettingsModel model = RangeSettingsModel.builder()
                .setLowText(valueFormatter.getFormattedLow(rangeInfo))
                .setHighText(valueFormatter.getFormattedHigh(rangeInfo))
                .setVeryHighText(valueFormatter.getFormattedVeryHigh(rangeInfo))
                .setNormalFloorMMolValue(mmolFloor)
                .setNormalCeilMMolValue(mmolCeil)
                .setNormalAbsoluteFloorMMolValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteCeilMMolValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .setSelectedUnit(rangeInfo.getPkuLevelUnit())
                .build();

        verify(view).displayRangeSettings(model);
    }

    @Test
    public void testSaveValues() throws Exception {
        doReturn(Completable.complete()).when(rangeInteractor).saveNormalRange(ArgumentMatchers.any(PkuLevel.class), ArgumentMatchers.any(PkuLevel.class));

        final float floor = 100f;
        final float ceil = 300f;
        presenter.saveNormalRange(floor, ceil);

        verify(rangeInteractor).saveNormalRange(PkuLevel.create(floor, PkuLevelUnits.MICRO_MOL), PkuLevel.create(ceil, PkuLevelUnits.MICRO_MOL));
        verify(view).finish();
    }

    @Test
    public void testOnBackDoesntPopDialog() throws Exception {
        final PkuRangeInfo rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_NORMAL_FLOOR)
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();
        doReturn(Single.just(rangeInfo)).when(rangeInteractor).getInfo();

        presenter.onBackPressed(Constants.DEFAULT_PKU_NORMAL_FLOOR, Constants.DEFAULT_PKU_NORMAL_CEIL);

        verify(view).finish();
    }

    @Test
    public void testOnBackPopsDialog() throws Exception {
        final PkuRangeInfo rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_NORMAL_FLOOR)
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();
        doReturn(Single.just(rangeInfo)).when(rangeInteractor).getInfo();

        presenter.onBackPressed(234f, 555f);

        verify(view).showSaveChangesDialog();
    }

}
