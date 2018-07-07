package com.aptatek.aptatek.data.mapper;

import com.aptatek.aptatek.data.model.ReminderDayDataModel;
import com.aptatek.aptatek.domain.base.Mapper;
import com.aptatek.aptatek.domain.model.ReminderDay;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import ix.Ix;

public class ReminderDayMapper implements Mapper<ReminderDay, ReminderDayDataModel> {

    @Inject
    public ReminderDayMapper() {
    }

    @Override
    public Collection<ReminderDay> mapListToDomain(final Collection<ReminderDayDataModel> dataModels) {
        return Ix.from(dataModels).map(this::mapToDomain).toList();
    }

    @Override
    public ReminderDay mapToDomain(final ReminderDayDataModel dataModel) {
        return ReminderDay.builder()
                .setWeekDay(dataModel.getWeekDay())
                .setActive(dataModel.isActive())
                .setReminders(new ArrayList<>())
                .build();
    }

    @Override
    public Collection<ReminderDayDataModel> mapListToData(final Collection<ReminderDay> domainModels) {
        return Ix.from(domainModels).map(this::mapToData).toList();
    }

    @Override
    public ReminderDayDataModel mapToData(final ReminderDay domainModel) {
        final ReminderDayDataModel reminderDayDataModel = new ReminderDayDataModel();
        reminderDayDataModel.setWeekDay(domainModel.getWeekDay());
        reminderDayDataModel.setActive(domainModel.isActive());
        return reminderDayDataModel;
    }
}
