package com.aptatek.pkulab.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.TaskStackBuilder;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.domain.interactor.remindersettings.ReminderNotificationFactory;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.settings.Reminder;
import com.aptatek.pkulab.domain.manager.analytic.events.test.TestFromReminder;
import com.aptatek.pkulab.injection.component.DaggerBroadcastReceiverComponent;
import com.aptatek.pkulab.injection.module.ReminderModule;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.test.TestActivity;

import java.io.Serializable;

import javax.inject.Inject;

public class ReminderActionReceiver extends BroadcastReceiver {

    private static final int QUARTER_HOUR = 15;
    private static final int HALF_HOUR = 30;

    @Inject
    AlarmManager alarmManager;

    @Inject
    PreferenceManager preferenceManager;

    @Inject
    ReminderNotificationFactory reminderNotificationFactory;

    @Inject
    IAnalyticsManager analyticsManager;

    @Inject
    DeviceHelper deviceHelper;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DaggerBroadcastReceiverComponent.builder()
                .applicationComponent(((AptatekApplication) context.getApplicationContext()).getApplicationComponent())
                .reminderModule(new ReminderModule())
                .build()
                .inject(this);

        final Serializable actionType = intent.getSerializableExtra(Constants.REMINDER_NOTIFICATION_ACTION_TYPE_KEY);
        if (actionType == ReminderActionType.NOW) {
            preferenceManager.clearPreference(PreferenceManager.PREF_TEST_STATUS);
            analyticsManager.logEvent(new TestFromReminder(deviceHelper.getPhoneBattery()));

            if (AptatekApplication.get(context).isInForeground()) {
                context.startActivity(TestActivity.createStarter(context));
                preferenceManager.setTestFlowStart();
            } else {
                TaskStackBuilder.create(context)
                        .addNextIntentWithParentStack(TestActivity.createStarter(context))
                        .startActivities();
            }
        } else if (actionType == ReminderActionType.QUARTER_HOUR) {
            analyticsManager.logEvent(new Reminder(QUARTER_HOUR));
            alarmManager.scheduleSnooze(QUARTER_HOUR);
        } else if (actionType == ReminderActionType.HALF_HOUR) {
            analyticsManager.logEvent(new Reminder(HALF_HOUR));
            alarmManager.scheduleSnooze(HALF_HOUR);
        }

        reminderNotificationFactory.cancelNotification(Constants.REMINDER_NOTIFICATION_ID);
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
}
