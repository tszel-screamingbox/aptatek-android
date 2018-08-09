package com.aptatek.pkuapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.aptatek.pkuapp.data.dao.ReminderDao;
import com.aptatek.pkuapp.data.dao.ReminderDayDao;
import com.aptatek.pkuapp.data.model.ReminderDataModel;
import com.aptatek.pkuapp.data.model.ReminderDayDataModel;
import com.commonsware.cwac.saferoom.SafeHelperFactory;


@Database(entities = {ReminderDayDataModel.class, ReminderDataModel.class}, version = 1)
public abstract class AptatekDatabase extends RoomDatabase {

    private static volatile AptatekDatabase instance;

    public static synchronized AptatekDatabase getInstance(final String databaseName,
                                                           final Context context,
                                                           final SafeHelperFactory safeHelperFactory) {
        if (instance == null) {
            instance = creator(databaseName, context, safeHelperFactory);
        }
        return instance;
    }

    public abstract ReminderDao getReminderDao();

    public abstract ReminderDayDao getReminderDayDao();

    private static AptatekDatabase creator(final String databaseName,
                                           final Context context,
                                           final SafeHelperFactory safeHelperFactory) {
        return Room.databaseBuilder(
                context,
                AptatekDatabase.class,
                databaseName)
                .openHelperFactory(safeHelperFactory)
                .build();
    }
}