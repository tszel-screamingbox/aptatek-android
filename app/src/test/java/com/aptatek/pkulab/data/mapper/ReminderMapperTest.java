package com.aptatek.pkulab.data.mapper;

import com.aptatek.pkulab.data.model.ReminderDataModel;
import com.aptatek.pkulab.data.model.ReminderScheduleDataType;
import com.aptatek.pkulab.domain.model.Reminder;
import com.aptatek.pkulab.domain.model.ReminderScheduleType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
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
        reminderDataModel.setReminderScheduleType(ReminderScheduleDataType.WEEKLY);


        final Reminder reminder = reminderMapper.mapToDomain(reminderDataModel);
        compare(reminderDataModel, reminder);
    }

    @Test
    public void testMapToDomainList() throws Exception {
        final List<ReminderDataModel> dataModels = new ArrayList<>();
        final ReminderDataModel one = new ReminderDataModel("a");
        one.setHour(1);
        one.setMinute(11);
        one.setReminderScheduleType(ReminderScheduleDataType.BIWEEKLY);
        one.setWeekDay(1);
        dataModels.add(one);
        final ReminderDataModel two = new ReminderDataModel("b");
        two.setHour(3);
        two.setMinute(133);
        two.setReminderScheduleType(ReminderScheduleDataType.MONTHLY);
        two.setWeekDay(2);
        dataModels.add(two);

        final List<Reminder> reminders = reminderMapper.mapListToDomain(dataModels);
        for (int i = 0; i < reminders.size(); i++) {
            compare(dataModels.get(i), reminders.get(i));
        }
    }

    private void compare(final ReminderDataModel reminderDataModel, final Reminder reminder) {
        assert reminderDataModel.getWeekDay() == reminder.getWeekDay();
        assert reminderDataModel.getId().equals(reminder.getId());
        assert reminderDataModel.getMinute() == reminder.getMinute();
        assert reminderDataModel.getHour() == reminder.getHour();
        assert reminderMapper.scheduleTypeToData(reminderDataModel.getReminderScheduleType()) == reminder.getReminderScheduleType();
    }

    @Test
    public void testMapToData() throws Exception {
        final Reminder reminder = Reminder.builder()
                .setWeekDay(1)
                .setId(UUID.randomUUID().toString())
                .setMinute(10)
                .setHour(10)
                .setReminderScheduleType(ReminderScheduleType.WEEKLY)
                .build();

        final ReminderDataModel reminderDataModel = reminderMapper.mapToData(reminder);
        compare(reminderDataModel, reminder);
    }

    @Test
    public void testMapToDataList() throws Exception {
        final List<Reminder> models = new ArrayList<>();
        final Reminder one = Reminder.builder()
                .setHour(1)
                .setId("a")
                .setMinute(11)
                .setReminderScheduleType(ReminderScheduleType.BIWEEKLY)
                .setWeekDay(1)
                .build();
        models.add(one);
        final Reminder two = Reminder.builder()
                .setHour(3)
                .setId("b")
                .setMinute(33)
                .setReminderScheduleType(ReminderScheduleType.MONTHLY)
                .setWeekDay(2)
                .build();
        models.add(two);

        final List<ReminderDataModel> reminderDataModels = reminderMapper.mapListToData(models);
        for (int i = 0; i < reminderDataModels.size(); i++) {
            compare(reminderDataModels.get(i), models.get(i));
        }
    }

}
