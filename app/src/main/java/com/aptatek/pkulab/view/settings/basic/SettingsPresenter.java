package com.aptatek.pkulab.view.settings.basic;

import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.manager.FingerprintManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import ix.Ix;

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

    @Override
    public void attachView(final SettingsView view) {
        super.attachView(view);

        final List<SettingsAdapterItem> data = Ix.from(Arrays.asList(SettingsItem.values()))
                .filter(settingsItem -> {
                    if(settingsItem != SettingsItem.FINGERPRINT_AUTH){
                        return true;
                    }

                    return fingerprintManager.isFingerprintHadrwareDetected();
                })
                .map(settingsItem -> new SettingsAdapterItem(settingsItem, false, false))
                .toList();

        view.populateAdapter(data);
    }

    public void checkFingerprintSettings() {
        ifViewAttached(attachedView ->
                attachedView.updateFingerprintSetting(fingerprintManager.isFingerprintHadrwareDetected(), preferenceManager.isFingerprintScanEnabled())
        );
    }

    public void setFingerprintEnabled(final boolean enabled) {
        if (fingerprintManager.isFingerprintHadrwareDetected()) {
            preferenceManager.enableFingerprintScan(enabled);
            ifViewAttached(attachedView -> attachedView.updateFingerprintSetting(true, enabled));
        }
    }

    public void getAppVersion() {
        final String appVersion = deviceHelper.getAppVersion();

        ifViewAttached(attachedView -> attachedView.showAppVersion(appVersion));
    }
}
