package com.aptatek.pkulab;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatDelegate;

import com.aptatek.pkulab.device.AlarmManager;
import com.aptatek.pkulab.injection.component.ApplicationComponent;
import com.aptatek.pkulab.injection.component.DaggerApplicationComponent;
import com.aptatek.pkulab.injection.module.ApplicationModule;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.service.BluetoothService;
import com.aptatek.pkulab.view.service.WettingForegroundService;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Inject;

import timber.log.Timber;


public class AptatekApplication extends MultiDexApplication implements LifecycleObserver {

    private ApplicationComponent applicationComponent;
    private boolean inForeground;
    private long lastForegroundTime;

    @Inject
    AlarmManager alarmManager;

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

        JodaTimeAndroid.init(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        inForeground = true;
        Timber.d("Process.Lifecycle: foreground");
        stopService(new Intent(this, WettingForegroundService.class));

        if (lastForegroundTime > 0L && Math.abs(System.currentTimeMillis() - lastForegroundTime) > Constants.PIN_IDLE_PERIOD_MS) {
            Timber.d("Requesting pin code due to exceeding max idle period");

            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.PIN_IDLE_ACTION));
        }

        lastForegroundTime = 0L;

        startService(new Intent(this, BluetoothService.class));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        inForeground = false;
        lastForegroundTime = System.currentTimeMillis();
        Timber.d("Process.Lifecycle: background");
        startService(new Intent(this, WettingForegroundService.class));
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public boolean isInForeground() {
        return inForeground;
    }

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }

    public static AptatekApplication get(final Context context) {
        return (AptatekApplication) context.getApplicationContext();
    }
}
