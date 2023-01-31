package com.aptatek.pkulab.domain.model.reader;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CartridgeInfo {

    public abstract long getExpiry();

    public abstract String getCalibration();

    public abstract long getDate();

    public abstract String getLot();

    public abstract String getType();

    public static Builder builder() {
        return new AutoValue_CartridgeInfo.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setExpiry(final long expiry);

        public abstract Builder setCalibration(final String calibration);

        public abstract Builder setDate(final long date);

        public abstract Builder setLot(final String lot);

        public abstract Builder setType(final String type);

        public abstract CartridgeInfo build();

    }

}
