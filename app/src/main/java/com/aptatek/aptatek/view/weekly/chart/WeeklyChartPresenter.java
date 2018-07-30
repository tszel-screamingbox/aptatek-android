package com.aptatek.aptatek.view.weekly.chart;

import android.graphics.Color;

import com.aptatek.aptatek.device.formatter.WeeklyChartValueFormatter;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.aptatek.domain.respository.manager.FakeCubeDataManager;
import com.aptatek.aptatek.util.CalendarUtils;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


class WeeklyChartPresenter extends MvpBasePresenter<WeeklyChartView> {

    private final FakeCubeDataManager fakeCubeDataManager;
    private final PkuRangeInteractor pkuRangeInteractor;

    @Inject
    WeeklyChartPresenter(final FakeCubeDataManager fakeCubeDataManager,
                         final PkuRangeInteractor pkuRangeInteractor) {
        this.fakeCubeDataManager = fakeCubeDataManager;
        this.pkuRangeInteractor = pkuRangeInteractor;
    }

    void fillDataSet(final int weekBefore) {
        final Date firstDayOfWeek;
        final Date lastDayOfWeek;
        final Date date = CalendarUtils.dateBefore(weekBefore);

        //fakeCubeDataManager.loadByDate(firstDayOfWeek, nextMonday);
    }

    BubbleDataSet generateDataset() {
        final ArrayList<BubbleEntry> entries = new ArrayList<>();
        entries.add(new BubbleEntry(0, 0, 0.1f));
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
        dataSet.setColors(Color.RED, Color.GRAY, Color.GREEN);

        final Map<Entry, String> labels = new HashMap<>();
        for (final BubbleEntry bubbleEntry : entries) {
            labels.put(bubbleEntry, "123");
        }

        dataSet.setValueFormatter(new WeeklyChartValueFormatter(labels));
        return dataSet;
    }
}
