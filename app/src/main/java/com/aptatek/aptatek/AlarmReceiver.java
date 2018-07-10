package com.aptatek.aptatek;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.aptatek.device.AlarmManager;
import com.aptatek.aptatek.domain.interactor.remindersettings.ReminderNotificationFactory;
import com.aptatek.aptatek.injection.component.DaggerBroadcastReceiverComponent;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.ReminderModule;
import com.aptatek.aptatek.util.Constants;

import java.util.Calendar;

import javax.inject.Inject;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 999;

    @Inject
    AlarmManager alarmManager;

    @Inject
    ReminderNotificationFactory reminderNotificationFactory;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DaggerBroadcastReceiverComponent.builder()
                .applicationModule(new ApplicationModule((AptatekApplication) context.getApplicationContext()))
                .reminderModule(new ReminderModule())
                .build()
                .inject(this);

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(NOTIFICATION_ID, reminderNotificationFactory.createReminderNotification());

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(intent.getLongExtra(Constants.REMINDER_TIMESTAMP_INTENT_KEY, 0));
        calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + 1);

        alarmManager.setReminder(calendar.getTimeInMillis(), intent.getIntExtra(Constants.REMINDER_REQUEST_CODE_INTENT_KEY, 0));
    }
}
