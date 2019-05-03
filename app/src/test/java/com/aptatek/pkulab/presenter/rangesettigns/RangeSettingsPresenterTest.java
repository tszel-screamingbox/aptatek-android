package com.aptatek.pkulab.presenter.rangesettigns;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsModel;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsPresenter;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsValueFormatter;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsView;

import org.junit.AfterClass;
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

/**
 * Tests for the RangeSettingsPresenter class
 *
 * @test.layer presentation
 * @test.feature RangeSettings
 * @test.type unit
 */
public class RangeSettingsPresenterTest {

    private RangeSettingsPresenter presenter;

    @Mock
    PkuRangeInteractor rangeInteractor;

    @Mock
    RangeSettingsValueFormatter valueFormatter;

    @Mock
    RangeSettingsView view;

    @BeforeClass
    public static void beforeClass() {
        final Scheduler immediate = new Scheduler() {

            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new RangeSettingsPresenter(rangeInteractor, valueFormatter);
        presenter.attachView(view);
    }

    @AfterClass
    public static void afterClass() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    /**
     * Tests the proper behavior: the refresh() methods should get the data from the PkuRangeInteractor then format it via the RangeSettingsValueFormatter to be rendered on UI.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testRefresh() {
        final PkuRangeInfo rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_INCREASED_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_INCREASED_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_INCREASED_FLOOR)
                .setPkuLevelUnit(PkuLevelUnits.MICRO_MOL)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();
        doReturn(Single.just(rangeInfo)).when(rangeInteractor).getInfo();
        final String testValue = "hello";
        doReturn(testValue).when(valueFormatter).getFormattedLow(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).getFormattedHigh(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).getFormattedVeryHigh(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).getFormattedIncreased(ArgumentMatchers.any(PkuRangeInfo.class));

        presenter.refresh();

        verify(rangeInteractor).getInfo();
        verify(valueFormatter).getFormattedLow(rangeInfo);
        verify(valueFormatter).getFormattedHigh(rangeInfo);
        verify(valueFormatter).getFormattedVeryHigh(rangeInfo);
        verify(valueFormatter).getFormattedIncreased(rangeInfo);

        verify(view).displayRangeSettings(RangeSettingsModel.builder()
                .setLowText(valueFormatter.getFormattedLow(rangeInfo))
                .setHighText(valueFormatter.getFormattedHigh(rangeInfo))
                .setIncreasedText(valueFormatter.getFormattedIncreased(rangeInfo))
                .setVeryHighText(valueFormatter.getFormattedVeryHigh(rangeInfo))
                .setNormalFloorMMolValue(Constants.DEFAULT_PKU_INCREASED_FLOOR)
                .setNormalCeilMMolValue(Constants.DEFAULT_PKU_INCREASED_CEIL)
                .setNormalAbsoluteFloorMMolValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteCeilMMolValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .setSelectedUnit(rangeInfo.getPkuLevelUnit())
                .build());
    }

    /**
     * Tests the proper behavior: the formatValue(PkuLevel) methods should rely on the RangeSettingsValueFormatter to get the data in the proper format to be rendered on UI.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testFormatTest() {
        final String hello = "hello";
        doReturn(hello).when(valueFormatter).formatRegularValue(ArgumentMatchers.any(PkuLevel.class));

        final String s = presenter.formatValue(PkuLevel.create(100f, PkuLevelUnits.MICRO_MOL));
        assertEquals(s, hello);
    }

    /**
     * Tests the proper behavior: the saveDisplayUnit(PkuLevelUnits) methods should save the selected unit as the displayUnit via the interactor then should trigger changes on the UI to re-render data according to the new displayUnit.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testChangeValues() {
        doReturn(Completable.complete()).when(rangeInteractor).saveDisplayUnit(ArgumentMatchers.any(PkuLevelUnits.class));
        final String testValue = "hello";
        doReturn(testValue).when(valueFormatter).getFormattedLow(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).getFormattedHigh(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).getFormattedVeryHigh(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).getFormattedIncreased(ArgumentMatchers.any(PkuRangeInfo.class));
        doReturn(testValue).when(valueFormatter).formatRegularValue(ArgumentMatchers.any(PkuLevel.class));

        final PkuRangeInfo rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_INCREASED_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_INCREASED_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_INCREASED_FLOOR)
                .setPkuLevelUnit(PkuLevelUnits.MICRO_MOL)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();

        final float mmolFloor = 100f;
        final float mmolCeil = 300f;
        presenter.changeValues(mmolFloor, mmolCeil, PkuLevelUnits.MICRO_MOL);

        final RangeSettingsModel model = RangeSettingsModel.builder()
                .setLowText(valueFormatter.getFormattedLow(rangeInfo))
                .setHighText(valueFormatter.getFormattedHigh(rangeInfo))
                .setVeryHighText(valueFormatter.getFormattedVeryHigh(rangeInfo))
                .setIncreasedText(valueFormatter.getFormattedIncreased(rangeInfo))
                .setNormalFloorMMolValue(mmolFloor)
                .setNormalCeilMMolValue(mmolCeil)
                .setNormalAbsoluteFloorMMolValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteCeilMMolValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .setSelectedUnit(rangeInfo.getPkuLevelUnit())
                .build();

        verify(view).displayRangeSettings(model);
    }

    /**
     * Tests the proper behavior: the saveValues(float, float, PkuLevelUnits) method should save normal ranges via the interactor.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testSaveValues() {
        doReturn(Completable.complete()).when(rangeInteractor).saveNormalRange(ArgumentMatchers.any(PkuLevel.class), ArgumentMatchers.any(PkuLevel.class));
        doReturn(Completable.complete()).when(rangeInteractor).saveDisplayUnit(ArgumentMatchers.any(PkuLevelUnits.class));

        presenter.saveValues(PkuLevelUnits.MICRO_MOL);

        verify(view).showSettingsUpdateMessage();
    }

    /**
     * Tests the proper behavior: when the user didn't change the range values then auto save feature should be triggered: UI should not show save changes dialog.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testOnBackDoesntPopDialog() {
        final PkuRangeInfo rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_INCREASED_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_INCREASED_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_INCREASED_FLOOR)
                .setPkuLevelUnit(PkuLevelUnits.MICRO_MOL)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();
        doReturn(Single.just(rangeInfo)).when(rangeInteractor).getInfo();

        presenter.onBackPressed(PkuLevelUnits.MICRO_MOL);

        verify(view).finish();
    }

    /**
     * Tests the proper behavior: when the user has changed the normal range values, auto save feature should be triggered on back press: UI should show a confirmation dialog to save new values.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testOnBackPopsDialog() {
        final PkuRangeInfo rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_INCREASED_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_INCREASED_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_INCREASED_FLOOR)
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();
        doReturn(Single.just(rangeInfo)).when(rangeInteractor).getInfo();

        presenter.onBackPressed(PkuLevelUnits.MICRO_MOL);

        verify(view).showSaveChangesDialog();
    }

}
