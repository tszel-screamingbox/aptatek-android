package com.aptatek.pkulab.view.splash;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.aptatek.pkulab.BuildConfig;
import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.appstart.OpenFromBTNotification;
import com.aptatek.pkulab.domain.manager.keystore.KeyStoreManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivityPresenter extends MvpBasePresenter<SplashActivityView> {

    private static final long DELAY_IN_MILLISEC = 1000L;

    private final KeyStoreManager keyStoreManager;
    private final PreferenceManager preferenceManager;
    private final DeviceHelper deviceHelper;
    private final File dbFile;
    private final IAnalyticsManager analyticsManager;

    private CompositeDisposable compositeDisposable;

    @Inject
    public SplashActivityPresenter(final KeyStoreManager keyStoreManager,
                                   final PreferenceManager preferenceManager,
                                   final DeviceHelper deviceHelper,
                                   final @Named("databaseFile") File dbFile,
                                   final IAnalyticsManager analyticsManager) {
        this.keyStoreManager = keyStoreManager;
        this.preferenceManager = preferenceManager;
        this.deviceHelper = deviceHelper;
        this.dbFile = dbFile;
        this.analyticsManager = analyticsManager;
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

                    if (!preferenceManager.isDbEncryptedWithPin() || !preferenceManager.isDbCypherUpdated()) {
                        // delete database first
                        dbFile.delete();
                        preferenceManager.setPrefDbEncryptedWithPin();
                        preferenceManager.setPrefDbCypherUpdated();
                    }

                    switchToNextActivity();
                })));
    }

    public void switchToNextActivity() {
        ifViewAttached(attachedView -> {
            if (!preferenceManager.isParentalPassed()) {
                attachedView.onParentalGateShouldLoad();
            } else if (!keyStoreManager.aliasExists()) {
                attachedView.onSetPinActivityShouldLoad();
            } else if (BuildConfig.FLAVOR.equals("prod") && (TextUtils.isEmpty(preferenceManager.getPairedDevice()) || !dbFile.exists())) {
                attachedView.onConnectReaderShouldLoad();
            } else {
                attachedView.onRequestPinActivityShouldLoad();
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

    public void logBtError() {
        analyticsManager.logEvent(new OpenFromBTNotification("error"));
    }
}
