package com.aptatek.pkuapp.view.settings.pkulevel;

import com.aptatek.pkuapp.domain.model.PkuLevelUnits;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RangeSettingsModel {

    public abstract String getLowText();

    public abstract float getNormalFloorMMolValue();

    public abstract float getNormalCeilMMolValue();

    public abstract float getNormalAbsoluteFloorMMolValue();

    public abstract float getNormalAbsoluteCeilMMolValue();

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

        public abstract Builder setNormalFloorMMolValue(float value);

        public abstract Builder setNormalCeilMMolValue(float value);

        public abstract Builder setNormalAbsoluteFloorMMolValue(float value);

        public abstract Builder setNormalAbsoluteCeilMMolValue(float value);

        public abstract Builder setSelectedUnit(PkuLevelUnits value);

        public abstract RangeSettingsModel build();

    }

}
