package com.aptatek.aptatek.device;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.aptatek.aptatek.AlarmReceiver;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;
import com.aptatek.aptatek.util.Constants;

import javax.inject.Inject;

public final class AlarmManager {

    private final android.app.AlarmManager systemAlarmManager;
    private final Context context;

    @Inject
    public AlarmManager(@ApplicationContext final Context context) {
        this.systemAlarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    public void setReminder(final long timestamp, final int requestCode) {
        final Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Constants.REMINDER_TIMESTAMP_INTENT_KEY, timestamp);
        intent.putExtra(Constants.REMINDER_REQUEST_CODE_INTENT_KEY, requestCode);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        systemAlarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }

    public void deleteReminder(final int requestCode) {
        final Intent intent = new Intent(context, AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        systemAlarmManager.cancel(pendingIntent);
    }

    public void updateReminder(final long timestamp, final int requestCode) {
        setReminder(timestamp, requestCode);
    }
}
