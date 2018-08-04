package com.aptatek.aptatek.data.mapper;

import com.aptatek.aptatek.data.model.ReminderDataModel;
import com.aptatek.aptatek.domain.base.Mapper;
import com.aptatek.aptatek.domain.model.Reminder;

import java.util.List;

import javax.inject.Inject;

import ix.Ix;

public class ReminderMapper implements Mapper<Reminder, ReminderDataModel> {

    @Inject
    public ReminderMapper() {
    }

    @Override
    public List<Reminder> mapListToDomain(final List<ReminderDataModel> dataModels) {
        return Ix.from(dataModels).map(this::mapToDomain).toList();
    }

    @Override
    public Reminder mapToDomain(final ReminderDataModel dataModel) {
        return Reminder.builder()
                .setId(dataModel.getId())
                .setHour(dataModel.getHour())
                .setMinute(dataModel.getMinute())
                .setWeekDay(dataModel.getWeekDay())
                .build();
    }

    @Override
    public List<ReminderDataModel> mapListToData(final List<Reminder> domainModels) {
        return Ix.from(domainModels).map(this::mapToData).toList();
    }

    @Override
    public ReminderDataModel mapToData(final Reminder domainModel) {
        final ReminderDataModel reminderDataModel = new ReminderDataModel(domainModel.getId());
        reminderDataModel.setWeekDay(domainModel.getWeekDay());
        reminderDataModel.setHour(domainModel.getHour());
        reminderDataModel.setMinute(domainModel.getMinute());
        return reminderDataModel;
    }
}
