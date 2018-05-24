package com.aptatek.aptatek.injection.module;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.aptatek.aptatek.data.AptatekDatabase;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Named("databaseName")
    public String provideDatabaseName() {
        return "aptatek.db";
    }

    @Provides
    @Named("databasePassPhrase")
    public char[] providePassPhrase() {
        // TODO compose passphrase from multiple places (e.g static fields)
        // TODO add salt to composed passphrase + hash it
        return "TODO:HowSecureIsThis?".toCharArray();
    }

    @Provides
    public SafeHelperFactory provideSafeHelperFactory(@Named("databasePassPhrase") char[] pass) {
        return new SafeHelperFactory(pass);
    }

    @Singleton
    @Provides
    public AptatekDatabase provideDatabase(@Named("databaseName") String databaseName,
                                           @ApplicationContext Context context,
                                           SafeHelperFactory safeHelperFactory) {
        return Room.databaseBuilder(
                context,
                AptatekDatabase.class,
                databaseName)
                .openHelperFactory(safeHelperFactory)
                .build();
    }

}
