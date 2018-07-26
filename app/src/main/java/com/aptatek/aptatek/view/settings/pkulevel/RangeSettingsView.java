package com.aptatek.aptatek.view.settings.pkulevel;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface RangeSettingsView extends MvpView {

    void displayRangeSettings(@NonNull RangeSettingsModel model);

    void showSaveChangesDialog();

    void finish();

}
