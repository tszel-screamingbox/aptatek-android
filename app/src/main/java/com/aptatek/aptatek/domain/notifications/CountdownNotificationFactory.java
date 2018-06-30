package com.aptatek.aptatek.domain.notifications;

import android.app.Notification;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.model.Countdown;

public interface CountdownNotificationFactory {

    @NonNull
    Notification createCountdownNotification(@NonNull Countdown countdown);

    @NonNull
    Notification createCountdownErrorNotification(@NonNull Throwable throwable);

    @NonNull
    Notification createCountdownFinishedNotification();

}
