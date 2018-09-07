package com.aptatek.pkuapp.device.formatter;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.model.PkuLevelUnits;
import com.aptatek.pkuapp.domain.model.PkuRangeInfo;
import com.aptatek.pkuapp.view.rangeinfo.PkuValueFormatter;

import java.util.Locale;

public class PkuValueFormatterImpl implements PkuValueFormatter {

    private static final String FORMAT_MILLI_GRAM = "%.1f";
    private static final String FORMAT_MICRO_MOL = "%.0f";

    private final ResourceInteractor interactor;

    public PkuValueFormatterImpl(final ResourceInteractor interactor) {
        this.interactor = interactor;
    }

    private float getProperOffset(final PkuRangeInfo info) {
        return info.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL ? 1f : 0.1f;
    }

    @Override
    public String formatVeryHigh(final PkuRangeInfo info) {
        return String.format(Locale.getDefault(), getProperFormat(info), info.getHighCeilValue() + getProperOffset(info)) + "+";
    }

    @NonNull
    private String getProperFormat(final PkuRangeInfo info) {
        return info.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL ? FORMAT_MICRO_MOL : FORMAT_MILLI_GRAM;
    }

    @Override
    public String formatHigh(final PkuRangeInfo info) {
        final String floor = String.format(Locale.getDefault(), getProperFormat(info), info.getNormalCeilValue() + getProperOffset(info));
        final String ceil = String.format(Locale.getDefault(), getProperFormat(info), info.getHighCeilValue());
        return floor + "-" + ceil;
    }

    @Override
    public String formatNormal(final PkuRangeInfo info) {
        final String floor = String.format(Locale.getDefault(), getProperFormat(info), info.getNormalFloorValue());
        final String ceil = String.format(Locale.getDefault(), getProperFormat(info), info.getNormalCeilValue());
        return floor + "-" + ceil;
    }

    @Override
    public String formatLow(final PkuRangeInfo info) {
        return "0-" + String.format(Locale.getDefault(), getProperFormat(info), info.getNormalFloorValue() - getProperOffset(info));
    }

    @Override
    public String formatUnits(final PkuRangeInfo info) {
        return interactor.getStringResource(R.string.rangeinfo_units_format, interactor.getStringResource(info.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL
                ? R.string.rangeinfo_pkulevel_mmol
                : R.string.rangeinfo_pkulevel_mg)
        );
    }
}
