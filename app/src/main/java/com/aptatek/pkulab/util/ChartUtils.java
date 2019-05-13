package com.aptatek.pkulab.util;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.util.Pair;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.view.main.home.adapter.chart.ChartVM;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ix.Ix;

public final class ChartUtils {

    public enum State {STANDARD, INCREASED, HIGH, VERY_HIGH}

    private ChartUtils() {
        // hidden ctor
    }

    @NonNull
    public static List<ChartVM> asChartVMList(final List<TestResult> inputList, final PkuRangeInfo rangeInfo) {
        final List<ChartVM> chartVms = new ArrayList<>();

        final Comparator<TestResult> cubeDataComparator = (p1, p2) -> Float.compare(p1.getPkuLevel().getValue(), p2.getPkuLevel().getValue());
        final Comparator<ChartVM> chartVMComparator = (first, second) -> Long.compare(first.getDate().getTime(), second.getDate().getTime());

        if (inputList == null || inputList.size() == 0) {
            return chartVms;
        }

        // the oldest measurement data will be the very first bubble on the graph
        final long timestampOldestDay = Ix.from(inputList)
                .filter(cubeData -> cubeData.getPkuLevel().getValue() >= 0)
                .min((first, second) -> Long.compare(first.getTimestamp(), second.getTimestamp()))
                .map(TestResult::getTimestamp)
                .map(TimeHelper::getEarliestTimeAtGivenDay)
                .first();
        final long nowInDay = TimeHelper.getEarliestTimeAtGivenDay(System.currentTimeMillis());
        final int daysBetween = TimeHelper.getDaysBetween(timestampOldestDay, nowInDay);
        // create an initial map to hold the data for each days passed between today and the first measurement
        // each value is an empty list by default
        final Map<Long, List<TestResult>> dayToMeasurementsMap = Ix.range(0, daysBetween + 1)
                .map(day -> TimeHelper.getEarliestTimeAtGivenDay(TimeHelper.addDays(day, timestampOldestDay)))
                .map(day -> new Pair<Long, List<TestResult>>(day, new ArrayList<>()))
                .toMap(pair -> pair.first, pair -> pair.second);

        // add the grouped real data to the initial map which holds all days passed since the first measurement
        Ix.from(inputList)
                .map(testResult -> new Pair<>(TimeHelper.getEarliestTimeAtGivenDay(testResult.getTimestamp()), testResult))
                .foreach(pair -> {
                    if (dayToMeasurementsMap.containsKey(pair.first)) {
                        dayToMeasurementsMap.get(pair.first).add(pair.second);
                    } else {
                        final List<TestResult> testResults = new ArrayList<>();
                        testResults.add(pair.second);
                        dayToMeasurementsMap.put(pair.first, testResults);
                    }
                });

        for (final Map.Entry<Long, List<TestResult>> entry : dayToMeasurementsMap.entrySet()) {
            final List<TestResult> dailyMeasures = entry.getValue();
            final TestResult dailyHighest = Ix.from(dailyMeasures)
                    .max(cubeDataComparator)
                    .first(null);

            if (dailyHighest == null) {
                continue;
            }

            final State state = getState(dailyHighest.getPkuLevel(), rangeInfo);
            final ChartVM chartVm = ChartVM.builder()
                    .setDate(new Date(dailyHighest.getTimestamp()))
                    .setMeasures(entry.getValue())
                    .setNumberOfMeasures(entry.getValue().size())
                    .setZoomed(false)
                    .setState(stateString(state))
                    .setHighestPkuLevel(dailyHighest.getPkuLevel())
                    .setColorRes(stateColor(state))
                    .build();

            chartVms.add(chartVm);
        }

        return Ix.from(chartVms)
                .orderBy(chartVMComparator)
                .toList();
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
            chartState = State.STANDARD;
        } else if (userRange.getNormalFloorValue() <= levelInProperUnit.getValue() && levelInProperUnit.getValue() <= userRange.getNormalCeilValue()) {
            chartState = State.INCREASED;
        } else if (userRange.getNormalCeilValue() < levelInProperUnit.getValue() && levelInProperUnit.getValue() <= userRange.getHighCeilValue()) {
            chartState = State.HIGH;
        } else {
            chartState = State.VERY_HIGH;
        }

        return chartState;
    }

    @StringRes
    public static int stateString(final State state) {
        switch (state) {
            case STANDARD:
                return R.string.test_result_bubble_level_standard;
            case INCREASED:
                return R.string.test_result_bubble_level_increased;
            case HIGH:
                return R.string.test_result_bubble_level_high;
            case VERY_HIGH:
                return R.string.test_result_bubble_level_veryhigh;
            default:
                return R.string.test_result_bubble_level_increased;
        }
    }

    @DrawableRes
    public static int smallBubbleBackground(final State state) {
        switch (state) {
            case STANDARD:
                return R.drawable.bubble_full_low;
            case INCREASED:
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
            case STANDARD:
                return R.color.pkuLevelStandard;
            case INCREASED:
                return R.color.pkuLevelIncreased;
            case HIGH:
                return R.color.pkuLevelHigh;
            case VERY_HIGH:
                return R.color.pkuLevelVeryHigh;
            default:
                return R.color.pkuLevelIncreased;
        }
    }

}
