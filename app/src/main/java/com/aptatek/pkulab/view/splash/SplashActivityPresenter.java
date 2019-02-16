package com.aptatek.pkulab.view.splash;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.manager.keystore.KeyStoreManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivityPresenter extends MvpBasePresenter<SplashActivityView> {

    private static final long DELAY_IN_MILLISEC = 1000L;

    private final KeyStoreManager keyStoreManager;
    private final PreferenceManager preferenceManager;
    private final DeviceHelper deviceHelper;

    private CompositeDisposable compositeDisposable;

    @Inject
    public SplashActivityPresenter(final KeyStoreManager keyStoreManager,
                                   final PreferenceManager preferenceManager,
                                   final DeviceHelper deviceHelper) {
        this.keyStoreManager = keyStoreManager;
        this.preferenceManager = preferenceManager;
        this.deviceHelper = deviceHelper;
    }

    @Override
    public void attachView(@NonNull final SplashActivityView view) {
        super.attachView(view);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(Flowable.timer(DELAY_IN_MILLISEC, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> ifViewAttached(attachedView -> {
                    if (deviceHelper.isRooted()) {
                        attachedView.showAlertDialog(R.string.splash_root_alert_title, R.string.splash_root_alert);
                        return;
                    }

                    if (deviceHelper.isStorageLimitReached()) {
                        attachedView.showAlertDialog(R.string.low_device_storage_detected_title, R.string.low_device_storage_detected_description);
                        return;
                    }

                    switchToNextActivity();
                })));
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
