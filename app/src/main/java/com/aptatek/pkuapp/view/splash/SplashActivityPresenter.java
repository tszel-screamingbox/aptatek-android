package com.aptatek.pkuapp.view.splash;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.pkuapp.domain.manager.keystore.KeyStoreManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivityPresenter extends MvpBasePresenter<SplashActivityView> {

    private static final int DELAY = 1000;

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
                .subscribe(this::startDelay));
    }

    private void startDelay() {
        compositeDisposable.add(
                Observable.timer(DELAY, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> switchToNextActivity())
        );
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
