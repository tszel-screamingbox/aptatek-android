package com.aptatek.pkuapp.view.weekly.chart;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.device.time.TimeHelper;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkuapp.domain.model.CubeData;
import com.aptatek.pkuapp.domain.model.PkuRangeInfo;
import com.aptatek.pkuapp.util.ChartUtils;
import com.aptatek.pkuapp.view.settings.pkulevel.RangeSettingsValueFormatter;

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
