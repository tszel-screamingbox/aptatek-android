package com.aptatek.pkulab.view.main.weekly.chart;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.formatter.WeeklyChartValueFormatter;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.util.ChartUtils;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsValueFormatter;
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
    public Single<ChartEntryData> transform(final TestResult testResult) {
        return pkuRangeInteractor.getInfo()
                .map(rangeInfo -> buildChartEntryData(rangeInfo, testResult));
    }

    @NonNull
    public Single<ChartEntryData> transform(final TestResult testResult, final PkuRangeInfo rangeInfo) {
        return Single.fromCallable(() -> buildChartEntryData(rangeInfo, testResult));
    }

    protected ChartEntryData buildChartEntryData(final PkuRangeInfo rangeInfo, final TestResult testResult) {
        final PkuLevel pkuLevel = testResult.getPkuLevel();
        final PkuLevel levelInProperUnit;
        if (rangeInfo.getPkuLevelUnit() != pkuLevel.getUnit()) {
            levelInProperUnit = PkuLevelConverter.convertTo(pkuLevel, rangeInfo.getPkuLevelUnit());
        } else {
            levelInProperUnit = pkuLevel;
        }

        final int x = TimeHelper.getDayOfWeek(testResult.getTimestamp()) - DAY_OFFSET;
        final int y = TimeHelper.getMinuteOfDay(testResult.getTimestamp());
        final String label = rangeSettingsValueFormatter.formatRegularValue(levelInProperUnit);
        final ChartUtils.State state = ChartUtils.getState(levelInProperUnit, rangeInfo);
        final @ColorRes int colorRes = ChartUtils.stateColor(state);
        final @ColorInt int labelColor = resourceInteractor.getColorResource(R.color.applicationWhite);
        final @ColorInt int bubbleColor = resourceInteractor.getColorResource(colorRes);

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
            dataSet.setValueTextSize(resourceInteractor.getDimension(R.dimen.font_size_xbig));
            dataSet.setValueTypeface(resourceInteractor.getTypeface(R.font.nunito_black));
            dataSet.setValueFormatter(new WeeklyChartValueFormatter());

            return dataSet;
        });
    }

    private BubbleEntry createBubbleEntryFor(final ChartEntryData data) {
        final BubbleEntry bubbleEntry = new BubbleEntry(data.getX(), data.getY(), data.getSize());
        bubbleEntry.setData(data);
        return bubbleEntry;
    }
}
