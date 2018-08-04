package com.aptatek.aptatek.view.weekly.chart;

import android.support.annotation.ColorInt;

import com.github.mikephil.charting.data.BubbleEntry;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ChartEntryData {

    public abstract BubbleEntry getEntry();

    public abstract String getLabel();

    public abstract @ColorInt int getLabelColor();

    public abstract @ColorInt int getBubbleColor();

    public static ChartEntryData.Builder builder() {
        return new AutoValue_ChartEntryData.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setEntry(BubbleEntry entry);

        public abstract Builder setLabel(String label);

        public abstract Builder setLabelColor(@ColorInt int labelColor);

        public abstract Builder setBubbleColor(@ColorInt int bubbleColor);

        public abstract ChartEntryData build();

    }
}
