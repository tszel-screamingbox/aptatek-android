package com.aptatek.pkulab.domain.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PkuLevel implements Parcelable {

    public abstract float getValue();

    public abstract PkuLevelUnits getUnit();

    public static PkuLevel create(final float value, final PkuLevelUnits unit) {
        return new AutoValue_PkuLevel(value, unit);
    }

}
