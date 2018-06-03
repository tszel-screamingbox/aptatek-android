package com.aptatek.aptatek.injection.module;

import android.app.Application;
import android.content.Context;

import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

import dagger.Module;
import dagger.Provides;


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
}
