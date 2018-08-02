package com.aptatek.aptatek.view.weekly.chart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.device.formatter.WeeklyChartValueFormatter;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.domain.respository.manager.FakeCubeDataManager;
import com.aptatek.aptatek.util.CalendarUtils;
import com.aptatek.aptatek.util.ChartUtils;
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
    private static final float BUBBLE_ALPHA = 0.2f;

    private final FakeCubeDataManager fakeCubeDataManager;
    private final ChartUtils chartUtils;
    private final ResourceInteractor resourceInteractor;

    @Inject
    WeeklyChartPresenter(final FakeCubeDataManager fakeCubeDataManager,
                         final ChartUtils chartUtils,
                         final ResourceInteractor resourceInteractor) {
        this.fakeCubeDataManager = fakeCubeDataManager;
        this.chartUtils = chartUtils;
        this.resourceInteractor = resourceInteractor;
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

                    final PkuLevel pkuLevelInDisplayUnit = chartUtils.convertToDisplayUnit(value.getMeasure());

                    labels.put(bubbleEntry, chartUtils.format(pkuLevelInDisplayUnit));
                    final ChartUtils.State state = chartUtils.getState(pkuLevelInDisplayUnit);
                    final int color = resourceInteractor.getColorResource(ChartUtils.stateColor(state));

                    dataColorList.add(adjustAlpha(color));
                    valueColorList.add(color);

                    return bubbleEntry;
                })
                .toList();

        Timber.d("Number of entries: %s", entries.size());
        final BubbleDataSet dataSet = new BubbleDataSet(entries, null);
        dataSet.setColors(dataColorList);
        dataSet.setValueTextColors(valueColorList);
        dataSet.setValueTextSize(resourceInteractor.getDimension(R.dimen.font_size_xregular));
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
        dataSet.setValueFormatter(new WeeklyChartValueFormatter(labels));
        return dataSet;
    }

    private int adjustAlpha(@ColorInt final int color) {
        final int alpha = Math.round(Color.alpha(color) * BUBBLE_ALPHA);
        final int red = Color.red(color);
        final int green = Color.green(color);
        final int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}
