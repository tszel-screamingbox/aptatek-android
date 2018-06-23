package com.aptatek.aptatek.data.mapper;

import com.aptatek.aptatek.data.model.ReminderDataModel;
import com.aptatek.aptatek.domain.model.Reminder;

import java.util.List;

import javax.inject.Inject;

import ix.Ix;

public class ReminderMapper {

    @Inject
    public ReminderMapper() {
    }

    public Reminder toDomain(final ReminderDataModel reminderDataModel) {
        return Reminder.builder()
                .setId(reminderDataModel.getId())
                .setHour(reminderDataModel.getHour())
                .setMinute(reminderDataModel.getMinute())
                .setWeekDay(reminderDataModel.getWeekDay())
                .build();
    }

    public ReminderDataModel toData(final Reminder reminder) {
        final ReminderDataModel reminderDataModel = new ReminderDataModel();
        reminderDataModel.setId(reminder.getId());
        reminderDataModel.setWeekDay(reminder.getWeekDay());
        reminderDataModel.setHour(reminder.getHour());
        reminderDataModel.setMinute(reminder.getMinute());
        return reminderDataModel;
    }

    public List<Reminder> toDomainList(final List<ReminderDataModel> reminderDataModels) {
        return Ix.from(reminderDataModels).map(this::toDomain).toList();
    }
}
