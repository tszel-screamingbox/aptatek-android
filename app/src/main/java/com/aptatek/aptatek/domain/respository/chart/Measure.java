package com.aptatek.aptatek.domain.respository.chart;

import com.aptatek.aptatek.domain.model.PkuLevel;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Measure {

    public abstract int getPhenylalanineLevel();

    public abstract PkuLevel getUnit();

    public static Measure create(final int level, final PkuLevel unit) {
        return new AutoValue_Measure(level, unit);
    }
}
