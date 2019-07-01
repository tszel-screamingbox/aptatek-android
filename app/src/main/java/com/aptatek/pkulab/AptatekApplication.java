package com.aptatek.pkulab;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.multidex.MultiDexApplication;

import com.amplitude.api.Amplitude;
import com.aptatek.pkulab.device.AlarmManager;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.interactor.wetting.WettingDataSource;
import com.aptatek.pkulab.domain.interactor.wetting.WettingStatus;
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
    @Inject
    WettingDataSource wettingDataSource;

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

        if (!BuildConfig.DEBUG) {
            Amplitude.getInstance()
                    .initialize(this, BuildConfig.AMPLITUDE_KEY)
                    .enableForegroundTracking(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        inForeground = true;
        Timber.d("Process.Lifecycle: foreground");
        stopService(new Intent(this, WettingForegroundService.class));

        if (shouldRequestPin()) {
            Timber.d("Requesting pin code due to exceeding max idle period");

            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.PIN_IDLE_ACTION));
        }

        stopService(ExplicitBluetoothService.createForDeviceReady(this));
        stopService(ExplicitBluetoothService.createForTestComplete(this));

        lastForegroundTime = 0L;

        disposeKillServiceTimer();
    }

    public boolean shouldRequestPin() {
        return lastForegroundTime > 0L && Math.abs(System.currentTimeMillis() - lastForegroundTime) > Constants.PIN_IDLE_PERIOD_MS;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        inForeground = false;
        lastForegroundTime = System.currentTimeMillis();
        Timber.d("Process.Lifecycle: background");

        startWettingServiceWhenPossible();
        startExplicitBTServiceWhenPossible();

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

    private void startExplicitBTServiceWhenPossible() {
        if (BluetoothService.isServiceRunning() || BuildConfig.FLAVOR.equals("mock")) {
            return;
        }

        try {
            final TestScreens testStatus = preferenceManager.getTestStatus();
            if (testStatus == TestScreens.TESTING) {
                ContextCompat.startForegroundService(this, ExplicitBluetoothService.createForTestComplete(this));
            } else if (testStatus == TestScreens.TURN_READER_ON) {
                ContextCompat.startForegroundService(this, ExplicitBluetoothService.createForDeviceReady(this));
            }
        } catch (Exception e) {
            // ignore
        }
    }

    private void startWettingServiceWhenPossible() {
        final WettingStatus wettingStatus = wettingDataSource.getWettingStatus();
        if (wettingStatus == WettingStatus.RUNNING) {
            ContextCompat.startForegroundService(this, new Intent(this, WettingForegroundService.class));
        }
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
