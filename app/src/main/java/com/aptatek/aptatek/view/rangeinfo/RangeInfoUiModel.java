package com.aptatek.aptatek.view.rangeinfo;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RangeInfoUiModel {

    public abstract String getVeryHighValue();

    public abstract String getHighValue();

    public abstract String getNormalValue();

    public abstract String getLowValue();

    public abstract String getUnitValue();

    public static Builder builder() {
        return new AutoValue_RangeInfoUiModel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setVeryHighValue(@NonNull String value);

        public abstract Builder setHighValue(@NonNull String value);

        public abstract Builder setNormalValue(@NonNull String value);

        public abstract Builder setLowValue(@NonNull String value);
        
        public abstract Builder setUnitValue(@NonNull String value);

        public abstract RangeInfoUiModel build();

    }

}
