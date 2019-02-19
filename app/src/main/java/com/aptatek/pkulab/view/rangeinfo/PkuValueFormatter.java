package com.aptatek.pkulab.view.rangeinfo;

import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;

public interface PkuValueFormatter {

    String formatVeryHigh(PkuRangeInfo info);

    String formatHigh(PkuRangeInfo info);

    String formatNormal(PkuRangeInfo info);

    String formatLow(PkuRangeInfo info);

    String formatUnits(PkuRangeInfo info);

    String formatFromUnits(PkuLevelUnits units);
}
