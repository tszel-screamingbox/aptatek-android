package com.aptatek.pkulab.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PkuRangeInfo {

    public abstract float getNormalFloorValue();

    public abstract float getNormalCeilValue();

    public abstract float getHighCeilValue();

    public abstract float getNormalAbsoluteMinValue();

    public abstract float getNormalAbsoluteMaxValue();

    public abstract PkuLevelUnits getPkuLevelUnit();

    public abstract boolean isDefaultValue();

    public static PkuRangeInfo.Builder builder() {
        return new AutoValue_PkuRangeInfo
                .Builder()
                .setDefaultValue(true);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setNormalFloorValue(float floorValue);

        public abstract Builder setNormalCeilValue(float ceilValue);

        public abstract Builder setHighCeilValue(float ceilValue);

        public abstract Builder setNormalAbsoluteMinValue(float minValue);

        public abstract Builder setNormalAbsoluteMaxValue(float maxValue);

        public abstract Builder setPkuLevelUnit(PkuLevelUnits unit);

        public abstract Builder setDefaultValue(boolean isDefault);

        public abstract PkuRangeInfo build();
    }

}
