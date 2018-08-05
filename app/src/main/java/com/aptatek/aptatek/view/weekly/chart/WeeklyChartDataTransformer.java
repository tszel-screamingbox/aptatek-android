package com.aptatek.aptatek.view.weekly.chart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.device.formatter.WeeklyChartValueFormatter;
import com.aptatek.aptatek.device.time.TimeHelper;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.aptatek.domain.model.CubeData;
import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.util.ChartUtils;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsValueFormatter;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;
import ix.Ix;

public class WeeklyChartDataTransformer {

    private static final float SIZE = 0.1f;
    private static final int DAY_OFFSET = 1;
    private static final float BUBBLE_ALPHA = 0.2f;

    private final ResourceInteractor resourceInteractor;
    private final PkuRangeInteractor pkuRangeInteractor;
    private final RangeSettingsValueFormatter rangeSettingsValueFormatter;

    @Inject
    WeeklyChartDataTransformer(final ResourceInteractor resourceInteractor,
                               final PkuRangeInteractor pkuRangeInteractor,
                               final RangeSettingsValueFormatter rangeSettingsValueFormatter) {
        this.resourceInteractor = resourceInteractor;
        this.pkuRangeInteractor = pkuRangeInteractor;
        this.rangeSettingsValueFormatter = rangeSettingsValueFormatter;
    }

    @NonNull
    Single<ChartEntryData> transform(CubeData cubeData) {
        return pkuRangeInteractor.getInfo()
            .map(rangeInfo -> {
                final PkuLevel pkuLevel = cubeData.getPkuLevel();
                final PkuLevel levelInProperUnit;
                if (rangeInfo.getPkuLevelUnit() != pkuLevel.getUnit()) {
                    levelInProperUnit = PkuLevelConverter.convertTo(pkuLevel, rangeInfo.getPkuLevelUnit());
                } else {
                    levelInProperUnit = pkuLevel;
                }

                final int x = TimeHelper.getDayOfWeek(cubeData.getTimestamp()) - DAY_OFFSET;
                final int y = TimeHelper.getHourOfDay(cubeData.getTimestamp());
                final String label = rangeSettingsValueFormatter.formatRegularValue(levelInProperUnit);
                final ChartUtils.State state = ChartUtils.getState(pkuLevel, rangeInfo);
                final @ColorRes int colorRes = ChartUtils.stateColor(state);
                final @ColorInt int labelColor = resourceInteractor.getColorResource(colorRes);
                final @ColorInt int bubbleColor = adjustAlpha(labelColor);

                return ChartEntryData.builder()
                        .setX(x)
                        .setY(y)
                        .setSize(SIZE)
                        .setLabel(label)
                        .setLabelColor(labelColor)
                        .setBubbleColor(bubbleColor)
                        .build();
            });
    }

    public Single<BubbleDataSet> transformEntries(@NonNull List<ChartEntryData> chartEntries) {
        return Single.fromCallable(() -> {
            final List<BubbleEntry> bubbleEntries = new ArrayList<>();
            final Map<Entry, String> labels = new HashMap<>();
            final List<Integer> bubbleColors = new ArrayList<>();
            final List<Integer> labelColors = new ArrayList<>();

            Ix.from(chartEntries)
                    .foreach(entry -> {
                        final BubbleEntry bubbleEntry = createBubbleEntryFor(entry);
                        bubbleEntries.add(bubbleEntry);
                        labels.put(bubbleEntry, entry.getLabel());
                        bubbleColors.add(entry.getBubbleColor());
                        labelColors.add(entry.getLabelColor());
                    });

            final BubbleDataSet dataSet = new BubbleDataSet(bubbleEntries, null);
            dataSet.setColors(bubbleColors);
            dataSet.setValueTextColors(labelColors);
            dataSet.setValueTextSize(resourceInteractor.getDimension(R.dimen.font_size_mini));
            dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
            dataSet.setValueFormatter(new WeeklyChartValueFormatter(labels));

            return dataSet;
        });
    }

    private BubbleEntry createBubbleEntryFor(ChartEntryData data) {
        final BubbleEntry bubbleEntry = new BubbleEntry(data.getX(), data.getY(), data.getSize());
        bubbleEntry.setData(data);
        return bubbleEntry;
    }

    private int adjustAlpha(@ColorInt final int color) {
        final int alpha = Math.round(Color.alpha(color) * BUBBLE_ALPHA);
        final int red = Color.red(color);
        final int green = Color.green(color);
        final int blue = Color.blue(color);

        return Color.argb(alpha, red, green, blue);
    }

}
