package com.aptatek.pkuapp.view.settings.reminder.adapter;

import android.support.annotation.NonNull;

public interface ReminderSettingsAdapterCallback {
    void onAddReminderClicked(@NonNull ReminderSettingsAdapterItem item);

    void changeActiveState(@NonNull ReminderSettingsAdapterItem item, boolean isActive);

    void modifyReminderTime(@NonNull ReminderSettingsAdapterItem reminderSettingsItem, @NonNull RemindersAdapterItem reminderItem);
}
