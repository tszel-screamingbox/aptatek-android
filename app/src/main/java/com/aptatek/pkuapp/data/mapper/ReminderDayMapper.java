package com.aptatek.pkuapp.data.mapper;

import com.aptatek.pkuapp.data.model.ReminderDayDataModel;
import com.aptatek.pkuapp.domain.base.Mapper;
import com.aptatek.pkuapp.domain.model.ReminderDay;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ix.Ix;

public class ReminderDayMapper implements Mapper<ReminderDay, ReminderDayDataModel> {

    @Inject
    public ReminderDayMapper() {
    }

    @Override
    public List<ReminderDay> mapListToDomain(final List<ReminderDayDataModel> dataModels) {
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
    public List<ReminderDayDataModel> mapListToData(final List<ReminderDay> domainModels) {
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
