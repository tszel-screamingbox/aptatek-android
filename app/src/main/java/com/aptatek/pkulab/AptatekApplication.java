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
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.injection.component.ApplicationComponent;
import com.aptatek.pkulab.injection.component.DaggerApplicationComponent;
import com.aptatek.pkulab.injection.module.ApplicationModule;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.service.BluetoothService;
import com.aptatek.pkulab.view.service.ExplicitBluetoothService;
import com.aptatek.pkulab.view.service.WettingForegroundService;
import com.aptatek.pkulab.view.test.TestScreens;
import com.crashlytics.android.Crashlytics;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;


public class AptatekApplication extends MultiDexApplication implements LifecycleObserver {

    private ApplicationComponent applicationComponent;
    private boolean inForeground;
    private long lastForegroundTime;

    @Inject
    AlarmManager alarmManager;
    @Inject
    Crashlytics crashlytics;
    @Inject
    Timber.Tree timber;
    @Inject
    PreferenceManager preferenceManager;

    private Disposable killServiceTimer = null;

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

        Fabric.with(this, crashlytics);
        Timber.plant(timber);

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

        stopService(ExplicitBluetoothService.createForDeviceReady(this));
        stopService(ExplicitBluetoothService.createForTestComplete(this));

        lastForegroundTime = 0L;

        disposeKillServiceTimer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        inForeground = false;
        lastForegroundTime = System.currentTimeMillis();
        Timber.d("Process.Lifecycle: background");
        startService(new Intent(this, WettingForegroundService.class));

        try {
            final TestScreens testStatus = preferenceManager.getTestStatus();
            if (testStatus == TestScreens.TESTING) {
                startService(ExplicitBluetoothService.createForTestComplete(this));
            } else if (testStatus == TestScreens.TURN_READER_ON) {
                startService(ExplicitBluetoothService.createForDeviceReady(this));
            }
        } catch (Exception e) {
            // ignore
        }

        disposeKillServiceTimer();
        killServiceTimer = Countdown.countdown(Constants.BT_SERVICE_IDLE_TIMEOUT, ignore -> true, ignore -> ignore)
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ignore -> {
                            Timber.d("Shutting down BluetoothService due to inactivity");
                            stopService(new Intent(this, BluetoothService.class));
                        },
                        Timber::e
                );

    }

    private void disposeKillServiceTimer() {
        if (killServiceTimer != null && !killServiceTimer.isDisposed()) {
            killServiceTimer.dispose();
            killServiceTimer = null;
        }
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
