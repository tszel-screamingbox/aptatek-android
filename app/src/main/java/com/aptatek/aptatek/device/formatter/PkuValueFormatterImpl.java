package com.aptatek.aptatek.device.formatter;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.aptatek.aptatek.view.rangeinfo.PkuValueFormatter;

import java.util.Locale;

public class PkuValueFormatterImpl implements PkuValueFormatter {

    private static final String FORMAT_MILLI_GRAM = "%.1f";
    private static final String FORMAT_MICRO_MOL = "%.0f";

    private final ResourceInteractor interactor;

    public PkuValueFormatterImpl(final ResourceInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public String formatVeryHigh(final PkuRangeInfo info) {
        return String.format(Locale.getDefault(), getProperFormat(info), info.getHighCeilValue() + 1f);
    }

    @NonNull
    private String getProperFormat(final PkuRangeInfo info) {
        return info.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL ? FORMAT_MICRO_MOL : FORMAT_MILLI_GRAM;
    }

    @Override
    public String formatHigh(final PkuRangeInfo info) {
        final String floor = String.format(Locale.getDefault(), getProperFormat(info), info.getNormalCeilValue() + 1f);
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
        return String.format(Locale.getDefault(), getProperFormat(info), info.getNormalFloorValue() - 1f);
    }

    @Override
    public String formatUnits(final PkuRangeInfo info) {
        return interactor.getStringResource(R.string.rangeinfo_units_format, interactor.getStringResource(info.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL
                ? R.string.rangeinfo_pkulevel_mmol
                : R.string.rangeinfo_pkulevel_mg)
        );
    }
}
