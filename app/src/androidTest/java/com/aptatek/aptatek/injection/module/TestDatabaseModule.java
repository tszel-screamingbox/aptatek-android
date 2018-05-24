package com.aptatek.aptatek.injection.module;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.aptatek.aptatek.data.AptatekDatabase;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;

public class TestDatabaseModule extends DatabaseModule {

    @Override
    public AptatekDatabase provideDatabase(String databaseName,
                                           Context context,
                                           SafeHelperFactory safeHelperFactory) {
        return Room.inMemoryDatabaseBuilder(context, AptatekDatabase.class).build();
    }

}