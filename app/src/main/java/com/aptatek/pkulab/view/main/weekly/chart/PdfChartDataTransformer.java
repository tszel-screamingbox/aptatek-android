package com.aptatek.pkulab.view.main.weekly.chart;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.model.CubeData;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
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
    protected ChartEntryData buildChartEntryData(final PkuRangeInfo rangeInfo, final CubeData cubeData) {
        final ChartEntryData chartEntryData = super.buildChartEntryData(rangeInfo, cubeData);

        final float x = TimeHelper.getDayOfMonth(cubeData.getTimestamp());

        final ChartUtils.State state = ChartUtils.getState(cubeData.getPkuLevel(), rangeInfo);
        final @ColorRes int colorRes = ChartUtils.stateColor(state);
        @ColorInt int labelColor = resourceInteractor.getColorResource(colorRes);
        @ColorInt int bubbleColor = adjustAlpha(resourceInteractor.getColorResource(colorRes), 0.2f);
        @ColorInt int strokeColor = 0;

        if (cubeData.isSick()) {
            labelColor = resourceInteractor.getColorResource(R.color.applicationWhite);
            bubbleColor = resourceInteractor.getColorResource(colorRes);
        } else if (cubeData.isFasting()) {
            strokeColor = resourceInteractor.getColorResource(colorRes);
        }

        return chartEntryData.toBuilder()
                .setX(x)
                .setStrokeColor(strokeColor)
                .setBubbleColor(bubbleColor)
                .setLabelColor(labelColor)
                .build();
    }
}