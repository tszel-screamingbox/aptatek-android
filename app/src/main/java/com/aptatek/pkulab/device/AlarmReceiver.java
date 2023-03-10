package com.aptatek.pkulab.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.domain.interactor.remindersettings.ReminderNotificationFactory;
import com.aptatek.pkulab.injection.component.DaggerBroadcastReceiverComponent;
import com.aptatek.pkulab.injection.module.ReminderModule;
import com.aptatek.pkulab.util.Constants;

import java.util.Calendar;

import javax.inject.Inject;


public class AlarmReceiver extends BroadcastReceiver {

    @Inject
    AlarmManager alarmManager;

    @Inject
    ReminderNotificationFactory reminderNotificationFactory;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DaggerBroadcastReceiverComponent.builder()
                .applicationComponent(((AptatekApplication) context.getApplicationContext()).getApplicationComponent())
                .reminderModule(new ReminderModule())
                .build()
                .inject(this);

        if (AptatekApplication.get(context).isInForeground()) {
            LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(new Intent(Constants.REMINDER_DIALOG_BROADCAST_NAME));
        } else {
            showNotification(context, intent);
        }
    }

    public void showNotification(final Context context, final Intent intent) {
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(Constants.REMINDER_NOTIFICATION_ID, reminderNotificationFactory.createReminderNotification());

        if (intent.getBooleanExtra(Constants.REMINDER_RE_SCHEDULE_INTENT_KEY, false)) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(intent.getLongExtra(Constants.REMINDER_TIMESTAMP_INTENT_KEY, 0));
            calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + 1);

            alarmManager.setReminder(calendar.getTimeInMillis(),
                    intent.getIntExtra(Constants.REMINDER_REQUEST_CODE_INTENT_KEY, 0), true);
        }
    }
}
