package com.aptatek.aptatek.util;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.respository.chart.ChartDTO;
import com.aptatek.aptatek.domain.respository.chart.CubeData;
import com.aptatek.aptatek.domain.respository.chart.Measure;
import com.aptatek.aptatek.view.main.adapter.ChartVM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import ix.Ix;

import static com.aptatek.aptatek.util.ChartUtils.State.HIGH;
import static com.aptatek.aptatek.util.ChartUtils.State.LOW;
import static com.aptatek.aptatek.util.ChartUtils.State.NORMAL;
import static com.aptatek.aptatek.util.ChartUtils.State.VERY_HIGH;

public class ChartUtils {

    private static final int RANGE = 10;

    private final List<ChartDTO> chartDTOList;
    private float minLevel;
    private float delta;


    public enum State {LOW, NORMAL, HIGH, VERY_HIGH}

    @Inject
    public ChartUtils() {
        chartDTOList = new ArrayList<>();
    }

    public List<ChartVM> asChartVMList(final List<CubeData> inputList) {
        final Comparator<CubeData> comp = (p1, p2) -> Integer.compare(p1.getMeasuredLevel(), p2.getMeasuredLevel());

        if (inputList == null || inputList.size() == 0) {
            return null;
        }

        final float maxLevel = Ix.from(inputList)
                .filter(cubeData -> cubeData.getMeasuredLevel() >= 0)
                .max(comp)
                .first()
                .getMeasuredLevel();
        minLevel = Ix.from(inputList)
                .filter(cubeData -> cubeData.getMeasuredLevel() >= 0)
                .min(comp)
                .first()
                .getMeasuredLevel();
        delta = maxLevel - minLevel;

        for (int i = 0; i < inputList.size(); i++) {
            final CubeData cubeData = inputList.get(i);
            float startY = -1;
            float endY = -1;
            final float bubbleY = getY(cubeData);

            if (i > 0 && inputList.get(i - 1) != null) {
                startY = chartDTOList.get(i - 1).getEndLineYAxis();
            }

            if (inputList.size() - 1 != i) {
                final float nextBubbleY;
                if (inputList.get(i + 1).getMeasuredLevel() >= 0) {
                    nextBubbleY = getY(inputList.get(i + 1));
                } else {
                    nextBubbleY = 0.5f;
                }

                endY = (bubbleY + nextBubbleY) / 2;
            }

            final List<Measure> measureList = new ArrayList<>();
            if (cubeData.getMeasuredLevel() >= 0) {
                measureList.addAll(randomMeasureList(cubeData));
            }

            chartDTOList.add(new ChartDTO(cubeData.getId(), cubeData.getDate(), measureList, bubbleY, startY, endY));
        }

        return Ix.from(chartDTOList)
                .map(ChartVM::new)
                .toList();
    }

    public static State getState(final int phenylalanineLevel) {
        if (0 <= phenylalanineLevel && phenylalanineLevel < 100) {
            return LOW;
        } else if (100 <= phenylalanineLevel && phenylalanineLevel < 350) {
            return NORMAL;
        } else if (350 <= phenylalanineLevel && phenylalanineLevel < 500) {
            return HIGH;
        } else if (500 <= phenylalanineLevel) {
            return VERY_HIGH;
        }
        return NORMAL;
    }

    public static int smallBubbleBackground(final State state) {
        switch (state) {
            case LOW:
                return R.drawable.bubble_full_low;
            case NORMAL:
                return R.drawable.bubble_full_normal;
            case HIGH:
                return R.drawable.bubble_full_high;
            case VERY_HIGH:
                return R.drawable.bubble_full_very_high;
            default:
                return R.drawable.bubble_full_normal;
        }
    }

    public static int stateColor(final State state) {
        switch (state) {
            case LOW:
                return R.color.chartBubbleLow;
            case NORMAL:
                return R.color.chartBubbleNormal;
            case HIGH:
                return R.color.chartBubbleHigh;
            case VERY_HIGH:
                return R.color.chartBubbleVeryHigh;
            default:
                return R.color.chartBubbleNormal;
        }
    }

    public static int bigBubbleBackground(final State state) {
        switch (state) {
            case LOW:
                return R.drawable.bubble_big_low;
            case NORMAL:
                return R.drawable.bubble_big_normal;
            case HIGH:
                return R.drawable.bubble_big_high;
            case VERY_HIGH:
                return R.drawable.bubble_big_very_high;
            default:
                return R.drawable.bubble_big_normal;
        }
    }


    private List<Measure> randomMeasureList(final CubeData cubeData) {
        final Random random = new Random();
        final int chance = random.nextInt(10);
        if (chance > 5) {
            final List<Measure> measureList = new ArrayList<>();
            for (int i = 0; i < RANGE - chance; i++) {
                measureList.add(new Measure(cubeData.getMeasuredLevel() - i, cubeData.getUnit()));
            }
            return measureList;
        } else {
            return Collections.singletonList(new Measure(cubeData.getMeasuredLevel(), cubeData.getUnit()));
        }
    }


    private float getY(final CubeData cubeData) {
        if (cubeData.getMeasuredLevel() >= 0 && delta > 0) {
            return (1 - (cubeData.getMeasuredLevel() - minLevel) / delta);
        } else {
            return 0.5f;
        }
    }
}