package com.aptatek.aptatek.util;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.respository.chart.CubeData;
import com.aptatek.aptatek.view.main.adapter.ChartVM;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        final List<CubeData> cubeDataList = new ArrayList<>();
        cubeDataList.add(new CubeData(0, new Date(), 0, 0));
        cubeDataList.add(new CubeData(0, new Date(), 100, 0));
        cubeDataList.add(new CubeData(0, new Date(), 100, 0));
        cubeDataList.add(new CubeData(0, new Date(), 50, 0));
        cubeDataList.add(new CubeData(0, new Date(), -1, 0));

        final List<ChartVM> chartVMList = chartUtils.asChartVMList(cubeDataList);
        assertEquals(-1, chartVMList.get(0).getStartLineYAxis(),0);
        assertEquals(0.5, chartVMList.get(0).getEndLineYAxis(),0);

        assertEquals(0.5, chartVMList.get(1).getStartLineYAxis(),0);
        assertEquals(0, chartVMList.get(1).getEndLineYAxis(),0);

        assertEquals(0, chartVMList.get(2).getStartLineYAxis(),0);
        assertEquals(0.25, chartVMList.get(2).getEndLineYAxis(),0);

        assertEquals(0.25, chartVMList.get(3).getStartLineYAxis(),0);
        assertEquals(0.5, chartVMList.get(3).getEndLineYAxis(),0);

        assertEquals(0.5, chartVMList.get(4).getStartLineYAxis(),0);
        assertEquals(-1, chartVMList.get(4).getEndLineYAxis(),0);
    }
}
