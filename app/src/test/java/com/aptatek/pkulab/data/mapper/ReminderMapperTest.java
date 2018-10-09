package com.aptatek.pkulab.data.mapper;

import com.aptatek.pkulab.data.model.ReminderDataModel;
import com.aptatek.pkulab.domain.model.Reminder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

public class ReminderMapperTest {

    private ReminderMapper reminderMapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reminderMapper = new ReminderMapper();
    }

    @Test
    public void testMapToDomain() throws Exception {
        final ReminderDataModel reminderDataModel = new ReminderDataModel(UUID.randomUUID().toString());
        reminderDataModel.setHour(10);
        reminderDataModel.setMinute(10);
        reminderDataModel.setWeekDay(10);


        final Reminder reminder = reminderMapper.mapToDomain(reminderDataModel);
        assert reminderDataModel.getWeekDay() == reminder.getWeekDay();
        assert reminderDataModel.getId().equals(reminder.getId());
        assert reminderDataModel.getMinute() == reminder.getMinute();
        assert reminderDataModel.getHour() == reminder.getHour();
    }

    @Test
    public void testMapToData() throws Exception {
        final Reminder reminder = Reminder.builder()
                .setWeekDay(1)
                .setId(UUID.randomUUID().toString())
                .setMinute(10)
                .setHour(10)
                .build();

        final ReminderDataModel reminderDataModel = reminderMapper.mapToData(reminder);
        assert reminderDataModel.getWeekDay() == reminder.getWeekDay();
        assert reminderDataModel.getId().equals(reminder.getId());
        assert reminderDataModel.getMinute() == reminder.getMinute();
        assert reminderDataModel.getHour() == reminder.getHour();
    }
}
