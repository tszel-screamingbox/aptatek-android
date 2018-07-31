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


class WeeklyChartPresenter extends MvpBasePresenter<WeeklyChartView> {

    private static final float SIZE = 0.1f;

    private final FakeCubeDataManager fakeCubeDataManager;

    @Inject
    WeeklyChartPresenter(final FakeCubeDataManager fakeCubeDataManager) {
        this.fakeCubeDataManager = fakeCubeDataManager;

    }

    BubbleDataSet getChartData(final int weekBefore) {
        final Date currentWeekDay = CalendarUtils.dateBefore(weekBefore);
        final Date lastMonday = CalendarUtils.lastMonday(currentWeekDay);
        final Date nextMonday = CalendarUtils.nextMonday(currentWeekDay);

        final List<BubbleEntry> entries =
                Ix.from(fakeCubeDataManager.loadByDate(lastMonday, nextMonday))
                        .filter(cubeData -> cubeData.getMeasuredLevel() >= 0)
                        .map(value -> new BubbleEntry(CalendarUtils.dayOfWeek(value.getDate()) - 1, CalendarUtils.hourOfDay(value.getDate()), SIZE))
                        .toList();

        return new BubbleDataSet(entries, null);
    }

    BubbleDataSet generateDataset() {
        final ArrayList<BubbleEntry> entries = new ArrayList<>();
        entries.add(new BubbleEntry(0, 0, SIZE));
        entries.add(new BubbleEntry(0, 13, 0.1f));
        entries.add(new BubbleEntry(1, 7, 0.1f));
        entries.add(new BubbleEntry(1, 10, 0.1f));
        entries.add(new BubbleEntry(2, 8, 0.1f));
        entries.add(new BubbleEntry(2, 12, 0.1f));
        entries.add(new BubbleEntry(3, 20, 0.1f));
        entries.add(new BubbleEntry(4, 23, 0.1f));
        entries.add(new BubbleEntry(6, 23, 0.1f));
        entries.add(new BubbleEntry(6, 11, 0.1f));


        final BubbleDataSet dataSet = new BubbleDataSet(entries, null);
//        dataSet.setColors(Color.RED, Color.GRAY, Color.GREEN);

        final Map<Entry, String> labels = new HashMap<>();
        for (final BubbleEntry bubbleEntry : entries) {
            labels.put(bubbleEntry, "123");
        }

        dataSet.setValueFormatter(new WeeklyChartValueFormatter(labels));
        return dataSet;
    }
}
