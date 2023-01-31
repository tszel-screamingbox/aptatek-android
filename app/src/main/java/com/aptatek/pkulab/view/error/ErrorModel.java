package com.aptatek.pkulab.view.error;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ErrorModel implements Parcelable {

    public abstract boolean isAfterChamberScrewedOn();

    public abstract String getErrorCode();

    public abstract String getTitle();

    public abstract String getMessage();

    public static Builder builder() {
        return new AutoValue_ErrorModel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setAfterChamberScrewedOn(final boolean isAfterChamberScrewedOn);

        public abstract Builder setErrorCode(final String errorCode);

        public abstract Builder setTitle(final String title);

        public abstract Builder setMessage(final String message);

        public abstract ErrorModel build();

    }
}
