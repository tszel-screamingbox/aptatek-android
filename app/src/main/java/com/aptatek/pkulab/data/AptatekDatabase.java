package com.aptatek.pkulab.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aptatek.pkulab.data.dao.ReminderDao;
import com.aptatek.pkulab.data.dao.ReminderDayDao;
import com.aptatek.pkulab.data.dao.TestResultDao;
import com.aptatek.pkulab.data.model.ReminderDataModel;
import com.aptatek.pkulab.data.model.ReminderDayDataModel;
import com.aptatek.pkulab.data.model.TestResultDataModel;
import com.aptatek.pkulab.data.model.converter.PkuLevelTypeConverter;
import com.aptatek.pkulab.data.model.converter.ReminderScheduleTypeConverter;
import com.commonsware.cwac.saferoom.SafeHelperFactory;


@Database(entities = {ReminderDayDataModel.class, ReminderDataModel.class, TestResultDataModel.class}, version = 3)
@TypeConverters({ReminderScheduleTypeConverter.class, PkuLevelTypeConverter.class})
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

    public abstract TestResultDao getTestResultDao();

    private static AptatekDatabase creator(final String databaseName,
                                           final Context context,
                                           final SafeHelperFactory safeHelperFactory) {
        return Room.databaseBuilder(context, AptatekDatabase.class, databaseName)
                .openHelperFactory(safeHelperFactory)
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .build();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE reminders ADD COLUMN reminderScheduleType INTEGER NOT NULL DEFAULT 0");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS test_results(`id` TEXT NOT NULL, `readerId` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `pkuLevel` REAL NOT NULL, `sick` INTEGER NOT NULL DEFAULT 0, `fasting` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`id`))");
        }
    };
}
