package com.aptatek.pkulab.domain.model.reader;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TestProgress {

    public abstract long getStart();

    public abstract long getEnd();

    public abstract int getPercent();

    public static TestProgress create(final long start, final long end, final int percent) {
        return new AutoValue_TestProgress(start, end, percent);
    }

}
