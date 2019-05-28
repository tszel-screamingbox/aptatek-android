package com.aptatek.pkulab.view.settings.pkulevel;

import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;

public interface RangeSettingsValueFormatter {

    String getFormattedLow(PkuRangeInfo info);

    String getFormattedIncreased(PkuRangeInfo info);

    String getFormattedHigh(PkuRangeInfo info);

    String getFormattedVeryHigh(PkuRangeInfo info);

    String formatRegularValue(PkuLevel pkuLevel);

    String getProperUnits(final PkuLevelUnits units);

    String formatStandardPdfEntry(PkuRangeInfo info);

    String formatIncreasedPdfEntry(PkuRangeInfo info);

    String formatHighPdfEntry(PkuRangeInfo info);

    String formatVeryHighPdfEntry(PkuRangeInfo info);
}
