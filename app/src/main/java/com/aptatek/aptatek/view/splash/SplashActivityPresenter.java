package com.aptatek.aptatek.view.splash;

import com.aptatek.aptatek.data.AptatekDatabase;
import com.aptatek.aptatek.domain.manager.keystore.KeyStoreManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class SplashActivityPresenter extends MvpBasePresenter<SplashActivityView> {

    private final KeyStoreManager keyStoreManager;

    @Inject
    SplashActivityPresenter(final KeyStoreManager keyStoreManager,
                            final AptatekDatabase aptatekDatabase) {
        this.keyStoreManager = keyStoreManager;
        //TODO hotfix, find the exact cause of issue
        // TODO add disposable and in switchToNextActivity, check if stream is completed. Don't navigate while it's still doing its work.
        aptatekDatabase.getReminderDayDao()
                .getReminderDays().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    void switchToNextActivity() {
        if (keyStoreManager.aliasExists()) {
            ifViewAttached(SplashActivityView::onRequestPinActivityShouldLoad);
        } else {
            ifViewAttached(SplashActivityView::onSetPinActivityShouldLoad);
        }
    }
}
