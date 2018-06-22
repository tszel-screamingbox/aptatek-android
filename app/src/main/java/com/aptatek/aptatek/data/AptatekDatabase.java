package com.aptatek.aptatek.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.data.dao.ReminderDao;
import com.aptatek.aptatek.data.dao.ReminderDayDao;
import com.aptatek.aptatek.data.model.ReminderDataModel;
import com.aptatek.aptatek.data.model.ReminderDayDataModel;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

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
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull final SupportSQLiteDatabase db) {
                        super.onCreate(db);

                            Completable.fromAction(() ->
                                    getInstance(databaseName, context, safeHelperFactory).getReminderDayDao()
                                        .insertAll(ReminderDayDataModel.creator())
                            ).subscribeOn(Schedulers.newThread()).subscribe();
                    }
                })
                .build();
    }
}