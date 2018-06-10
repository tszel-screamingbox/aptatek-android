package com.aptatek.aptatek;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.aptatek.aptatek.injection.component.ApplicationComponent;
import com.aptatek.aptatek.injection.component.DaggerApplicationComponent;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.view.test.incubation.IncubationReminderService;

import timber.log.Timber;


public class AptatekApplication extends MultiDexApplication implements LifecycleObserver {

    private ApplicationComponent applicationComponent;
    private boolean inForeground;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        inForeground = true;
        Timber.d("Process.Lifecycle: foreground");
        stopService(new Intent(this, IncubationReminderService.class));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        inForeground = false;
        Timber.d("Process.Lifecycle: background");
        startService(new Intent(this, IncubationReminderService.class));
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public boolean isInForeground() {
        return inForeground;
    }

    public static AptatekApplication get(final Context context) {
        return (AptatekApplication) context.getApplicationContext();
    }

}
