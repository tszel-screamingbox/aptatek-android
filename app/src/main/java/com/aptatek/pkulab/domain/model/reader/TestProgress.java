package com.aptatek.pkulab.domain.model.reader;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TestProgress {

    public abstract String getTestId();

    public abstract long getStart();

    public abstract long getEnd();

    public abstract int getPercent();

    public static TestProgress create(final String testId, final long start, final long end, final int percent) {
        return new AutoValue_TestProgress(testId, start, end, percent);
    }

}
