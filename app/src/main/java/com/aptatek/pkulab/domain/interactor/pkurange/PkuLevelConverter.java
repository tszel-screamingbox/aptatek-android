package com.aptatek.pkulab.domain.interactor.pkurange;

import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;

public final class PkuLevelConverter {

    private static final float MOL_TO_GRAM_CONVERSION_RATIO = 60f;

    private PkuLevelConverter() {

    }

    public static PkuLevel convertTo(final PkuLevel currentLevel, final PkuLevelUnits targetUnit) {
        if (currentLevel.getUnit() == targetUnit) {
            return currentLevel;
        }

        final float ratio;
        if (currentLevel.getUnit() == PkuLevelUnits.MICRO_MOL) {
            ratio = 1 / MOL_TO_GRAM_CONVERSION_RATIO;
        } else {
            ratio = MOL_TO_GRAM_CONVERSION_RATIO;
        }

        return PkuLevel.create(currentLevel.getValue() * ratio, targetUnit);
    }

}
