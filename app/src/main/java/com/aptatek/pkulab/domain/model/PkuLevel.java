package com.aptatek.pkulab.domain.model;

import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PkuLevel implements Parcelable {

    public abstract float getValue();

    public abstract PkuLevelUnits getUnit();

    @Nullable
    public abstract String getTextResult();

    @Nullable
    public abstract String getAssayName();

    @Nullable
    public abstract String getName();

    public static PkuLevel create(float value, PkuLevelUnits units) {
        return PkuLevel.builder()
                .setValue(value)
                .setUnit(units)
                .build();
    }

    public static PkuLevel.Builder builder() {
        return new com.aptatek.pkulab.domain.model.AutoValue_PkuLevel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setValue(float value);

        public abstract Builder setUnit(PkuLevelUnits pkuLevelUnits);

        public abstract Builder setTextResult(@Nullable String textResult);

        public abstract Builder setAssayName(@Nullable String assayName);

        public abstract Builder setName(@Nullable String name);

        public abstract PkuLevel build();
    }

}
