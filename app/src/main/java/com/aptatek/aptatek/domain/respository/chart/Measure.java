package com.aptatek.aptatek.domain.respository.chart;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Measure {

    public abstract int getPhenylalanineLevel();

    public abstract double getUnit();

    public static Measure create(final int level, final double unit) {
        return new AutoValue_Measure(level, unit);
    }
}
