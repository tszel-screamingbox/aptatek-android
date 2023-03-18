package com.aptatek.pkulab.device.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.model.Countdown;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.test.TestActivity;

public class WettingNotificationFactoryImpl extends BaseNotificationFactory implements WettingNotificationFactory {

    private static final String COUNTDOWN_CHANNEL_ID = "pkulab.incubation.done";
    private static final String COUNTDOWN_PROGRESS_ID = "pkulab.incubation.progress";

    public WettingNotificationFactoryImpl(@ApplicationContext final Context context,
                                          final ResourceInteractor resourceInteractor,
                                          final NotificationManager notificationManager) {
        super(context, resourceInteractor, notificationManager);
    }

    private PendingIntent createContentIntent(boolean forWettingDone) {
        final Intent starter = forWettingDone ? TestActivity.createStarterForNotificationWithReason(context, "end_of_sample_wetting") : TestActivity.createStarter(context);

        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(starter);

        return stackBuilder.getPendingIntent(1, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private String createAlertChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                notificationManager.deleteNotificationChannel(COUNTDOWN_CHANNEL_ID);
            } catch (SecurityException ex) {
                // ignored
            }
            final NotificationChannel notificationChannel = new NotificationChannel(
                    COUNTDOWN_CHANNEL_ID,
                    resourceInteractor.getStringResource(R.string.notification_channel_incubation_alert),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(
                    resourceInteractor.getUriForRawFile(R.raw.noti_sound),
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build());
            notificationManager.createNotificationChannel(notificationChannel);
        }

        return COUNTDOWN_CHANNEL_ID;
    }

    private String createProgressChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                notificationManager.deleteNotificationChannel(COUNTDOWN_PROGRESS_ID);
            } catch (SecurityException ex) {
                // ignored
            }
            final NotificationChannel notificationChannel = new NotificationChannel(
                    COUNTDOWN_PROGRESS_ID,
                    resourceInteractor.getStringResource(R.string.notification_channel_incubation_countdown),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(false);
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        return COUNTDOWN_PROGRESS_ID;
    }

    @NonNull
    @Override
    public Notification createCountdownNotification(@NonNull final Countdown countdown) {
        return new NotificationCompat.Builder(context, createProgressChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.test_wetting_notification_inprogress_title))
                .setContentText(resourceInteractor.getStringResource(R.string.test_wetting_notification_inprogress_textformat, countdown.getRemainingFormattedText()))
                .setSmallIcon(R.drawable.ic_notification)
                .setProgress(Constants.HUNDRED_PERCENT, getWettingProgress(countdown.getRemainingMillis()), false)
                .setVibrate(new long[]{0L})
                .setSound(null)
                .setOngoing(true)
                .setDefaults(0)
                .setContentIntent(createContentIntent(false))
                .build();
    }

    private int getWettingProgress(final long remainingMillis) {
        return Constants.HUNDRED_PERCENT - (int) ((remainingMillis / (float) Constants.DEFAULT_WETTING_PERIOD) * Constants.HUNDRED_PERCENT);
    }

    @NonNull
    @Override
    public Notification createCountdownErrorNotification(@NonNull final Throwable throwable) {
        return new NotificationCompat.Builder(context, createProgressChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.test_wetting_notification_finished_title))
                .setContentText(resourceInteractor.getStringResource(R.string.test_wetting_notification_finished_text))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(createContentIntent(false))
                .setAutoCancel(true)
                .build();
    }

    @NonNull
    @Override
    public Notification createCountdownFinishedNotification() {
        return new NotificationCompat.Builder(context, createAlertChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.test_wetting_notification_finished_title))
                .setContentText(resourceInteractor.getStringResource(R.string.test_wetting_notification_finished_text))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(createContentIntent(true))
                .setSound(resourceInteractor.getUriForRawFile(R.raw.noti_sound))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(0)
                .build();
    }

}
