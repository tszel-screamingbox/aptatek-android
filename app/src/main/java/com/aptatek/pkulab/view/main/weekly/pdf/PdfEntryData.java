package com.aptatek.pkulab.view.main.weekly.pdf;

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

    public abstract String getAverageCount();

    public abstract String getStandardText();

    public abstract String getIncreasedText();

    public abstract String getHighText();

    public abstract String getVeryHighText();

    public abstract double getDeviation();

    public abstract String getMin();

    public abstract String getMax();

    public abstract String getFileName();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_PdfEntryData.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setStandardText(String standardText);

        public abstract Builder setIncreasedText(String normalText);

        public abstract Builder setHighText(String highText);

        public abstract Builder setVeryHighText(String veryHighText);


        public abstract Builder setFileName(String fileName);

        public abstract Builder setFormattedDate(String date);

        public abstract Builder setDaysOfMonth(int days);

        public abstract Builder setBubbleDataSet(BubbleDataSet bubbleDataSet);

        public abstract Builder setUnit(String unit);

        public abstract Builder setLowCount(int lowCount);

        public abstract Builder setNormalCount(int normalCount);

        public abstract Builder setHighCount(int highCount);

        public abstract Builder setVeryHighCount(int veryHighCount);

        public abstract Builder setAverageCount(String averageCount);

        public abstract Builder setDeviation(double deviation);

        public abstract Builder setMin(String min);

        public abstract Builder setMax(String max);

        public abstract PdfEntryData build();
    }
}
