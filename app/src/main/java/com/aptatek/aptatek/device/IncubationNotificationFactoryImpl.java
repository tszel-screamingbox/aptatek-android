package com.aptatek.aptatek.device;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationNotificationFactory;
import com.aptatek.aptatek.domain.model.Countdown;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;
import com.aptatek.aptatek.util.Constants;
import com.aptatek.aptatek.view.test.TestActivity;

public class IncubationNotificationFactoryImpl implements IncubationNotificationFactory {

    private static final String INCUBATION_CHANNEL_ID = "aptatek.incubation.channel";

    private final Context context;
    private final ResourceInteractor resourceInteractor;
    private final NotificationManager notificationManager;

    public IncubationNotificationFactoryImpl(@ApplicationContext final Context context,
                                             final ResourceInteractor resourceInteractor,
                                             final NotificationManager notificationManager) {
        this.context = context;
        this.resourceInteractor = resourceInteractor;
        this.notificationManager = notificationManager;
    }

    @NonNull
    @Override
    public Notification createCountdownNotification(@NonNull final Countdown countdown) {
        return new NotificationCompat.Builder(context, createChannelId())
                .setContentTitle(resourceInteractor.getStringResource(R.string.test_incubation_notification_inprogress_title))
                .setContentText(resourceInteractor.getStringResource(R.string.test_incubation_notification_inprogress_textformat, countdown.getRemainingFormattedText()))
                .setSmallIcon(R.drawable.ic_play)
                .setProgress(100, getIncubationProgress(countdown.getRemainingMillis()), false)
                .setVibrate(new long[] {0L})
                .setContentIntent(createContentIntent())
                .build();
    }

    private int getIncubationProgress(final long remainingMillis) {
        return 100 - (int) ((remainingMillis / (float) Constants.DEFAULT_INCUBATION_PERIOD) * 100);
    }

    @NonNull
    @Override
    public Notification createErrorNotification(@NonNull final Throwable throwable) {
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
    public Notification createFinishedNotification() {
        return new NotificationCompat.Builder(context, createChannelId())
                .setContentTitle(resourceInteractor.getStringResource(R.string.test_incubation_notification_finished_title))
                .setContentText(resourceInteractor.getStringResource(R.string.test_incubation_notification_finished_text))
                .setSmallIcon(R.drawable.ic_play)
                .setContentIntent(createContentIntent())
                .setAutoCancel(true)
                .build();
    }

    private PendingIntent createContentIntent() {
        final Intent starter = TestActivity.createStarter(context);
        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(starter);

        return stackBuilder.getPendingIntent(1, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private String createChannelId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    INCUBATION_CHANNEL_ID,
                    resourceInteractor.getStringResource(R.string.test_incubation_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(false);
            notificationChannel.setVibrationPattern(new long[] {0L});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        return INCUBATION_CHANNEL_ID;
    }
}
