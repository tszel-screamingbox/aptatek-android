package com.aptatek.pkulab.view.weekly.pdf;

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

    public abstract String getNormalCeilValue();

    public abstract String getNormalFloorValue();

    public abstract int getSickCount();

    public abstract double getDeviation();

    public abstract String getFileName();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_PdfEntryData.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setNormalCeilValue(String normalCeilValue);

        public abstract Builder setNormalFloorValue(String normalFloorValue);

        public abstract Builder setFileName(String fileName);

        public abstract Builder setFormattedDate(String date);

        public abstract Builder setDaysOfMonth(int days);

        public abstract Builder setBubbleDataSet(BubbleDataSet bubbleDataSet);

        public abstract Builder setUnit(String unit);

        public abstract Builder setLowCount(int lowCount);

        public abstract Builder setNormalCount(int normalCount);

        public abstract Builder setHighCount(int highCount);

        public abstract Builder setVeryHighCount(int veryHighCount);

        public abstract Builder setAverageCount(int averageCount);

        public abstract Builder setFastingCount(int fastingCount);

        public abstract Builder setSickCount(int sickCount);

        public abstract Builder setDeviation(double deviation);

        public abstract PdfEntryData build();
    }
}
