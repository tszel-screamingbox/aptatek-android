package com.aptatek.pkulab.domain.notifications;

import android.app.Notification;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.model.Countdown;

public interface CountdownNotificationFactory {

    @NonNull
    Notification createCountdownNotification(@NonNull Countdown countdown);

    @NonNull
    Notification createCountdownErrorNotification(@NonNull Throwable throwable);

    @NonNull
    Notification createCountdownFinishedNotification();

}
