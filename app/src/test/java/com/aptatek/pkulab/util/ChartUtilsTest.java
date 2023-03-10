package com.aptatek.pkulab.util;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static com.aptatek.pkulab.util.ChartUtils.State;
import static com.aptatek.pkulab.util.ChartUtils.smallBubbleBackground;
import static com.aptatek.pkulab.util.ChartUtils.stateColor;
import static org.junit.Assert.assertEquals;

/**
 * @test.layer Util
 * @test.feature ChartUtils operations
 * @test.type Unit tests
 */
public class ChartUtilsTest {

    private PkuRangeInfo rangeInfo;

    /**
     * Setting up the required variable
     */
    @Before
    public void before() {
        rangeInfo = PkuRangeInfo.builder()
                .setHighCeilValue(PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_INCREASED_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue())
                .setNormalCeilValue(PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_INCREASED_CEIL, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue())
                .setNormalFloorValue(PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_INCREASED_FLOOR, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue())
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .setNormalAbsoluteMinValue(PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_LOWEST_VALUE, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue())
                .setNormalAbsoluteMaxValue(PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_HIGHEST_VALUE, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue())
                .build();
    }

    /**
     * Testing bubble's states.
     *
     * @test.expected The values are equals, no errors.
     */
    @Test
    public void testChartState() {
        try {
            ChartUtils.getState(PkuLevel.create(-10f, PkuLevelUnits.MICRO_MOL), rangeInfo);
            Assert.fail("should have thrown exception");
        } catch (IllegalArgumentException ex) {
            // good boy
        }
        assertEquals(State.STANDARD, ChartUtils.getState(PkuLevel.create(0f, PkuLevelUnits.MICRO_MOL), rangeInfo));
        assertEquals(State.INCREASED, ChartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_INCREASED_FLOOR, PkuLevelUnits.MICRO_MOL), rangeInfo));
        assertEquals(State.INCREASED, ChartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_INCREASED_CEIL, PkuLevelUnits.MICRO_MOL), rangeInfo));
        assertEquals(State.HIGH, ChartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_HIGH_RANGE - 20f + Constants.DEFAULT_PKU_INCREASED_CEIL, PkuLevelUnits.MICRO_MOL), rangeInfo));
        assertEquals(State.VERY_HIGH, ChartUtils.getState(PkuLevel.create(Constants.DEFAULT_PKU_HIGH_RANGE + 50f + Constants.DEFAULT_PKU_INCREASED_CEIL, PkuLevelUnits.MICRO_MOL), rangeInfo));
    }

    /**
     * Testing small bubble's background colors.
     *
     * @test.expected The values are equals, no errors.
     */
    @Test
    public void testSmallBubbleBackground() {
        assertEquals(R.drawable.bubble_full_low, smallBubbleBackground(State.STANDARD));
        assertEquals(R.drawable.bubble_full_normal, smallBubbleBackground(State.INCREASED));
        assertEquals(R.drawable.bubble_full_high, smallBubbleBackground(State.HIGH));
        assertEquals(R.drawable.bubble_full_very_high, smallBubbleBackground(State.VERY_HIGH));
    }

    /**
     * Testing state's colors.
     *
     * @test.expected The values are equals, no errors.
     */
    @Test
    public void testStateColor() {
        assertEquals(R.color.pkuLevelStandard, stateColor(State.STANDARD));
        assertEquals(R.color.pkuLevelIncreased, stateColor(State.INCREASED));
        assertEquals(R.color.pkuLevelHigh, stateColor(State.HIGH));
        assertEquals(R.color.pkuLevelVeryHigh, stateColor(State.VERY_HIGH));
    }


    @Test
    public void testChartCoordinates() {
        //TODO
    }
}
