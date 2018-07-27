package com.aptatek.aptatek.view.settings.pkulevel;

import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;

public interface RangeSettingsValueFormatter {

    String getFormattedLow(PkuRangeInfo info);

    String getFormattedHigh(PkuRangeInfo info);

    String getFormattedVeryHigh(PkuRangeInfo info);

    String formatRegularValue(PkuLevel pkuLevel);

}
