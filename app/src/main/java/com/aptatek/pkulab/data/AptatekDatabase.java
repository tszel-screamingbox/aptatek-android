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


@Database(entities = {ReminderDayDataModel.class, ReminderDataModel.class, TestResultDataModel.class}, version = 5)
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
                .addMigrations(MIGRATION_3_4)
                .addMigrations(MIGRATION_4_5)
                .build();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `reminders` ADD COLUMN `reminderScheduleType` INTEGER NOT NULL DEFAULT 0");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `test_results`(`id` TEXT NOT NULL, `readerId` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `pkuLevel` REAL NOT NULL, `sick` INTEGER NOT NULL DEFAULT 0, `fasting` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`id`))");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `valid` INTEGER NOT NULL DEFAULT 0");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `endTimestamp` INTEGER NOT NULL DEFAULT -1");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `overallResult` TEXT");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `temperature` TEXT");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `humidity` TEXT");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `hardwareVersion` TEXT");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `softwareVersion` TEXT");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `firmwareVersion` TEXT");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `configHash` TEXT");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `cassetteLot` INTEGER NOT NULL DEFAULT -1");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `assayHash` TEXT");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `assayVersion` INTEGER NOT NULL DEFAULT -1");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `assay` TEXT");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `numericValue` NUMBER NOT NULL DEFAULT -1.0");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `unit` TEXT NOT NULL DEFAULT 'MABS'");
            database.execSQL("ALTER TABLE `test_results` ADD COLUMN `textResult` TEXT");

        }
    };
}
