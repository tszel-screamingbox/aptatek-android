package com.aptatek.pkulab.view.settings.pkulevel;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface RangeSettingsView extends MvpView {

    void displayRangeSettings(@NonNull RangeSettingsModel model);

    void showSaveChangesDialog();

    void finish();

    void showSettingsUpdateMessage();

}
