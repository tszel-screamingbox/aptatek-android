package com.aptatek.pkuapp.domain.interactor.wetting;

import android.app.Notification;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.domain.model.Countdown;

public interface WettingNotificationFactory {

    @NonNull
    Notification createCountdownNotification(@NonNull Countdown countdown);

    @NonNull
    Notification createErrorNotification(@NonNull Throwable throwable);

    @NonNull
    Notification createFinishedNotification();

}
