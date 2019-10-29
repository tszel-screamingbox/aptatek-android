package com.aptatek.pkulab.domain.manager.analytic.events.settings;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;
import com.aptatek.pkulab.domain.model.ReminderScheduleType;

import java.util.HashMap;
import java.util.Map;

import static com.aptatek.pkulab.domain.manager.analytic.EventCategory.USER_BEHAVIOUR;
import static java.lang.System.currentTimeMillis;

public class Reminder extends AnalyticsEvent {

    private final String day;
    private final Integer minutes;
    private final ReminderScheduleType scheduleType;

    public Reminder(final String eventName, final String day, final ReminderScheduleType type) {
        super(eventName, currentTimeMillis(), USER_BEHAVIOUR);
        this.day = day;
        this.scheduleType = type;
        this.minutes = null;
    }

    public Reminder(final int minutes) {
        super("reminder_snoozed", currentTimeMillis(), USER_BEHAVIOUR);
        this.day = null;
        this.scheduleType = null;
        this.minutes = minutes;
    }

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        if (day != null) {
            map.put("day", day);
        }

        if (scheduleType != null) {
            map.put("frequency", scheduleType.getShortName());
        }

        if (minutes != null) {
            map.put("minutes", minutes.toString());
        }
        return map;
    }

    @Override
    public String toString() {
        return eventName +
                "{day=" + day + '\'' +
                ", frequency='" + scheduleType.getShortName() + '\'' +
                ", minutes='" + minutes + '\'' +
                ", timestamp=" + timestamp + '\'' +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
