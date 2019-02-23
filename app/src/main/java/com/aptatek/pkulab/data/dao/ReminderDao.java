package com.aptatek.pkulab.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.aptatek.pkulab.data.model.ReminderDataModel;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert
    void insert(ReminderDataModel reminderDataModel);

    @Query("SELECT * FROM reminders WHERE weekDay = :weekDay")
    List<ReminderDataModel> getReminders(int weekDay);

    @Query("UPDATE reminders SET hour = :hour, minute = :minute, reminderScheduleType = :reminderScheduleDataType WHERE id = :id")
    void updateReminder(String id, int hour, int minute, int reminderScheduleDataType);

    @Query("DELETE FROM reminders WHERE id = :id")
    void deleteReminder(String id);
}
