package com.aptatek.aptatek.data.mapper;

import com.aptatek.aptatek.data.model.ReminderDayDataModel;
import com.aptatek.aptatek.domain.model.ReminderDay;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ix.Ix;

public class ReminderDayMapper {

    @Inject
    public ReminderDayMapper() {
    }

    public ReminderDay toDomain(final ReminderDayDataModel reminderDayDataModel) {
        return ReminderDay.builder()
                .setWeekDay(reminderDayDataModel.getWeekDay())
                .setActive(reminderDayDataModel.isActive())
                .setReminders(new ArrayList<>())
                .build();
    }

    public ReminderDayDataModel toData(final ReminderDay reminderDay) {
        final ReminderDayDataModel reminderDayDataModel = new ReminderDayDataModel();
        reminderDayDataModel.setWeekDay(reminderDay.getWeekDay());
        reminderDayDataModel.setActive(reminderDay.isActive());
        return reminderDayDataModel;
    }

    public List<ReminderDay> toDomainList(final List<ReminderDayDataModel> reminderDayDataModels) {
        return Ix.from(reminderDayDataModels).map(this::toDomain).toList();
    }
}
