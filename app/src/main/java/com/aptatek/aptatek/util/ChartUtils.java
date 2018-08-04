package com.aptatek.aptatek.util;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.device.time.TimeHelper;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.aptatek.domain.model.CubeData;
import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.aptatek.aptatek.view.main.adapter.ChartVM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ix.GroupedIx;
import ix.Ix;
import timber.log.Timber;

public final class ChartUtils {

    public enum State {LOW, NORMAL, HIGH, VERY_HIGH}

    private ChartUtils() {
        // hidden ctor
    }

    @NonNull
    public static List<ChartVM> asChartVMList(final List<CubeData> inputList, final PkuRangeInfo rangeInfo) {
        final List<ChartVM> chartVms = new ArrayList<>();

        final Comparator<CubeData> cubeDataComparator = (p1, p2) -> Float.compare(p1.getPkuLevel().getValue(), p2.getPkuLevel().getValue());
        final Comparator<ChartVM> chartVMComparator = (first, second) -> Long.compare(first.getDate().getTime(), second.getDate().getTime());

        if (inputList == null || inputList.size() == 0) {
            return chartVms;
        }

        final float maxLevel = Ix.from(inputList)
                .filter(cubeData -> cubeData.getPkuLevel().getValue() >= 0)
                .max(cubeDataComparator)
                .first()
                .getPkuLevel().getValue();
        final float minLevel = Ix.from(inputList)
                .filter(cubeData -> cubeData.getPkuLevel().getValue() >= 0)
                .min(cubeDataComparator)
                .first()
                .getPkuLevel().getValue();
        final float delta = maxLevel - minLevel;

        // the oldest measurement data will be the very first bubble on the graph
        final long timestampOldestDay = Ix.from(inputList)
                .filter(cubeData -> cubeData.getPkuLevel().getValue() >= 0)
                .min((first, second) -> Long.compare(first.getTimestamp(), second.getTimestamp()))
                .map(CubeData::getTimestamp)
                .map(TimeHelper::getEarliestTimeAtGivenDay)
                .first();
        final long nowInDay = TimeHelper.getEarliestTimeAtGivenDay(System.currentTimeMillis());
        final int daysBetween = TimeHelper.getDaysBetween(timestampOldestDay, nowInDay);
        // create an initial map to hold the data for each days passed between today and the first measurement
        // each value is an empty list by default
        final Map<Long, List<CubeData>> dayToMeasurementsMap = Ix.range(0, daysBetween)
                .map(day -> TimeHelper.getEarliestTimeAtGivenDay(TimeHelper.addDays(day, timestampOldestDay)))
                .map(day -> new Pair<Long, List<CubeData>>(day, new ArrayList<>()))
                .toMap(pair -> pair.first, pair -> pair.second);

        // add the grouped real data to the initial map which holds all days passed since the first measurement
        Ix.from(inputList)
                .map(cubeData -> new Pair<>(TimeHelper.getEarliestTimeAtGivenDay(cubeData.getTimestamp()), cubeData))
                .foreach(pair -> {
                    if (dayToMeasurementsMap.containsKey(pair.first)) {
                        dayToMeasurementsMap.get(pair.first).add(pair.second);
                    } else {
                        dayToMeasurementsMap.put(pair.first, Collections.singletonList(pair.second));
                    }
                });

        for (final Map.Entry<Long, List<CubeData>> entry : dayToMeasurementsMap.entrySet()) {
            final List<CubeData> dailyMeasures = entry.getValue();
            final CubeData dailyHighest = Ix.from(dailyMeasures)
                    .max(cubeDataComparator)
                    .first(null);

            float startY = -1; // start Y-axis of dashed-line on chart,  if less than 0, won't be drawn
            float endY = -1; // end Y-axis of dashed-line on chart,  if less than 0, won't be drawn
            final float bubbleY = getY(dailyHighest, delta, minLevel); // Y-axis of bubble

            final long prevDay = TimeHelper.getEarliestTimeAtGivenDay(TimeHelper.addDays(-1, entry.getKey()));
            if (dayToMeasurementsMap.containsKey(prevDay)) {
                final List<CubeData> prevDayMeasures = dayToMeasurementsMap.get(prevDay);
                final CubeData nextDayHighest = Ix.from(prevDayMeasures)
                        .max(cubeDataComparator)
                        .first(null);
                final float prevBubbleY = getY(nextDayHighest, delta, minLevel);
                // calculate the current item end-line Y-height with the current and next item Y-height
                endY = (bubbleY + prevBubbleY) / 2;
            }

            final long nextDay = TimeHelper.getEarliestTimeAtGivenDay(TimeHelper.addDays(1, entry.getKey()));
            if (dayToMeasurementsMap.containsKey(nextDay)) { // if it's not the last day
                // get the the measurements for the next day
                final List<CubeData> nextDayMeasures = dayToMeasurementsMap.get(nextDay);
                final CubeData nextDayHighest = Ix.from(nextDayMeasures)
                        .max(cubeDataComparator)
                        .first(null);
                final float nextBubbleY = getY(nextDayHighest, delta, minLevel);
                // calculate the current item end-line Y-height with the current and next item Y-height
                endY = (bubbleY + nextBubbleY) / 2;
            }

            final State state;
            if (dailyHighest != null) {
                 state = ChartUtils.getState(dailyHighest.getPkuLevel(), rangeInfo);
            } else {
                state = State.LOW;
            }

            final ChartVM chartVm = ChartVM.builder()
                    .setDate(new Date(entry.getKey()))
                    .setMeasures(entry.getValue())
                    .setNumberOfMeasures(entry.getValue().size())
                    .setZoomed(false)
                    .setBubbleYAxis(bubbleY)
                    .setStartLineYAxis(startY)
                    .setEndLineYAxis(endY)
                    .setHighestPkuLevel(dailyHighest == null ? null : dailyHighest.getPkuLevel())
                    .setCollapsedBackgroundRes(ChartUtils.smallBubbleBackground(state))
                    .setExpandedBackgroundRes(ChartUtils.bigBubbleBackground(state))
                    .setColorRes(ChartUtils.stateColor(state))
                    .build();

            chartVms.add(chartVm);
        }

        return Ix.from(chartVms)
                .orderBy(chartVMComparator)
                .toList();
    }

    private static float getY(final CubeData cubeData, float delta, float minLevel) {
        if (cubeData != null && cubeData.getPkuLevel().getValue() >= 0 && delta > 0) {
            // calculate the Y percentage of the bubble
            return (1 - (cubeData.getPkuLevel().getValue() - minLevel) / delta);
        } else {
            // if there is no measure on the given day, set Y to half of the Y-axis
            return 0.5f;
        }
    }

    public static PkuLevel convertToDisplayUnit(final PkuLevel pkuLevel, final PkuRangeInfo userRange) {
        final PkuLevel levelInProperUnit;
        if (userRange.getPkuLevelUnit() != pkuLevel.getUnit()) {
            levelInProperUnit = PkuLevelConverter.convertTo(pkuLevel, userRange.getPkuLevelUnit());
        } else {
            levelInProperUnit = pkuLevel;
        }

        return levelInProperUnit;
    }

    public static State getState(final PkuLevel pkuLevel, final PkuRangeInfo userRange) {
        final PkuLevel levelInProperUnit = convertToDisplayUnit(pkuLevel, userRange);
        final State chartState;

        if (0f > levelInProperUnit.getValue()) {
            throw new IllegalArgumentException("Invalid pku value: " + pkuLevel);
        } else if (userRange.getNormalFloorValue() > levelInProperUnit.getValue()) {
            chartState = State.LOW;
        } else if (userRange.getNormalFloorValue() <= levelInProperUnit.getValue() && levelInProperUnit.getValue() <= userRange.getNormalCeilValue()) {
            chartState = State.NORMAL;
        } else if (userRange.getNormalCeilValue() < levelInProperUnit.getValue() && levelInProperUnit.getValue() <= userRange.getHighCeilValue()) {
            chartState = State.HIGH;
        } else {
            chartState = State.VERY_HIGH;
        }

        return chartState;
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

}
