package com.aptatek.pkulab.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.dao.ReminderDao;
import com.aptatek.pkulab.data.dao.ReminderDayDao;
import com.aptatek.pkulab.data.model.ReminderDataModel;
import com.aptatek.pkulab.data.model.ReminderDayDataModel;
import com.aptatek.pkulab.data.model.ReminderScheduleTypeConverter;
import com.commonsware.cwac.saferoom.SafeHelperFactory;


@Database(entities = {ReminderDayDataModel.class, ReminderDataModel.class}, version = 2)
@TypeConverters({ReminderScheduleTypeConverter.class})
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
        return Room.databaseBuilder(context, AptatekDatabase.class, databaseName)
                .openHelperFactory(safeHelperFactory)
                .addMigrations(MIGRATION_1_2)
                .setJournalMode(JournalMode.TRUNCATE)
                .build();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE reminders ADD COLUMN reminderScheduleType INTEGER NOT NULL DEFAULT 0");
        }
    };
}
