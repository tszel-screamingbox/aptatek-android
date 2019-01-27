package com.aptatek.pkulab.view.settings.basic;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface SettingsView extends MvpView {

    void updateFingerprintSetting(final boolean isEnabled, final boolean isChecked);

    void showAppVersion(final String version);

}
