package com.aptatek.aptatek.view.settings.reminder;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.ArrayList;

public interface ReminderSettingsView extends MvpView {
    void presentDays(@NonNull ArrayList<ReminderSettingsAdapterItem> data);

    void addReminder(@NonNull ReminderSettingsAdapterItem item);

    void changeActiveState(@NonNull ReminderSettingsAdapterItem item);

    void deleteReminder(@NonNull ReminderSettingsAdapterItem item);

    void modifyReminder(@NonNull ReminderSettingsAdapterItem item);

    void alreadyHasReminderError();
}
