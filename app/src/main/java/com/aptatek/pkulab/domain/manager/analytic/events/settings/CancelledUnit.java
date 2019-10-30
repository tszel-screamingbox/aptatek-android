package com.aptatek.pkulab.domain.manager.analytic.events.settings;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;

import java.util.HashMap;
import java.util.Map;

import static com.aptatek.pkulab.domain.manager.analytic.EventCategory.USER_BEHAVIOUR;

public class CancelledUnit extends AnalyticsEvent {

    private final PkuLevelUnits units;

    public CancelledUnit(final PkuLevelUnits units) {
        super("settings_unit_save_cancelled", System.currentTimeMillis(), USER_BEHAVIOUR);
        this.units = units;
    }


    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("selected_unit", units.name());
        return map;
    }

    @Override
    public String toString() {
        return "CancelledUnit{" +
                "{selected_unit=" + units.name() +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
