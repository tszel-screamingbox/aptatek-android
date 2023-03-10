package com.aptatek.pkulab.view.settings.reminder;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.view.settings.reminder.adapter.ReminderSettingsAdapterItem;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.ArrayList;
import java.util.List;

public interface ReminderSettingsView extends MvpView {
    void presentDays(@NonNull ArrayList<ReminderSettingsAdapterItem> data);

    void addReminder(@NonNull List<ReminderSettingsAdapterItem> data);

    void changeActiveState(@NonNull List<ReminderSettingsAdapterItem> data);

    void deleteReminder(@NonNull List<ReminderSettingsAdapterItem> data);

    void modifyReminder(@NonNull List<ReminderSettingsAdapterItem> data);

    void showTimePickerDialog(@NonNull ReminderSettingsAdapterItem item);

    void showAlreadyHasReminderError();
}
