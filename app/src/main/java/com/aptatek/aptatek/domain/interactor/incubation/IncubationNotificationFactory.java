package com.aptatek.aptatek.domain.interactor.incubation;

import android.app.Notification;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.model.Countdown;

public interface IncubationNotificationFactory {

    @NonNull
    Notification createCountdownNotification(@NonNull Countdown countdown);

    @NonNull
    Notification createErrorNotification(@NonNull Throwable throwable);

    @NonNull
    Notification createFinishedNotification();

}
