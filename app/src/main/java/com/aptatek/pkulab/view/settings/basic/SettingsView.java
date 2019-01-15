package com.aptatek.pkulab.view.settings.basic;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface SettingsView extends MvpView {

    void showFingerprintAuthEnabled(final boolean isEnabled);

    void showFingerprintAuthChecked(final boolean isChecked);

    void showAppVersion(final String version);

}
