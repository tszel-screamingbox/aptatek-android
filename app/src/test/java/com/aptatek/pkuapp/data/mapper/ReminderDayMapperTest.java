package com.aptatek.pkuapp.data.mapper;

import com.aptatek.pkuapp.data.model.ReminderDayDataModel;
import com.aptatek.pkuapp.domain.model.ReminderDay;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

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
        assert reminderDay.getWeekDay() == reminderDayDataModel.getWeekDay();
        assert reminderDay.isActive() == reminderDayDataModel.isActive();
    }

    @Test
    public void testMapToData() throws Exception {
        final ReminderDay reminderDay = ReminderDay.builder()
                .setWeekDay(1)
                .setActive(true)
                .build();

        final ReminderDayDataModel reminderDayDataModel = reminderDayMapper.mapToData(reminderDay);
        assert reminderDay.getWeekDay() == reminderDayDataModel.getWeekDay();
        assert reminderDay.isActive() == reminderDayDataModel.isActive();
    }
}
