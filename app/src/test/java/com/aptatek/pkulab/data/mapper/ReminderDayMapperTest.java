package com.aptatek.pkulab.data.mapper;

import com.aptatek.pkulab.data.model.ReminderDayDataModel;
import com.aptatek.pkulab.domain.model.ReminderDay;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReminderDayMapperTest {

    private ReminderDayMapper reminderDayMapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reminderDayMapper = new ReminderDayMapper();
    }

    @Test
    public void testMapToDomain() throws Exception {
        final ReminderDayDataModel reminderDayDataModel = new ReminderDayDataModel();
        reminderDayDataModel.setActive(true);
        reminderDayDataModel.setWeekDay(1);

        final ReminderDay reminderDay = reminderDayMapper.mapToDomain(reminderDayDataModel);
        compare(reminderDayDataModel, reminderDay);
    }

    private void compare(final ReminderDayDataModel model, final ReminderDay reminderDay) throws Exception {
        assert reminderDay.getWeekDay() == model.getWeekDay();
        assert reminderDay.isActive() == model.isActive();
    }

    @Test
    public void testMapToDomainList() throws Exception {
        final List<ReminderDayDataModel> dayModels = new ArrayList<>();
        final ReminderDayDataModel one = new ReminderDayDataModel();
        one.setActive(true);
        one.setWeekDay(1);
        dayModels.add(one);
        final ReminderDayDataModel two = new ReminderDayDataModel();
        two.setActive(false);
        two.setWeekDay(5);
        dayModels.add(two);

        final List<ReminderDay> reminderDays = reminderDayMapper.mapListToDomain(dayModels);
        for (int i = 0; i < reminderDays.size(); i++) {
            compare(dayModels.get(i), reminderDays.get(i));
        }
    }

    @Test
    public void testMapToData() throws Exception {
        final ReminderDay reminderDay = ReminderDay.builder()
                .setWeekDay(1)
                .setActive(true)
                .build();

        final ReminderDayDataModel reminderDayDataModel = reminderDayMapper.mapToData(reminderDay);
        compare(reminderDayDataModel, reminderDay);
    }

    @Test
    public void testMapToDataList() throws Exception {
        final List<ReminderDay> reminderDays = new ArrayList<>();
        final ReminderDay one = ReminderDay.builder()
                .setActive(true)
                .setWeekDay(1)
                .setReminders(Collections.emptyList())
                .build();
        reminderDays.add(one);
        final ReminderDay two = ReminderDay.builder()
                .setActive(false)
                .setWeekDay(4)
                .setReminders(Collections.emptyList())
                .build();
        reminderDays.add(two);

        final List<ReminderDayDataModel> reminderDayDataModels = reminderDayMapper.mapListToData(reminderDays);
        for (int i = 0; i < reminderDays.size(); i++) {
            compare(reminderDayDataModels.get(i), reminderDays.get(i));
        }
    }
}
