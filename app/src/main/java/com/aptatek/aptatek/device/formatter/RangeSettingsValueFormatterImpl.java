package com.aptatek.aptatek.device.formatter;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsValueFormatter;

import java.util.Locale;

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
        return units == PkuLevelUnits.MICRO_MOL ? FORMAT_MICRO_MOL : FORMAT_MILLI_GRAM;
    }

    private String getProperUnits(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(info.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL
                ? R.string.rangeinfo_pkulevel_mmol
                : R.string.rangeinfo_pkulevel_mg);
    }

    private float getProperOffset(final PkuLevelUnits units) {
        return units == PkuLevelUnits.MICRO_MOL ? OFFSET_MICRO_MOL : OFFSET_MILLI_GRAM;
    }

    @Override
    public String getFormattedLow(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.settings_units_range_format,
                "0",
                formatRegularValue(info.getNormalFloorValue() - getProperOffset(info.getPkuLevelUnit()), info.getPkuLevelUnit()),
                getProperUnits(info));
    }

    @Override
    public String getFormattedHigh(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.settings_units_range_format,
                formatRegularValue(info.getNormalCeilValue() + getProperOffset(info.getPkuLevelUnit()), info.getPkuLevelUnit()),
                formatRegularValue(info.getHighCeilValue(), info.getPkuLevelUnit()),
                getProperUnits(info));
    }

    @Override
    public String getFormattedVeryHigh(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.settings_units_range_over_format,
                formatRegularValue(info.getHighCeilValue() + getProperOffset(info.getPkuLevelUnit()), info.getPkuLevelUnit()),
                getProperUnits(info));
    }

    @Override
    public String formatRegularValue(final float value, final PkuLevelUnits unit) {
        return String.format(Locale.getDefault(), getProperFormat(unit), value);
    }

}
