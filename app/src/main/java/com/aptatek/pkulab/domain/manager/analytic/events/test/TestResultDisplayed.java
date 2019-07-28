package com.aptatek.pkulab.domain.manager.analytic.events.test;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.util.ChartUtils;

import java.util.HashMap;
import java.util.Map;

public class TestResultDisplayed extends AnalyticsEvent {

    //result value, result level, result unit, result_timestamp
    private final PkuLevel level;
    private final ChartUtils.State state;
    private final long resultTimestamp;

    public TestResultDisplayed(final PkuLevel level, final ChartUtils.State state, final long resultTimestamp) {
        super("testing_result_displayed", null, EventCategory.READER_COMMUNICATION);
        this.level = level;
        this.state = state;
        this.resultTimestamp = resultTimestamp;
    }

    public PkuLevel getLevel() {
        return level;
    }

    public ChartUtils.State getState() {
        return state;
    }

    public long getResultTimestamp() {
        return resultTimestamp;
    }

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("result_value", String.valueOf(level.getValue()));
        map.put("result_unit", String.valueOf(level.getUnit()));
        map.put("result_level", String.valueOf(state));
        map.put("result_timestamp", String.valueOf(resultTimestamp));
        return map;
    }

    @Override
    public String toString() {
        return "TestResultDisplayed{" +
                "level=" + level +
                ", state=" + state +
                ", resultTimestamp=" + resultTimestamp +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
