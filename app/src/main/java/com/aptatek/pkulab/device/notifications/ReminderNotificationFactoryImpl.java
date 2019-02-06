package com.aptatek.pkulab.device.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.ReminderActionReceiver;
import com.aptatek.pkulab.device.ReminderActionType;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.remindersettings.ReminderNotificationFactory;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.aptatek.pkulab.util.Constants;

public class ReminderNotificationFactoryImpl extends BaseNotificationFactory implements ReminderNotificationFactory {

    private static final int REMINDER_ACTION_NOW_REQUEST_CODE = 9950;
    private static final int REMINDER_ACTION_QUARTER_HOUR_CODE = 9951;
    private static final int REMINDER_ACTION_HALF_HOUR_REQUEST_CODE = 9952;

    public ReminderNotificationFactoryImpl(@ApplicationContext final Context context,
                                           final ResourceInteractor resourceInteractor,
                                           final NotificationManager notificationManager) {
        super(context, resourceInteractor, notificationManager);
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

        return new NotificationCompat.Builder(context, createChannel())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(resourceInteractor.getStringResource(R.string.reminder_notification_title))
                .setContentText(resourceInteractor.getStringResource(R.string.reminder_notification_message))
                .setColor(resourceInteractor.getColorResource(R.color.applicationPink))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(0)
                .setSound(resourceInteractor.getUriForRawFile(R.raw.noti_sound))
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
}
