package com.aptatek.aptatek.view.rangeinfo;

import com.aptatek.aptatek.domain.model.PkuRangeInfo;

public interface PkuValueFormatter {

    String formatVeryHigh(PkuRangeInfo info);

    String formatHigh(PkuRangeInfo info);

    String formatNormal(PkuRangeInfo info);

    String formatLow(PkuRangeInfo info);

    String formatUnits(PkuRangeInfo info);

}
