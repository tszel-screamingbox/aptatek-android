package com.aptatek.pkulab.injection.module;

import android.content.Context;

import androidx.room.Room;

import com.aptatek.pkulab.data.AptatekDatabase;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

public class TestDatabaseModule extends DatabaseModule {

    @Override
    public AptatekDatabase provideDatabase(final String databaseName,
                                           final Context context,
                                           final SafeHelperFactory safeHelperFactory) {
        return Room.inMemoryDatabaseBuilder(context, AptatekDatabase.class).build();
    }

}