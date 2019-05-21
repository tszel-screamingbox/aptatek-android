package com.aptatek.pkulab.device.formatter;


import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsValueFormatter;

import java.util.Locale;

import static com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter.convertTo;
import static com.aptatek.pkulab.domain.model.PkuLevelUnits.MICRO_MOL;
import static com.aptatek.pkulab.util.Constants.DEFAULT_PKU_MARGIN_MIN;

public class RangeSettingsValueFormatterImpl implements RangeSettingsValueFormatter {

    private static final String FORMAT_MILLI_GRAM = "%.1f";
    private static final String FORMAT_MICRO_MOL = "%.0f";
    private static final float OFFSET_MICRO_MOL = 1f;
    private static final float OFFSET_MILLI_GRAM = 0.1f;

    private final ResourceInteractor resourceInteractor;

    public RangeSettingsValueFormatterImpl(final ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    @NonNull
    private String getProperFormat(final PkuLevelUnits units) {
        return units == MICRO_MOL ? FORMAT_MICRO_MOL : FORMAT_MILLI_GRAM;
    }

    private float getProperOffset(final PkuLevelUnits units) {
        return units == MICRO_MOL ? OFFSET_MICRO_MOL : OFFSET_MILLI_GRAM;
    }

    @Override
    public String getProperUnits(final PkuLevelUnits units) {
        return resourceInteractor.getStringResource(units == MICRO_MOL
                ? R.string.rangeinfo_pkulevel_mmol
                : R.string.rangeinfo_pkulevel_mg);
    }

    @Override
    public String formatStandardPdfEntry(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.pdf_legend_low,
                formatRegularValue(info.getNormalFloorValue() - getProperOffset(info.getPkuLevelUnit()), info.getPkuLevelUnit()),
                getProperUnits(info.getPkuLevelUnit()));
    }

    @Override
    public String formatIncreasedPdfEntry(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.pdf_legend_normal,
                formatRegularValue(info.getNormalFloorValue(), info.getPkuLevelUnit()),
                formatRegularValue(info.getNormalCeilValue(), info.getPkuLevelUnit()),
                getProperUnits(info.getPkuLevelUnit()));
    }

    @Override
    public String formatHighPdfEntry(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.pdf_legend_high,
                formatRegularValue(info.getNormalCeilValue() + getProperOffset(info.getPkuLevelUnit()), info.getPkuLevelUnit()),
                formatRegularValue(info.getHighCeilValue(), info.getPkuLevelUnit()),
                getProperUnits(info.getPkuLevelUnit()));
    }

    @Override
    public String formatVeryHighPdfEntry(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.pdf_legend_very_high,
                formatRegularValue(info.getHighCeilValue() + getProperOffset(info.getPkuLevelUnit()), info.getPkuLevelUnit()),
                getProperUnits(info.getPkuLevelUnit()));
    }

    @Override
    public String getFormattedLow(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.settings_units_range_format,
                "0",
                formatRegularValue(info.getNormalFloorValue() - getProperOffset(info.getPkuLevelUnit()), info.getPkuLevelUnit()),
                getProperUnits(info.getPkuLevelUnit()));
    }

    @Override
    public String getFormattedIncreased(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.settings_units_range_format,
                formatRegularValue(info.getNormalFloorValue(), info.getPkuLevelUnit()),
                formatRegularValue(info.getNormalCeilValue(), info.getPkuLevelUnit()),
                getProperUnits(info.getPkuLevelUnit()));
    }

    @Override
    public String getFormattedHigh(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.settings_units_range_format,
                formatRegularValue(info.getNormalCeilValue() + getProperOffset(info.getPkuLevelUnit()), info.getPkuLevelUnit()),
                formatRegularValue(info.getHighCeilValue(), info.getPkuLevelUnit()),
                getProperUnits(info.getPkuLevelUnit()));
    }

    @Override
    public String getFormattedVeryHigh(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.settings_units_range_over_format,
                formatRegularValue(info.getHighCeilValue() + getProperOffset(info.getPkuLevelUnit()), info.getPkuLevelUnit()),
                getProperUnits(info.getPkuLevelUnit()));
    }

    @Override
    public String formatRegularValue(final PkuLevel level) {
        if (isUnderMargin(level)) {
            return resourceInteractor.getStringResource(level.getUnit() == MICRO_MOL
                    ? R.string.rangeinfo_margin_umol
                    : R.string.rangeinfo_margin_mg);
        }

        return String.format(Locale.getDefault(), getProperFormat(level.getUnit()), level.getValue());
    }

    private String formatRegularValue(final float value, final PkuLevelUnits unit) {
        return formatRegularValue(PkuLevel.create(value, unit));
    }

    private boolean isUnderMargin(final PkuLevel pkuLevel) {
        return convertTo(pkuLevel, MICRO_MOL).getValue() < DEFAULT_PKU_MARGIN_MIN;
    }
}
