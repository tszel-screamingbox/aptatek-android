package com.aptatek.pkuapp.view.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.aptatek.pkuapp.AptatekApplication;
import com.aptatek.pkuapp.injection.component.test.DaggerTestServiceComponent;
import com.aptatek.pkuapp.injection.component.test.TestServiceComponent;
import com.aptatek.pkuapp.injection.module.ServiceModule;
import com.aptatek.pkuapp.injection.module.test.TestModule;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseReminderService extends Service {

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

        final TestServiceComponent serviceComponent = DaggerTestServiceComponent.builder()
                .applicationComponent(((AptatekApplication) getApplication()).getApplicationComponent())
                .testModule(new TestModule())
                .serviceModule(new ServiceModule(this))
                .build();
        injectService(serviceComponent);

        disposables.add(
            shouldStart()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(running -> {

                    if (running) {
                        startForeground();
                    } else {
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

    protected abstract void injectService(TestServiceComponent component);

    protected abstract Single<Boolean> shouldStart();

    protected abstract void startForeground();
}
