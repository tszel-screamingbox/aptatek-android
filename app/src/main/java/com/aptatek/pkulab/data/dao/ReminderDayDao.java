package com.aptatek.pkulab.data.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.aptatek.pkulab.data.model.ReminderDayDataModel;

import java.util.List;

@Dao
public interface ReminderDayDao {

    @Insert
    void insertAll(List<ReminderDayDataModel> days);

    @Query("SELECT * FROM reminderDays")
    List<ReminderDayDataModel> getReminderDays();

    @Query("SELECT COUNT(*) FROM reminderDays")
    int getReminderDaysCount();

    @Query("UPDATE reminderDays SET active = :active WHERE weekDay = :id")
    void updateReminderDayActiveState(int id, boolean active);
}
