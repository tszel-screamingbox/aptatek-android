package com.aptatek.pkulab.domain.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MonthPickerDialogModel implements Parcelable {

    public abstract int getMinMonth();

    public abstract int getMinYear();

    public static Builder builder() {
        return new AutoValue_MonthPickerDialogModel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setMinMonth(int minMonth);

        public abstract Builder setMinYear(int minYear);

        public abstract MonthPickerDialogModel build();
    }
}
