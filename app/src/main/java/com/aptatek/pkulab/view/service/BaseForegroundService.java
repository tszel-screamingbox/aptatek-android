package com.aptatek.pkulab.view.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.injection.component.ApplicationComponent;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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

        disposables.add(
            shouldStart()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(running -> {

                    if (running) {
                        startForeground();
                    } else {
                        stopForeground(true);
                        stopSelf();
                    }
                })
        );

    }

    @Override
    public void onDestroy() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }

        super.onDestroy();
    }

    protected abstract void injectService(final ApplicationComponent component);

    protected abstract Single<Boolean> shouldStart();

    protected abstract void startForeground();
}
