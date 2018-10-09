package com.aptatek.pkulab.device.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.notifications.CountdownNotificationFactory;
import com.aptatek.pkulab.view.test.TestActivity;

abstract class BaseCountdownNotificationFactory implements CountdownNotificationFactory {

    protected static final String APTATEK_COUNTDOWN_CHANNEL_ID = "aptatek.countdown.channel";

    protected final Context context;
    protected final ResourceInteractor resourceInteractor;
    protected final NotificationManager notificationManager;

    BaseCountdownNotificationFactory(final Context context,
                                     final ResourceInteractor resourceInteractor,
                                     final NotificationManager notificationManager) {
        this.context = context;
        this.resourceInteractor = resourceInteractor;
        this.notificationManager = notificationManager;
    }

    @Nullable
    protected abstract Bundle getIntentExtras();

    protected PendingIntent createContentIntent() {
        final Intent starter = TestActivity.createStarter(context);
        final Bundle intentExtras = getIntentExtras();

        if (intentExtras != null) {
            starter.putExtras(intentExtras);
        }

        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(starter);

        return stackBuilder.getPendingIntent(1, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    protected String createChannelId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    APTATEK_COUNTDOWN_CHANNEL_ID,
                    resourceInteractor.getStringResource(R.string.test_countdown_notification_channel_name),
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        return APTATEK_COUNTDOWN_CHANNEL_ID;
    }


}
