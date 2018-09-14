package com.aptatek.pkuapp.device.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.model.Countdown;
import com.aptatek.pkuapp.injection.qualifier.ApplicationContext;
import com.aptatek.pkuapp.util.Constants;
import com.aptatek.pkuapp.view.test.TestActivity;

public class WettingCountdownNotificationFactory extends BaseCountdownNotificationFactory {

    private Bundle extrasBundle;

    public WettingCountdownNotificationFactory(@ApplicationContext final Context context,
                                               final ResourceInteractor resourceInteractor,
                                               final NotificationManager notificationManager) {
        super(context, resourceInteractor, notificationManager);
    }

    @NonNull
    @Override
    public Notification createCountdownNotification(@NonNull final Countdown countdown) {
        return new NotificationCompat.Builder(context, createChannelId())
                .setContentTitle(resourceInteractor.getStringResource(R.string.test_wetting_notification_inprogress_title))
                .setContentText(resourceInteractor.getStringResource(R.string.test_wetting_notification_inprogress_textformat, countdown.getRemainingFormattedText()))
                .setSmallIcon(R.drawable.ic_play)
                .setProgress(Constants.HUNDRED_PERCENT, getWettingProgress(countdown.getRemainingMillis()), false)
                .setVibrate(new long[]{0L})
                .setSound(null)
                .setDefaults(0)
                .setContentIntent(createContentIntent())
                .build();
    }

    private int getWettingProgress(final long remainingMillis) {
        return Constants.HUNDRED_PERCENT - (int) ((remainingMillis / (float) Constants.DEFAULT_WETTING_PERIOD) * Constants.HUNDRED_PERCENT);
    }

    @Nullable
    @Override
    protected Bundle getIntentExtras() {
        return extrasBundle;
    }

    @NonNull
    @Override
    public Notification createCountdownErrorNotification(@NonNull final Throwable throwable) {
        return new NotificationCompat.Builder(context, createChannelId())
                .setContentTitle(resourceInteractor.getStringResource(R.string.test_wetting_notification_finished_title))
                .setContentText(resourceInteractor.getStringResource(R.string.test_wetting_notification_finished_text))
                .setSmallIcon(R.drawable.ic_play)
                .setContentIntent(createContentIntent())
                .setAutoCancel(true)
                .build();
    }

    @NonNull
    @Override
    public Notification createCountdownFinishedNotification() {
        extrasBundle = TestActivity.createForSampleWettingFinishedIntent();

        return new NotificationCompat.Builder(context, createChannelId())
                .setContentTitle(resourceInteractor.getStringResource(R.string.test_wetting_notification_finished_title))
                .setContentText(resourceInteractor.getStringResource(R.string.test_wetting_notification_finished_text))
                .setSmallIcon(R.drawable.ic_play)
                .setContentIntent(createContentIntent())
                .setAutoCancel(true)
                .build();
    }

}
