package com.aptatek.pkulab.domain.interactor.remindersettings;

import android.app.Notification;

import androidx.annotation.NonNull;

public interface ReminderNotificationFactory {

    @NonNull
    Notification createReminderNotification();

    void cancelNotification(int notificationId);
}
