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

    private final ResourceInteractor resourceInteractor;

    public RangeSettingsValueFormatterImpl(ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    @NonNull
    private String getProperFormat(final PkuRangeInfo info) {
        return info.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL ? FORMAT_MICRO_MOL : FORMAT_MILLI_GRAM;
    }

    private String getProperUnits(final PkuRangeInfo info) {
        return resourceInteractor.getStringResource(info.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL ? R.string.rangeinfo_pkulevel_mmol : R.string.rangeinfo_pkulevel_mg);
    }

    private float getProperOffset(final PkuRangeInfo info) {
        return info.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL ? 1f : 0.1f;
    }

    @Override
    public String getFormattedLow(PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.settings_units_range_format,
                "0",
                String.format(Locale.getDefault(), getProperFormat(info), info.getNormalFloorValue() - getProperOffset(info)),
                getProperUnits(info));
    }

    @Override
    public String getFormattedHigh(PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.settings_units_range_format,
                String.format(Locale.getDefault(), getProperFormat(info), info.getNormalCeilValue() + getProperOffset(info)),
                String.format(Locale.getDefault(), getProperFormat(info), info.getHighCeilValue()),
                getProperUnits(info));
    }

    @Override
    public String getFormattedVeryHigh(PkuRangeInfo info) {
        return resourceInteractor.getStringResource(R.string.settings_units_range_over_format,
                String.format(Locale.getDefault(), getProperFormat(info), info.getHighCeilValue() + getProperOffset(info)),
                getProperUnits(info));
    }
}
