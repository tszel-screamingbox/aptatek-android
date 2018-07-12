package com.aptatek.aptatek.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aptatek.aptatek.AptatekApplication;
import com.aptatek.aptatek.injection.component.DaggerBroadcastReceiverComponent;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.util.Constants;
import com.aptatek.aptatek.view.test.TestActivity;

import java.io.Serializable;
import java.util.Calendar;

import javax.inject.Inject;

public class ReminderActionReceiver extends BroadcastReceiver {

    private static final int REMINDER_SNOOZE_REQUEST_KEY = 75;
    private static final int QUARTER_HOUR = 15;
    private static final int HALF_HOUR = 30;

    @Inject
    AlarmManager alarmManager;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DaggerBroadcastReceiverComponent.builder()
                .applicationModule(new ApplicationModule((AptatekApplication) context.getApplicationContext()))
                .build()
                .inject(this);

        final Serializable actionType = intent.getSerializableExtra(Constants.REMINDER_NOTIFICATION_ACTION_TYPE_KEY);
        if (actionType == ReminderActionType.NOW) {
            TestActivity.createStarter(context);
        } else if (actionType == ReminderActionType.QUARTER_HOUR) {
            scheduleReminder(QUARTER_HOUR);
        } else if (actionType == ReminderActionType.HALF_HOUR) {
            scheduleReminder(HALF_HOUR);
        }

        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private void scheduleReminder(final int time) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + time);
        alarmManager.setReminder(calendar.getTimeInMillis(), REMINDER_SNOOZE_REQUEST_KEY, false);
    }
}
