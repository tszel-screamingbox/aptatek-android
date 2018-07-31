package com.aptatek.aptatek.domain.respository.chart;

import com.aptatek.aptatek.domain.model.PkuLevel;
import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class CubeData {

    public abstract long getId();

    public abstract Date getDate();

    public abstract PkuLevel getMeasure();

    public static CubeData create(final long id, final Date date, final PkuLevel measure) {
        return new AutoValue_CubeData(id, date, measure);
    }
}
