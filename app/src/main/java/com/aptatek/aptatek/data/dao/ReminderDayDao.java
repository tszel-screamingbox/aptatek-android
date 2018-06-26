package com.aptatek.aptatek.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.aptatek.aptatek.data.model.ReminderDayDataModel;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ReminderDayDao {

    @Insert
    void insertAll(List<ReminderDayDataModel> days);

    @Query("SELECT * FROM reminderDays")
    Single<List<ReminderDayDataModel>> getReminderDays();

    @Query("UPDATE reminderDays SET active = :active WHERE weekDay = :id")
    void updateReminderDayActiveState(int id, boolean active);
}
