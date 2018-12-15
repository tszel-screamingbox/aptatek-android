package com.aptatek.pkulab.data.mapper;

import com.aptatek.pkulab.data.model.ReminderDataModel;
import com.aptatek.pkulab.data.model.ReminderScheduleDataType;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.model.Reminder;
import com.aptatek.pkulab.domain.model.ReminderScheduleType;

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
                .setReminderScheduleType(scheduleTypeToData(dataModel.getReminderScheduleType()))
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
        reminderDataModel.setReminderScheduleType(scheduleDataTypeToDomain(domainModel.getReminderScheduleType()));
        return reminderDataModel;
    }

    public ReminderScheduleType scheduleTypeToData(final ReminderScheduleDataType reminderScheduleDataType) {
        if (reminderScheduleDataType.getCode() == ReminderScheduleType.WEEKLY.getCode()) {
            return ReminderScheduleType.WEEKLY;
        } else if (reminderScheduleDataType.getCode() == ReminderScheduleType.MONTHLY.getCode()) {
            return ReminderScheduleType.MONTHLY;
        } else {
            return ReminderScheduleType.BIWEEKLY;
        }
    }

    public ReminderScheduleDataType scheduleDataTypeToDomain(final ReminderScheduleType reminderScheduleType) {
        if (reminderScheduleType.getCode() == ReminderScheduleDataType.WEEKLY.getCode()) {
            return ReminderScheduleDataType.WEEKLY;
        } else if (reminderScheduleType.getCode() == ReminderScheduleDataType.MONTHLY.getCode()) {
            return ReminderScheduleDataType.MONTHLY;
        } else {
            return ReminderScheduleDataType.BIWEEKLY;
        }
    }
}
