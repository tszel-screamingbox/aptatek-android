package com.aptatek.aptatek.util;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

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

public class ChartUtils {

    private static final int RANGE = 5;
    // Default measure levels
    private static final int NORMAL = 100;
    private static final int HIGH = 350;
    private static final int VERY_HIGH = 500;

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
            float startY = -1; // start Y-axis of dashed-line on chart,  if less than 0, won't be drawn
            float endY = -1; // end Y-axis of dashed-line on chart,  if less than 0, won't be drawn
            final float bubbleY = getY(cubeData); // Y-axis of bubble

            if (i > 0 && inputList.get(i - 1) != null) {
                // if there were previously calculated element, than set its end-line Y-height to the next item start-line Y-height
                startY = chartDTOList.get(i - 1).getEndLineYAxis();
            }

            if (inputList.size() - 1 != i) { // if it's not the last item in the list
                final float nextBubbleY;
                if (inputList.get(i + 1).getMeasuredLevel() >= 0) {
                    //calculate the next bubble Y-height
                    nextBubbleY = getY(inputList.get(i + 1));
                } else {
                    // if there is no measure on the next day, set Y-height to half of the Y-axis
                    nextBubbleY = 0.5f;
                }
                // calculate the current item end-line Y-height with the current and next item Y-height
                endY = (bubbleY + nextBubbleY) / 2;
            }

            final List<Measure> measureList = new ArrayList<>();
            if (cubeData.getMeasuredLevel() >= 0) {
                // if the item has one measure at least, generate a random sized list
                measureList.addAll(randomMeasureList(cubeData));
            }

            chartDTOList.add(ChartDTO.create(cubeData.getId(), cubeData.getDate(), measureList, bubbleY, startY, endY));
        }

        return Ix.from(chartDTOList)
                .map(chartDTO -> {
                    Measure highest = null;
                    if (chartDTO.getMeasureList() != null && !chartDTO.getMeasureList().isEmpty()) {
                        final Comparator<Measure> comp1 = (p1, p2) -> Integer.compare(p1.getPhenylalanineLevel(), p2.getPhenylalanineLevel());
                        highest = Ix.from(chartDTO.getMeasureList())
                                .max(comp1)
                                .first();
                    }

                    return ChartVM.builder()
                            .setId(chartDTO.getId())
                            .setDate(chartDTO.getDate())
                            .setHighestMeasure(highest)
                            .setBubbleYAxis(chartDTO.getBubbleYAxis())
                            .setStartLineYAxis(chartDTO.getStartLineYAxis())
                            .setEndLineYAxis(chartDTO.getEndLineYAxis())
                            .setNumberOfMeasures(chartDTO.getMeasureList() != null ? chartDTO.getMeasureList().size() : 0)
                            .setMeasures(chartDTO.getMeasureList())
                            .setZoomed(false)
                            .build();
                })
                .toList();
    }

    public static State getState(final int phenylalanineLevel) {
        if (0 <= phenylalanineLevel && phenylalanineLevel < NORMAL) {
            return State.LOW;
        } else if (NORMAL <= phenylalanineLevel && phenylalanineLevel < HIGH) {
            return State.NORMAL;
        } else if (HIGH <= phenylalanineLevel && phenylalanineLevel < VERY_HIGH) {
            return State.HIGH;
        } else if (VERY_HIGH <= phenylalanineLevel) {
            return State.VERY_HIGH;
        }
        return State.NORMAL;
    }

    @DrawableRes
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

    @ColorRes
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

    @DrawableRes
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
        //Generates random measue data for a given day
        final Random random = new Random();
        if (random.nextBoolean()) {
            // 2 or more measure on the same day
            final List<Measure> measureList = new ArrayList<>();
            for (int i = 0; i < random.nextInt(RANGE); i++) {
                measureList.add(Measure.create(cubeData.getMeasuredLevel() - i, cubeData.getUnit()));
            }
            return measureList;
        } else {
            return Collections.singletonList(Measure.create(cubeData.getMeasuredLevel(), cubeData.getUnit()));
        }
    }


    private float getY(final CubeData cubeData) {
        if (cubeData.getMeasuredLevel() >= 0 && delta > 0) {
            // calculate the Y percentage of the bubble
            return (1 - (cubeData.getMeasuredLevel() - minLevel) / delta);
        } else {
            // if there is no measure on the givan day, set Y to half of the Y-axis
            return 0.5f;
        }
    }
}
