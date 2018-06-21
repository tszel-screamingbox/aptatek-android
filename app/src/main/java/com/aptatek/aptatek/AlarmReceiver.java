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

    @Inject
    AlarmManager alarmManager;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DaggerBroadcastReceiverComponent.builder().applicationModule(new ApplicationModule(((AptatekApplication) context.getApplicationContext()))).build().inject(this);

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.reminder_notification_title))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(999, mBuilder.build());

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(intent.getLongExtra(Constants.REMINDER_TIMESTAMP_INTENT_KEY, 0));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);

        alarmManager.setReminder(calendar.getTimeInMillis(), intent.getIntExtra(Constants.REMINDER_REQUEST_CODE_INTENT_KEY, 0));
    }
}
