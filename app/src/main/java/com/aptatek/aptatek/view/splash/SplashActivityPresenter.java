package com.aptatek.aptatek.view.splash;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.data.AptatekDatabase;
import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.aptatek.domain.manager.keystore.KeyStoreManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivityPresenter extends MvpBasePresenter<SplashActivityView> {

    private final KeyStoreManager keyStoreManager;
    private final PreferenceManager preferenceManager;
    private final ReminderInteractor reminderInteractor;

    private CompositeDisposable compositeDisposable;

    @Inject
    public SplashActivityPresenter(final KeyStoreManager keyStoreManager,
                                   final ReminderInteractor reminderInteractor,
                                   final PreferenceManager preferenceManager) {
        this.keyStoreManager = keyStoreManager;
        this.preferenceManager = preferenceManager;
        this.reminderInteractor = reminderInteractor;
    }

    @Override
    public void attachView(@NonNull final SplashActivityView view) {
        super.attachView(view);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(reminderInteractor.initializeDays()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::switchToNextActivity));
    }

    public void switchToNextActivity() {
        ifViewAttached(attachedView -> {
            if (!preferenceManager.isParentalPassed()) {
                attachedView.onParentalGateShouldLoad();
            } else if (keyStoreManager.aliasExists()) {
                attachedView.onRequestPinActivityShouldLoad();
            } else {
                attachedView.onSetPinActivityShouldLoad();
            }
        });
    }

    @Override
    public void detachView() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.detachView();
    }
}
