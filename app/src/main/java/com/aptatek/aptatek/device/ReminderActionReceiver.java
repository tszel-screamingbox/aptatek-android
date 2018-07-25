package com.aptatek.aptatek.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;

import com.aptatek.aptatek.AptatekApplication;
import com.aptatek.aptatek.domain.interactor.remindersettings.ReminderNotificationFactory;
import com.aptatek.aptatek.injection.component.DaggerBroadcastReceiverComponent;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.ReminderModule;
import com.aptatek.aptatek.util.Constants;
import com.aptatek.aptatek.view.test.TestActivity;

import java.io.Serializable;

import javax.inject.Inject;

public class ReminderActionReceiver extends BroadcastReceiver {

    private static final int QUARTER_HOUR = 15;
    private static final int HALF_HOUR = 30;

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

        final Serializable actionType = intent.getSerializableExtra(Constants.REMINDER_NOTIFICATION_ACTION_TYPE_KEY);
        if (actionType == ReminderActionType.NOW) {
            if (AptatekApplication.get(context).isInForeground()) {
                context.startActivity(TestActivity.createStarter(context));
            } else {
                TaskStackBuilder.create(context)
                        .addNextIntentWithParentStack(TestActivity.createStarter(context))
                        .startActivities();
            }
        } else if (actionType == ReminderActionType.QUARTER_HOUR) {
            alarmManager.scheduleSnooze(QUARTER_HOUR);
        } else if (actionType == ReminderActionType.HALF_HOUR) {
            alarmManager.scheduleSnooze(HALF_HOUR);
        }

        reminderNotificationFactory.cancelNotification(Constants.REMINDER_NOTIFICATION_ID);
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
}
