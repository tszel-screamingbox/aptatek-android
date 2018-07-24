package com.aptatek.aptatek.domain.interactor.remindersettings;

import android.app.Notification;
import android.support.annotation.NonNull;

public interface ReminderNotificationFactory {

    @NonNull
    Notification createReminderNotification();

    void cancelNotification(int notificationId);
}
