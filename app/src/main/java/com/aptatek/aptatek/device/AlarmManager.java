package com.aptatek.aptatek.device;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.aptatek.aptatek.AlarmReceiver;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

import javax.inject.Inject;

import timber.log.Timber;

public class AlarmManager {

    private android.app.AlarmManager alarmManager;
    private Context context;

    @Inject
    public AlarmManager(@ApplicationContext Context context) {
        this.alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    public void setReminder(Long timestamp, int requestCode) {
        Timber.d("reminder timestamp : " + timestamp);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }

    public void deleteReminder(int requestCode) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public void updateReminder(Long timestamp, int requestCode) {
        setReminder(timestamp, requestCode);
    }
}
