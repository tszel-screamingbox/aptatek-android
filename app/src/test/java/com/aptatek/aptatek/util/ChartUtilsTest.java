package com.aptatek.aptatek.util;

import com.aptatek.aptatek.R;

import org.junit.Before;
import org.junit.Test;

import static com.aptatek.aptatek.util.ChartUtils.State;
import static com.aptatek.aptatek.util.ChartUtils.bigBubbleBackground;
import static com.aptatek.aptatek.util.ChartUtils.getState;
import static com.aptatek.aptatek.util.ChartUtils.smallBubbleBackground;
import static com.aptatek.aptatek.util.ChartUtils.stateColor;
import static org.junit.Assert.assertEquals;

public class ChartUtilsTest {

    private ChartUtils chartUtils;

    @Before
    public void setUp() {
        chartUtils = new ChartUtils();
    }

    @Test
    public void testChartState() {
        assertEquals(State.NORMAL, getState(-10));
        assertEquals(State.LOW, getState(0));
        assertEquals(State.NORMAL, getState(100));
        assertEquals(State.NORMAL, getState(300));
        assertEquals(State.HIGH, getState(350));
        assertEquals(State.VERY_HIGH, getState(500));
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
