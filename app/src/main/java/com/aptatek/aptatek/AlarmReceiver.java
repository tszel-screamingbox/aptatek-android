package com.aptatek.aptatek;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.aptatek.device.AlarmManager;
import com.aptatek.aptatek.injection.component.DaggerBroadcastReceiverComponent;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.util.Constants;

import java.util.Calendar;

import javax.inject.Inject;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 999;

    @Inject
    AlarmManager alarmManager;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DaggerBroadcastReceiverComponent.builder()
                .applicationModule(new ApplicationModule((AptatekApplication) context.getApplicationContext()))
                .build()
                .inject(this);

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // TODO would-be-nice: domain interface NotificationBuilder which takes a domain model (NotificationModel) and creates the notification. Should be implemented in device layer
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.reminder_notification_title))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(intent.getLongExtra(Constants.REMINDER_TIMESTAMP_INTENT_KEY, 0));
        calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + 1);

        alarmManager.setReminder(calendar.getTimeInMillis(), intent.getIntExtra(Constants.REMINDER_REQUEST_CODE_INTENT_KEY, 0));
    }
}
