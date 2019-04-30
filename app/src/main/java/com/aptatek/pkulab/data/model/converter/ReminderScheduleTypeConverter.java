package com.aptatek.pkulab.data.model.converter;


import androidx.room.TypeConverter;

import com.aptatek.pkulab.data.model.ReminderScheduleDataType;

import static com.aptatek.pkulab.data.model.ReminderScheduleDataType.BIWEEKLY;
import static com.aptatek.pkulab.data.model.ReminderScheduleDataType.MONTHLY;
import static com.aptatek.pkulab.data.model.ReminderScheduleDataType.WEEKLY;

public class ReminderScheduleTypeConverter {

    @TypeConverter
    public static ReminderScheduleDataType toStatus(final int type) {
        if (type == WEEKLY.getCode()) {
            return WEEKLY;
        } else if (type == MONTHLY.getCode()) {
            return MONTHLY;
        } else if (type == BIWEEKLY.getCode()) {
            return BIWEEKLY;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static Integer toInteger(final ReminderScheduleDataType reminderScheduleType) {
        return reminderScheduleType.getCode();
    }
}