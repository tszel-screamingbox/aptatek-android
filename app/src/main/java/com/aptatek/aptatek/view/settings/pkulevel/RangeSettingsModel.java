package com.aptatek.aptatek.view.settings.pkulevel;

import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RangeSettingsModel {

    public abstract String getLowText();

    public abstract float getNormalFloorValue();

    public abstract float getNormalCeilValue();

    public abstract float getNormalAbsoluteFloorValue();

    public abstract float getNormalAbsoluteCeilValue();

    public abstract String getHighText();

    public abstract String getVeryHighText();

    public abstract PkuLevelUnits getSelectedUnit();

    public static Builder builder() {
        return new AutoValue_RangeSettingsModel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setLowText(String value);

        public abstract Builder setHighText(String value);

        public abstract Builder setVeryHighText(String value);

        public abstract Builder setNormalFloorValue(float value);

        public abstract Builder setNormalCeilValue(float value);

        public abstract Builder setNormalAbsoluteFloorValue(float value);

        public abstract Builder setNormalAbsoluteCeilValue(float value);

        public abstract Builder setSelectedUnit(PkuLevelUnits value);

        public abstract RangeSettingsModel build();

    }

}
