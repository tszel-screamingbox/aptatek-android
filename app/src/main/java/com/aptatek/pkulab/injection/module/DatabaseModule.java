package com.aptatek.pkulab.injection.module;

import android.content.Context;

import com.aptatek.pkulab.data.AptatekDatabase;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.manager.keystore.KeyStoreError;
import com.aptatek.pkulab.domain.manager.keystore.KeyStoreManager;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

import java.io.File;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module(includes = DatabaseMapperModule.class)
public class DatabaseModule {

    @Provides
    @Named("databaseName")
    public String provideDatabaseName() {
        return "aptatek.db";
    }

    @Provides
    @Named("databasePassPhrase")
    public char[] providePassPhrase(final KeyStoreManager keyStoreManager, final PreferenceManager preferenceManager) {
        try {
            return keyStoreManager.decrypt(preferenceManager.getEncryptedPin()).getChars();
        } catch (final KeyStoreError keyStoreError) {
            Timber.d("Failed to decrypt pin");
        }

        return new char[0];
    }

    @Provides
    @Named("databaseFile")
    public File provideDbFile(@Named("databaseName") String name, @ApplicationContext Context context) {
        return context.getDatabasePath(name);
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
