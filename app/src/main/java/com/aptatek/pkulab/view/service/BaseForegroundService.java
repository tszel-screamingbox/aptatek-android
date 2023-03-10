package com.aptatek.pkulab.view.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.injection.component.ApplicationComponent;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseForegroundService extends Service {

    protected CompositeDisposable disposables;

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        disposables = new CompositeDisposable();

        injectService(((AptatekApplication) getApplication()).getApplicationComponent());

        startForeground();
    }

    @Override
    public void onDestroy() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }

        super.onDestroy();
    }

    protected abstract void injectService(final ApplicationComponent component);

    protected abstract void startForeground();
}
