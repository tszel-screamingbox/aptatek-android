package com.aptatek.aptatek.device.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.model.Countdown;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;
import com.aptatek.aptatek.util.Constants;
import com.aptatek.aptatek.view.test.TestActivity;

public class IncubationCountdownNotificationFactory extends BaseCountdownNotificationFactory {

    private Bundle intentExtras;

    public IncubationCountdownNotificationFactory(@ApplicationContext final Context context,
                                                  final ResourceInteractor resourceInteractor,
                                                  final NotificationManager notificationManager) {
        super(context, resourceInteractor, notificationManager);
    }

    @NonNull
    @Override
    public Notification createCountdownNotification(@NonNull final Countdown countdown) {
        return new NotificationCompat.Builder(context, createChannelId())
                .setContentTitle(resourceInteractor.getStringResource(R.string.test_incubation_notification_inprogress_title))
                .setContentText(resourceInteractor.getStringResource(R.string.test_incubation_notification_inprogress_textformat, countdown.getRemainingFormattedText()))
                .setSmallIcon(R.drawable.ic_play)
                .setProgress(Constants.HUNDRED_PERCENT, getIncubationProgress(countdown.getRemainingMillis()), false)
                .setVibrate(new long[] {0L})
                .setSound(null)
                .setDefaults(0)
                .setContentIntent(createContentIntent())
                .build();
    }

    private int getIncubationProgress(final long remainingMillis) {
        return Constants.HUNDRED_PERCENT - (int) ((remainingMillis / (float) Constants.DEFAULT_INCUBATION_PERIOD) * Constants.HUNDRED_PERCENT);
    }

    @Nullable
    @Override
    protected Bundle getIntentExtras() {
        return intentExtras;
    }

    @NonNull
    @Override
    public Notification createCountdownErrorNotification(@NonNull final Throwable throwable) {
        return new NotificationCompat.Builder(context, createChannelId())
                .setContentTitle(resourceInteractor.getStringResource(R.string.test_incubation_notification_finished_title))
                .setContentText(resourceInteractor.getStringResource(R.string.test_incubation_notification_finished_text))
                .setSmallIcon(R.drawable.ic_play)
                .setContentIntent(createContentIntent())
                .setAutoCancel(true)
                .build();
    }

    @NonNull
    @Override
    public Notification createCountdownFinishedNotification() {
        intentExtras = TestActivity.createForIncubationFinishedIntent();

        return new NotificationCompat.Builder(context, createChannelId())
                .setContentTitle(resourceInteractor.getStringResource(R.string.test_incubation_notification_finished_title))
                .setContentText(resourceInteractor.getStringResource(R.string.test_incubation_notification_finished_text))
                .setSmallIcon(R.drawable.ic_play)
                .setContentIntent(createContentIntent())
                .setAutoCancel(true)
                .build();
    }
}
