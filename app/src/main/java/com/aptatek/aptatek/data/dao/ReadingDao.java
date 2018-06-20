package com.aptatek.aptatek.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.aptatek.aptatek.data.model.ReadingDataModel;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ReadingDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void addReading(ReadingDataModel readingDataModel);

    @Query("SELECT * FROM readings")
    Single<List<ReadingDataModel>> getReadings();

}
