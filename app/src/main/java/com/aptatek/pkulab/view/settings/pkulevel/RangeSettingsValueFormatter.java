package com.aptatek.pkulab.view.settings.pkulevel;

import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;

public interface RangeSettingsValueFormatter {

    String getFormattedLow(PkuRangeInfo info);

    String getFormattedHigh(PkuRangeInfo info);

    String getFormattedVeryHigh(PkuRangeInfo info);

    String formatRegularValue(PkuLevel pkuLevel);

    String getProperUnits(final PkuLevelUnits units);
}
