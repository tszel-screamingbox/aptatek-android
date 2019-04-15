package com.aptatek.pkulab.view.settings.basic;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

public interface SettingsView extends MvpView {

    void updateFingerprintSetting(final boolean isEnabled, final boolean isChecked);

    void showAppVersion(final String version);

    void populateAdapter(final List<SettingsAdapterItem> data);
}
