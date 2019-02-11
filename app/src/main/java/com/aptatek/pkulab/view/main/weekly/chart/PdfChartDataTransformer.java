package com.aptatek.pkulab.view.main.weekly.chart;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.util.ChartUtils;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsValueFormatter;

import javax.inject.Inject;

public class PdfChartDataTransformer extends WeeklyChartDataTransformer {

    @Inject
    PdfChartDataTransformer(final ResourceInteractor resourceInteractor,
                            final PkuRangeInteractor pkuRangeInteractor,
                            final RangeSettingsValueFormatter rangeSettingsValueFormatter) {
        super(resourceInteractor, pkuRangeInteractor, rangeSettingsValueFormatter);
    }

    @Override
    protected ChartEntryData buildChartEntryData(final PkuRangeInfo rangeInfo, final TestResult testResult) {
        final ChartEntryData chartEntryData = super.buildChartEntryData(rangeInfo, testResult);

        final float x = TimeHelper.getDayOfMonth(testResult.getTimestamp());

        final ChartUtils.State state = ChartUtils.getState(testResult.getPkuLevel(), rangeInfo);
        final @ColorRes int colorRes = ChartUtils.stateColor(state);
        @ColorInt int labelColor = resourceInteractor.getColorResource(R.color.applicationWhite);
        @ColorInt int bubbleColor = resourceInteractor.getColorResource(colorRes);

        if (testResult.isFasting()) {
            labelColor = resourceInteractor.getColorResource(colorRes);
            bubbleColor = resourceInteractor.getColorResource(R.color.applicationWhite);
        }

        return chartEntryData.toBuilder()
                .setX(x)
                .setBubbleColor(bubbleColor)
                .setLabelColor(labelColor)
                .setSick(testResult.isSick())
                .setFasting(testResult.isFasting())
                .build();
    }
}
