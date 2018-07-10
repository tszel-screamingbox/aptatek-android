package com.aptatek.aptatek.device;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.remindersettings.ReminderNotificationFactory;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

public class ReminderNotificationFactoryImpl implements ReminderNotificationFactory {

    private static final String REMINDER_CHANNEL_ID = "aptatek.reminder.channel";

    private final Context context;
    private final ResourceInteractor resourceInteractor;
    private final NotificationManager notificationManager;

    public ReminderNotificationFactoryImpl(@ApplicationContext final Context context,
                                           final ResourceInteractor resourceInteractor,
                                           final NotificationManager notificationManager) {
        this.context = context;
        this.resourceInteractor = resourceInteractor;
        this.notificationManager = notificationManager;
    }

    @NonNull
    @Override
    public Notification createReminderNotification() {
        return new NotificationCompat.Builder(context, createChannelId())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(resourceInteractor.getStringResource(R.string.reminder_notification_title))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();
    }

    private String createChannelId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    REMINDER_CHANNEL_ID,
                    resourceInteractor.getStringResource(R.string.reminder_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(false);
            notificationChannel.setVibrationPattern(new long[] {0L});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        return REMINDER_CHANNEL_ID;
    }
}
