package com.aptatek.pkulab.injection.module;

import android.content.Context;

import com.aptatek.pkulab.data.AptatekDatabase;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

import javax.inject.Named;

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
        return "InitialPassPhraseWillBeReKeyedWithPinLater".toCharArray();
    }

    @Provides
    public SafeHelperFactory provideSafeHelperFactory(@Named("databasePassPhrase") final char[] pass) {
        return new SafeHelperFactory(pass);
    }

    @Provides
    public AptatekDatabase provideDatabase(@Named("databaseName") final String databaseName,
                                           @ApplicationContext final Context context,
                                           final SafeHelperFactory safeHelperFactory) {
        return AptatekDatabase.getInstance(databaseName, context, safeHelperFactory);
    }

}
