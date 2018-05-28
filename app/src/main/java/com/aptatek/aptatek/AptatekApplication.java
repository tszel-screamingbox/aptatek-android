package com.aptatek.aptatek;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.aptatek.aptatek.injection.component.ApplicationComponent;
import com.aptatek.aptatek.injection.component.DaggerApplicationComponent;
import com.aptatek.aptatek.injection.module.ApplicationModule;

import timber.log.Timber;


public class AptatekApplication extends MultiDexApplication {

    private ApplicationComponent applicationComponent;
    private static AptatekApplication application;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }


    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static AptatekApplication get(Context context) {
        return (AptatekApplication) context.getApplicationContext();
    }

    public static AptatekApplication getApplication() {
        return application;
    }
}
