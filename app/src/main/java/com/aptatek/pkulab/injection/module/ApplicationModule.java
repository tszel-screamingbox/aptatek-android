package com.aptatek.pkulab.injection.module;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationManagerCompat;

import com.aptatek.pkulab.BuildConfig;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;


@Module
public class ApplicationModule {
    protected final Application application;

    public ApplicationModule(final Application application) {
        this.application = application;
    }

    @Provides
    public Application provideApplication() {
        return application;
    }

    @ApplicationContext
    @Provides
    public Context provideContext() {
        return application.getApplicationContext();
    }

    @Provides
    NotificationManagerCompat provideNotificationManagerCompat() {
        return NotificationManagerCompat.from(application);
    }

    @Provides
    NotificationManager provideNotificationManager() {
        return (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    Timber.Tree provideLogger() {
        if (BuildConfig.DEBUG) {
            return new Timber.DebugTree();
        }

        return new Timber.Tree() {
            @Override
            protected void log(final int priority, final String tag, final String message, final Throwable t) {
                // do nothing
            }
        };
    }
}
