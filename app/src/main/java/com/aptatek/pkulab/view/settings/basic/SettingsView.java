package com.aptatek.pkulab.view.settings.basic;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface SettingsView extends MvpView {

    void showFingerprintAuthEnabled(boolean isEnabled);

    void showFingerprintAuthChecked(boolean isChecked);

}
