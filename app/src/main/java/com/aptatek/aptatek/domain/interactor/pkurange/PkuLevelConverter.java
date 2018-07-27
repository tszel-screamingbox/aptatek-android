package com.aptatek.aptatek.domain.interactor.pkurange;

import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;

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
