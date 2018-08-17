package com.aptatek.pkuapp.device;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.remindersettings.ReminderNotificationFactory;
import com.aptatek.pkuapp.injection.qualifier.ApplicationContext;
import com.aptatek.pkuapp.util.Constants;

public class ReminderNotificationFactoryImpl implements ReminderNotificationFactory {

    private static final String REMINDER_CHANNEL_ID = "aptatek.reminder.channel";
    private static final int REMINDER_ACTION_NOW_REQUEST_CODE = 9950;
    private static final int REMINDER_ACTION_QUARTER_HOUR_CODE = 9951;
    private static final int REMINDER_ACTION_HALF_HOUR_REQUEST_CODE = 9952;

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
        final Intent nowIntent = new Intent(context, ReminderActionReceiver.class);
        final Intent quarterHourIntent = new Intent(context, ReminderActionReceiver.class);
        final Intent halfHourIntent = new Intent(context, ReminderActionReceiver.class);

        nowIntent.putExtra(Constants.REMINDER_NOTIFICATION_ACTION_TYPE_KEY, ReminderActionType.NOW);
        quarterHourIntent.putExtra(Constants.REMINDER_NOTIFICATION_ACTION_TYPE_KEY, ReminderActionType.QUARTER_HOUR);
        halfHourIntent.putExtra(Constants.REMINDER_NOTIFICATION_ACTION_TYPE_KEY, ReminderActionType.HALF_HOUR);

        return new NotificationCompat.Builder(context, createChannelId())
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(resourceInteractor.getStringResource(R.string.reminder_notification_title))
                .setContentText(resourceInteractor.getStringResource(R.string.reminder_notification_message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setColor(resourceInteractor.getColorResource(R.color.applicationPink))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(0)
                .setAutoCancel(true)
                .addAction(0,
                        resourceInteractor.getStringResource(R.string.reminder_notification_now),
                        PendingIntent.getBroadcast(context, REMINDER_ACTION_NOW_REQUEST_CODE, nowIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .addAction(0,
                        resourceInteractor.getStringResource(R.string.reminder_notification_quarter_hour),
                        PendingIntent.getBroadcast(context, REMINDER_ACTION_QUARTER_HOUR_CODE, quarterHourIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .addAction(0,
                        resourceInteractor.getStringResource(R.string.reminder_notification_half_hour),
                        PendingIntent.getBroadcast(context, REMINDER_ACTION_HALF_HOUR_REQUEST_CODE, halfHourIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
    }

    @Override
    public void cancelNotification(final int notificationId) {
        notificationManager.cancel(notificationId);
    }

    private String createChannelId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    REMINDER_CHANNEL_ID,
                    resourceInteractor.getStringResource(R.string.reminder_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(false);
            notificationChannel.setVibrationPattern(new long[]{0L});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        return REMINDER_CHANNEL_ID;
    }
}
