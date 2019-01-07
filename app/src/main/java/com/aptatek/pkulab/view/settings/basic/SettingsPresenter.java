package com.aptatek.pkulab.view.settings.basic;

import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.manager.FingerprintManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

public class SettingsPresenter extends MvpBasePresenter<SettingsView> {

    private final PreferenceManager preferenceManager;
    private final FingerprintManager fingerprintManager;
    private final DeviceHelper deviceHelper;

    @Inject
    public SettingsPresenter(final PreferenceManager preferenceManager,
                             final FingerprintManager fingerprintManager,
                             final DeviceHelper deviceHelper) {
        this.preferenceManager = preferenceManager;
        this.fingerprintManager = fingerprintManager;
        this.deviceHelper = deviceHelper;
    }

    public void checkFingerprintSettings() {
        ifViewAttached(attachedView -> {
            attachedView.showFingerprintAuthEnabled(fingerprintManager.isFingerprintHadrwareDetected());
            attachedView.showFingerprintAuthChecked(preferenceManager.isFingerprintScanEnabled());
        });
    }

    public void setFingerprintEnabled(final boolean enabled) {
        preferenceManager.enableFingerprintScan(enabled);

        ifViewAttached(attachedView -> attachedView.showFingerprintAuthChecked(enabled));
    }

    public void getAppVersion() {
        final String appVersion = deviceHelper.getAppVersion();

        ifViewAttached(attachedView -> attachedView.showAppVersion(appVersion));
    }
}
