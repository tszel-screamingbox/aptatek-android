package com.aptatek.pkuapp.view.settings.pkulevel;

import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuRangeInfo;

public interface RangeSettingsValueFormatter {

    String getFormattedLow(PkuRangeInfo info);

    String getFormattedHigh(PkuRangeInfo info);

    String getFormattedVeryHigh(PkuRangeInfo info);

    String formatRegularValue(PkuLevel pkuLevel);

}
