package com.aptatek.pkuapp.view.weekly.chart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.device.formatter.WeeklyChartValueFormatter;
import com.aptatek.pkuapp.device.time.TimeHelper;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkuapp.domain.model.CubeData;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuRangeInfo;
import com.aptatek.pkuapp.util.ChartUtils;
import com.aptatek.pkuapp.view.settings.pkulevel.RangeSettingsValueFormatter;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ix.Ix;

public class WeeklyChartDataTransformer {

    private static final float SIZE = 1f;
    private static final int DAY_OFFSET = 1;
    private static final float BUBBLE_ALPHA = 0.8f;

    protected final ResourceInteractor resourceInteractor;
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
    public Single<ChartEntryData> transform(final CubeData cubeData) {
        return pkuRangeInteractor.getInfo()
            .map(rangeInfo -> buildChartEntryData(rangeInfo, cubeData));
    }

    protected ChartEntryData buildChartEntryData(final PkuRangeInfo rangeInfo, final CubeData cubeData) {
        final PkuLevel pkuLevel = cubeData.getPkuLevel();
        final PkuLevel levelInProperUnit;
        if (rangeInfo.getPkuLevelUnit() != pkuLevel.getUnit()) {
            levelInProperUnit = PkuLevelConverter.convertTo(pkuLevel, rangeInfo.getPkuLevelUnit());
        } else {
            levelInProperUnit = pkuLevel;
        }

        final int x = TimeHelper.getDayOfWeek(cubeData.getTimestamp()) - DAY_OFFSET;
        final int y = TimeHelper.getMinuteOfDay(cubeData.getTimestamp());
        final String label = rangeSettingsValueFormatter.formatRegularValue(levelInProperUnit);
        final ChartUtils.State state = ChartUtils.getState(levelInProperUnit, rangeInfo);
        final @ColorRes int colorRes = ChartUtils.stateColor(state);
        final @ColorInt int labelColor = resourceInteractor.getColorResource(R.color.applicationWhite);
        final @ColorInt int bubbleColor = adjustAlpha(resourceInteractor.getColorResource(colorRes), BUBBLE_ALPHA);

        return ChartEntryData.builder()
                .setX(x)
                .setY(y)
                .setSize(SIZE)
                .setLabel(label)
                .setLabelColor(labelColor)
                .setBubbleColor(bubbleColor)
                .build();
    }

    public Single<BubbleDataSet> transformEntries(@NonNull final List<ChartEntryData> chartEntries) {
        return Single.fromCallable(() -> {
            final List<BubbleEntry> bubbleEntries = new ArrayList<>();
            final List<Integer> bubbleColors = new ArrayList<>();
            final List<Integer> labelColors = new ArrayList<>();

            Ix.from(chartEntries)
                    .foreach(entry -> {
                        final BubbleEntry bubbleEntry = createBubbleEntryFor(entry);
                        bubbleEntries.add(bubbleEntry);
                        bubbleColors.add(entry.getBubbleColor());
                        labelColors.add(entry.getLabelColor());
                    });

            final BubbleDataSet dataSet = new BubbleDataSet(bubbleEntries, null);
            dataSet.setColors(bubbleColors);
            dataSet.setValueTextColors(labelColors);
            dataSet.setValueTextSize(resourceInteractor.getDimension(R.dimen.font_size_xmini));
            dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
            dataSet.setValueFormatter(new WeeklyChartValueFormatter());

            return dataSet;
        });
    }

    private BubbleEntry createBubbleEntryFor(final ChartEntryData data) {
        final BubbleEntry bubbleEntry = new BubbleEntry(data.getX(), data.getY(), data.getSize());
        bubbleEntry.setData(data);
        return bubbleEntry;
    }

    protected int adjustAlpha(@ColorInt final int color, final float alpha) {
        final int newAlpha = Math.round(Color.alpha(color) * alpha);
        final int red = Color.red(color);
        final int green = Color.green(color);
        final int blue = Color.blue(color);

        return Color.argb(newAlpha, red, green, blue);
    }

}
