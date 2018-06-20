package com.aptatek.aptatek.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.data.dao.ReadingDao;
import com.aptatek.aptatek.data.dao.ReminderDao;
import com.aptatek.aptatek.data.dao.ReminderDayDao;
import com.aptatek.aptatek.data.model.ReadingDataModel;
import com.aptatek.aptatek.data.model.ReminderDayDataModel;
import com.aptatek.aptatek.data.model.ReminderDataModel;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

import java.util.concurrent.Executors;

@Database(entities = {ReadingDataModel.class, ReminderDayDataModel.class, ReminderDataModel.class}, version = 1)
public abstract class AptatekDatabase extends RoomDatabase {

    private static AptatekDatabase INSTANCE;

    public synchronized static AptatekDatabase getInstance(String databaseName,
                                                           Context context,
                                                           SafeHelperFactory safeHelperFactory) {
        if (INSTANCE == null) {
            INSTANCE = creator(databaseName, context, safeHelperFactory);
        }
        return INSTANCE;
    }

    public abstract ReadingDao getReadingDao();

    public abstract ReminderDao getReminderDao();

    public abstract ReminderDayDao getReminderDayDao();

    private static AptatekDatabase creator(String databaseName,
                                           Context context,
                                           SafeHelperFactory safeHelperFactory) {
        return Room.databaseBuilder(
                context,
                AptatekDatabase.class,
                databaseName)
                .openHelperFactory(safeHelperFactory)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() ->
                                getInstance(databaseName, context, safeHelperFactory).getReminderDayDao().insertAll(ReminderDayDataModel.creator()));
                    }
                })
                .build();
    }
}
