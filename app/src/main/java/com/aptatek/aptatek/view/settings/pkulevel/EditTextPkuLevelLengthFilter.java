package com.aptatek.aptatek.view.settings.pkulevel;

import android.text.InputFilter;
import android.text.Spanned;

import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.util.Constants;

import javax.inject.Inject;

public class EditTextPkuLevelLengthFilter implements InputFilter {

    private static final int LEVEL_MAX_LENGTH = 4;

    private PkuLevelUnits currentUnit;

    @Inject
    public EditTextPkuLevelLengthFilter() {
        currentUnit = Constants.DEFAULT_PKU_LEVEL_UNIT;
    }

    @Override
    public CharSequence filter(final CharSequence source,
                               final int start,
                               final int end,
                               final Spanned dest,
                               final int dstart,
                               final int dend) {
        // no decimals are allowed in case of micro mols
        if (currentUnit == PkuLevelUnits.MICRO_MOL) {
            if (source.toString().contains(".") || source.toString().contains(",")) {
                return "";
            }
        }

        int keep = LEVEL_MAX_LENGTH - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }

    public void setCurrentUnit(final PkuLevelUnits currentUnit) {
        this.currentUnit = currentUnit;
    }

}
