package com.aptatek.aptatek.view.weekly.chart;

import com.aptatek.aptatek.device.formatter.WeeklyChartValueFormatter;
import com.aptatek.aptatek.domain.respository.manager.FakeCubeDataManager;
import com.aptatek.aptatek.util.CalendarUtils;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ix.Ix;
import timber.log.Timber;


class WeeklyChartPresenter extends MvpBasePresenter<WeeklyChartView> {

    private static final float SIZE = 0.1f;
    private static final int DAY_OFFSET = 1;

    private final FakeCubeDataManager fakeCubeDataManager;

    @Inject
    WeeklyChartPresenter(final FakeCubeDataManager fakeCubeDataManager) {
        this.fakeCubeDataManager = fakeCubeDataManager;

    }

    BubbleDataSet getChartData(final int weekBefore) {
        Timber.d("Loading data from %s week(s) ago", weekBefore);
        final Date currentWeekDay = CalendarUtils.weekAgo(weekBefore);
        final Date lastMonday = CalendarUtils.lastMonday(currentWeekDay);
        final Date nextMonday = CalendarUtils.nextMonday(currentWeekDay);

        final Map<Entry, String> labels = new HashMap<>();
        final List<Integer> dataColorList = new ArrayList<>();
        final List<Integer> valueColorList = new ArrayList<>();
        final List<BubbleEntry> entries = Ix.from(fakeCubeDataManager.loadByDate(lastMonday, nextMonday))
                .filter(cubeData -> cubeData.getMeasure().getValue() >= 0)
                .map(value -> {
                    final BubbleEntry bubbleEntry = new BubbleEntry(CalendarUtils.dayOfWeek(value.getDate()) - DAY_OFFSET, CalendarUtils.hourOfDay(value.getDate()), SIZE);
                    labels.put(bubbleEntry, String.valueOf(value.getMeasure().getValue()));
                    //TODO add color to valueColorList and dataColorList
                    return bubbleEntry;
                })
                .toList();

        Timber.d("Number of entries: %s", entries.size());
        final BubbleDataSet dataSet = new BubbleDataSet(entries, null);
//        dataSet.setColors(dataColorList);
//        dataSet.setValueTextColors(valueColorList);
        dataSet.setValueFormatter(new WeeklyChartValueFormatter(labels));
        return dataSet;
    }
}
