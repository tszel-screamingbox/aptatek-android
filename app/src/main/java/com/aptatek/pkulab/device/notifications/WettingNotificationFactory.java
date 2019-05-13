package com.aptatek.pkulab.device.notifications;

import android.app.Notification;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.domain.model.Countdown;

public interface WettingNotificationFactory {

    @NonNull
    Notification createCountdownNotification(@NonNull Countdown countdown);

    @NonNull
    Notification createCountdownErrorNotification(@NonNull Throwable throwable);

    @NonNull
    Notification createCountdownFinishedNotification();

}
