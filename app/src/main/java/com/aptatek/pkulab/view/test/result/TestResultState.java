package com.aptatek.pkulab.view.test.result;

import android.support.annotation.ColorInt;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TestResultState {

    public abstract String getTitle();

    public abstract @ColorInt
    int getColor();

    public abstract String getFormattedPkuValue();

    public abstract String getPkuLevelText();

    public abstract String getPkuUnit();

    public static Builder builder() {
        return new AutoValue_TestResultState.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setTitle(String title);

        public abstract Builder setColor(@ColorInt int color);

        public abstract Builder setFormattedPkuValue(String value);

        public abstract Builder setPkuLevelText(String level);

        public abstract Builder setPkuUnit(String unit);

        public abstract TestResultState build();

    }

}
