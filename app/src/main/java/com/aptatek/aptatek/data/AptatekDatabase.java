package com.aptatek.aptatek.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.aptatek.aptatek.data.dao.ReadingDao;
import com.aptatek.aptatek.data.model.ReadingDataModel;

@Database(entities = {ReadingDataModel.class}, version = 1)
public abstract class AptatekDatabase extends RoomDatabase {

    public abstract ReadingDao getReadingDao();

}
