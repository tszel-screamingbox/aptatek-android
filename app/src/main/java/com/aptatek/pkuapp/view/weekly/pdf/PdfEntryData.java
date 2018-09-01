package com.aptatek.pkuapp.view.weekly.pdf;

import com.github.mikephil.charting.data.BubbleDataSet;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PdfEntryData {

    public abstract String getDate();

    public abstract BubbleDataSet getBubbleDataSet();

    public abstract String getUnit();

    public abstract int getLowCount();

    public abstract int getNormalCount();

    public abstract int getHighCount();

    public abstract int getVeryHighCount();

    public abstract int getAverageCount();

    public abstract int getFastingCount();

    public abstract int getSickCount();

    public static Builder builder() {
        return new AutoValue_PdfEntryData.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setDate(String date);

        public abstract Builder setBubbleDataSet(BubbleDataSet bubbleDataSet);

        public abstract Builder setUnit(String unit);

        public abstract Builder setLowCount(int lowCount);

        public abstract Builder setNormalCount(int normalCount);

        public abstract Builder setHighCount(int highCount);

        public abstract Builder setVeryHighCount(int veryHighCount);

        public abstract Builder setAverageCount(int averageCount);

        public abstract Builder setFastingCount(int fastingCount);

        public abstract Builder setSickCount(int sickCount);

        public abstract PdfEntryData build();
    }
}
