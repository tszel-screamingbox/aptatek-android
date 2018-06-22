package com.aptatek.aptatek.view.splash;

import com.aptatek.aptatek.data.AptatekDatabase;
import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.manager.keystore.KeyStoreManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class SplashActivityPresenter extends MvpBasePresenter<SplashActivityView> {

    private final KeyStoreManager keyStoreManager;
    private final PreferenceManager preferenceManager;

    @Inject
    SplashActivityPresenter(final KeyStoreManager keyStoreManager,
                            final AptatekDatabase aptatekDatabase,
                            final PreferenceManager preferenceManager) {
        this.keyStoreManager = keyStoreManager;
        this.preferenceManager = preferenceManager;
        //TODO hotfix, find the exact cause of issue
        // TODO add disposable and in switchToNextActivity, check if stream is completed. Don't navigate while it's still doing its work.
        aptatekDatabase.getReminderDayDao()
                .getReminderDays().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    void switchToNextActivity() {
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
}
