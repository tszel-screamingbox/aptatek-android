package com.aptatek.pkuapp.util;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuLevelUnits;
import com.aptatek.pkuapp.domain.model.PkuRangeInfo;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static com.aptatek.pkuapp.util.ChartUtils.State;
import static com.aptatek.pkuapp.util.ChartUtils.bigBubbleBackground;
import static com.aptatek.pkuapp.util.ChartUtils.smallBubbleBackground;
import static com.aptatek.pkuapp.util.ChartUtils.stateColor;
import static org.junit.Assert.assertEquals;

public class ChartUtilsTest {

    private PkuRangeInfo rangeInfo;

    @Before
    public void before() throws Exception {
        rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_NORMAL_FLOOR)
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();
    }

    @Test
    public void testChartState() {
        try {
            ChartUtils.getState(PkuLevel.create(-10f, PkuLevelUnits.MICRO_MOL), rangeInfo);
            Assert.fail("should have thrown exception");
        } catch (IllegalArgumentException ex) {
            // good boy
        }
        assertEquals(State.LOW, ChartUtils.getState(PkuLevel.create(0f, PkuLevelUnits.MICRO_MOL), rangeInfo));
        assertEquals(State.NORMAL, ChartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_NORMAL_FLOOR, PkuLevelUnits.MICRO_MOL), rangeInfo));
        assertEquals(State.NORMAL, ChartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_NORMAL_CEIL, PkuLevelUnits.MICRO_MOL), rangeInfo));
        assertEquals(State.HIGH, ChartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_HIGH_RANGE - 20f + Constants.DEFAULT_PKU_NORMAL_CEIL, PkuLevelUnits.MICRO_MOL), rangeInfo));
        assertEquals(State.VERY_HIGH, ChartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_HIGH_RANGE + 50f + Constants.DEFAULT_PKU_NORMAL_CEIL, PkuLevelUnits.MICRO_MOL), rangeInfo));
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
        assertEquals(R.color.pkuLevelLow, stateColor(State.LOW));
        assertEquals(R.color.pkuLevelNormal, stateColor(State.NORMAL));
        assertEquals(R.color.pkuLevelHigh, stateColor(State.HIGH));
        assertEquals(R.color.pkuLevelVeryHigh, stateColor(State.VERY_HIGH));
    }


    @Test
    public void testChartCoordinates() {
        //TODO
    }
}
