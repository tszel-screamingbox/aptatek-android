package com.aptatek.pkulab.view.main.home.adapter.daily;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.util.StringUtils;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class DailyChartFormatter {
    private final ResourceInteractor resourceInteractor;
    private final RangeSettingsValueFormatter valueFormatter;
    private final PkuRangeInteractor pkuRangeInteractor;
    private final SimpleDateFormat shortFormat;
    private final SimpleDateFormat longFormat;
    private final SimpleDateFormat dailyFormat;

    @Inject
    DailyChartFormatter(final ResourceInteractor resourceInteractor,
                        final RangeSettingsValueFormatter valueFormatter,
                        final PkuRangeInteractor pkuRangeInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.valueFormatter = valueFormatter;
        this.pkuRangeInteractor = pkuRangeInteractor;
        shortFormat = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.main_date_format_short), Locale.getDefault());
        longFormat = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.main_date_format_long), Locale.getDefault());
        dailyFormat = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.main_chart_daily_format), Locale.getDefault());
    }

    public String formatDailyDate(final long timestamp) {
        return dailyFormat.format(new Date(timestamp));
    }

    public String getBubbleValue(final PkuLevel highestMeasure) {
        final PkuRangeInfo userSettings = pkuRangeInteractor.getInfo().blockingGet();
        final PkuLevel pkuLevelInSelectedUnit = convertToDisplayUnit(highestMeasure, userSettings);

        return valueFormatter.formatRegularValue(pkuLevelInSelectedUnit);
    }

    public CharSequence getBubbleText(final PkuLevel highestMeasure) {
        final PkuRangeInfo userSettings = pkuRangeInteractor.getInfo().blockingGet();
        final PkuLevel pkuLevelInSelectedUnit = convertToDisplayUnit(highestMeasure, userSettings);
        final PkuLevel pkuLevelInAlternativeUnit = userSettings.getPkuLevelUnit() == highestMeasure.getUnit()
                ? PkuLevelConverter.convertTo(highestMeasure, userSettings.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL ? PkuLevelUnits.MILLI_GRAM : PkuLevelUnits.MICRO_MOL)
                : highestMeasure;

        final String alternativeText = valueFormatter.formatRegularValue(pkuLevelInAlternativeUnit)
                + resourceInteractor.getStringResource(pkuLevelInAlternativeUnit.getUnit() == PkuLevelUnits.MICRO_MOL
                ? R.string.rangeinfo_pkulevel_mmol
                : R.string.rangeinfo_pkulevel_mg);

        return StringUtils.highlightWord(
                valueFormatter.formatRegularValue(pkuLevelInSelectedUnit),
                alternativeText
        );
    }

    public String formatDate(final long timestamp, final boolean useLongFormat) {
        final Date dateToFormat = new Date(timestamp);
        return useLongFormat ? longFormat.format(dateToFormat) : shortFormat.format(dateToFormat);
    }

    public String getNameOfDay(final long timestamp) {
        return TimeHelper.getNameOfDay(timestamp);
    }

    private PkuLevel convertToDisplayUnit(final PkuLevel pkuLevel, final PkuRangeInfo userRange) {
        final PkuLevel levelInProperUnit;
        if (userRange.getPkuLevelUnit() != pkuLevel.getUnit()) {
            levelInProperUnit = PkuLevelConverter.convertTo(pkuLevel, userRange.getPkuLevelUnit());
        } else {
            levelInProperUnit = pkuLevel;
        }

        return levelInProperUnit;
    }

}
