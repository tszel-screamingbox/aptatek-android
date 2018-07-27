package com.aptatek.aptatek.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PkuLevel {

    public abstract float getValue();

    public abstract PkuLevelUnits getUnit();

    public static PkuLevel create(final float value, final PkuLevelUnits unit) {
        return new AutoValue_PkuLevel(value, unit);
    }

}
