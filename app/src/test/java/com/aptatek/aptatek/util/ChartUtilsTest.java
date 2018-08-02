package com.aptatek.aptatek.util;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsValueFormatter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static com.aptatek.aptatek.util.ChartUtils.State;
import static com.aptatek.aptatek.util.ChartUtils.bigBubbleBackground;
import static com.aptatek.aptatek.util.ChartUtils.smallBubbleBackground;
import static com.aptatek.aptatek.util.ChartUtils.stateColor;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class ChartUtilsTest {

    private ChartUtils chartUtils;

    @Mock
    PkuRangeInteractor rangeInteractor;

    @Mock
    RangeSettingsValueFormatter formatter;

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
        when(rangeInteractor.getInfo()).thenReturn(Single.just(rangeInfo));
        doReturn("hello").when(formatter).formatRegularValue(ArgumentMatchers.any(PkuLevel.class));
        chartUtils = new ChartUtils(rangeInteractor, formatter);
    }

    @Test
    public void testChartState() {
        try {
            chartUtils.getState(PkuLevel.create(-10f, PkuLevelUnits.MICRO_MOL));
            Assert.fail("should have thrown exception");
        } catch (IllegalArgumentException ex) {
            // good boy
        }
        assertEquals(State.LOW, chartUtils.getState(PkuLevel.create(0f, PkuLevelUnits.MICRO_MOL)));
        assertEquals(State.NORMAL, chartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_NORMAL_FLOOR, PkuLevelUnits.MICRO_MOL)));
        assertEquals(State.NORMAL, chartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_NORMAL_CEIL, PkuLevelUnits.MICRO_MOL)));
        assertEquals(State.HIGH, chartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_HIGH_RANGE - 20f + Constants.DEFAULT_PKU_NORMAL_CEIL, PkuLevelUnits.MICRO_MOL)));
        assertEquals(State.VERY_HIGH, chartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_HIGH_RANGE + 50f + Constants.DEFAULT_PKU_NORMAL_CEIL, PkuLevelUnits.MICRO_MOL)));
    }

    @Test
    public void testSmallBubbleBackground() {
        assertEquals(R.drawable.bubble_full_low, smallBubbleBackground(State.LOW));
        assertEquals(R.drawable.bubble_full_normal, smallBubbleBackground(State.NORMAL));
        assertEquals(R.drawable.bubble_full_high, smallBubbleBackground(State.HIGH));
        assertEquals(R.drawable.bubble_full_very_high, smallBubbleBackground(State.VERY_HIGH));
    }

    @Test
    public void testBigBubbleBackground() {
        assertEquals(R.drawable.bubble_big_low, bigBubbleBackground(State.LOW));
        assertEquals(R.drawable.bubble_big_normal, bigBubbleBackground(State.NORMAL));
        assertEquals(R.drawable.bubble_big_high, bigBubbleBackground(State.HIGH));
        assertEquals(R.drawable.bubble_big_very_high, bigBubbleBackground(State.VERY_HIGH));
    }

    @Test
    public void testStateColor() {
        assertEquals(R.color.chartBubbleLow, stateColor(State.LOW));
        assertEquals(R.color.chartBubbleNormal, stateColor(State.NORMAL));
        assertEquals(R.color.chartBubbleHigh, stateColor(State.HIGH));
        assertEquals(R.color.chartBubbleVeryHigh, stateColor(State.VERY_HIGH));
    }


    @Test
    public void testChartCoordinates() {
        //TODO
    }
}
