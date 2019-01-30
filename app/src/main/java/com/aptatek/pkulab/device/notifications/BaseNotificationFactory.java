package com.aptatek.pkulab.device.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;

abstract class BaseNotificationFactory {

    private static final String PKULAB_NOTI_CHANNEL = "pkulab.notifications";

    protected final Context context;
    protected final ResourceInteractor resourceInteractor;
    protected final NotificationManager notificationManager;

    BaseNotificationFactory(final Context context,
                                   final ResourceInteractor resourceInteractor,
                                   final NotificationManager notificationManager) {
        this.context = context;
        this.resourceInteractor = resourceInteractor;
        this.notificationManager = notificationManager;
    }

    protected String getChannelId() {
        return PKULAB_NOTI_CHANNEL;
    }

    protected String createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    getChannelId(),
                    resourceInteractor.getStringResource(R.string.test_countdown_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(false);
            notificationChannel.setVibrationPattern(new long[]{0L});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        return getChannelId();
    }
}
