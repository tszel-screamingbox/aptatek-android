package com.aptatek.pkuapp.view.weekly.pdf;

import com.github.mikephil.charting.data.BubbleDataSet;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PdfEntryData {

    public abstract String getFormattedDate();

    public abstract int getDaysOfMonth();

    public abstract BubbleDataSet getBubbleDataSet();

    public abstract String getUnit();

    public abstract int getLowCount();

    public abstract int getNormalCount();

    public abstract int getHighCount();

    public abstract int getVeryHighCount();

    public abstract int getAverageCount();

    public abstract int getFastingCount();

    public abstract int getSickCount();

    public abstract double getDeviation();

    public abstract String getFileName();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_PdfEntryData.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setFileName(final String fileName);

        public abstract Builder setFormattedDate(final String date);

        public abstract Builder setDaysOfMonth(final int days);

        public abstract Builder setBubbleDataSet(final BubbleDataSet bubbleDataSet);

        public abstract Builder setUnit(final String unit);

        public abstract Builder setLowCount(final int lowCount);

        public abstract Builder setNormalCount(final int normalCount);

        public abstract Builder setHighCount(final int highCount);

        public abstract Builder setVeryHighCount(final int veryHighCount);

        public abstract Builder setAverageCount(final int averageCount);

        public abstract Builder setFastingCount(final int fastingCount);

        public abstract Builder setSickCount(final int sickCount);

        public abstract Builder setDeviation(final double deviation);

        public abstract PdfEntryData build();
    }
}
