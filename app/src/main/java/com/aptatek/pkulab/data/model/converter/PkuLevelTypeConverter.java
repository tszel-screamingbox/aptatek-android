package com.aptatek.pkulab.data.model.converter;


import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;

public class PkuLevelTypeConverter {

    @TypeConverter
    public static PkuLevel toPkuLevel(final double valueInMg) {
        return PkuLevel.create((float) valueInMg, PkuLevelUnits.MILLI_GRAM);
    }

    @TypeConverter
    public static double toDouble(final @NonNull PkuLevel value) {
        final PkuLevel inMg = PkuLevelConverter.convertTo(value, PkuLevelUnits.MILLI_GRAM);
        return inMg.getValue();
    }

}
