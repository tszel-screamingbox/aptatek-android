package com.aptatek.pkulab.device.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.aptatek.pkulab.view.service.BluetoothService;

public class BluetoothNotificationFactoryImpl extends BaseNotificationFactory implements BluetoothNotificationFactory {

    public BluetoothNotificationFactoryImpl(@ApplicationContext final Context context,
                                            final ResourceInteractor resourceInteractor,
                                            final NotificationManager notificationManager) {
        super(context, resourceInteractor, notificationManager);
    }

    @NonNull
    @Override
    public Notification createNotification(@NonNull final NotificationData notificationData) {
        final Notification notification;

        if (notificationData instanceof ConnectingToDevice) {
            notification = createConnectingNotification();
        } else if (notificationData instanceof ConnectedToDevice) {
            notification = createConnectedNotification(((ConnectedToDevice) notificationData));
        } else if (notificationData instanceof SyncingData) {
            notification = createSyncingDataNotification(((SyncingData) notificationData));
        } else if (notificationData instanceof RunningTest) {
            notification = createTestRunningNotification(((RunningTest) notificationData));
        } else if (notificationData instanceof TestComplete) {
            notification = createTestCompleteNotification(((TestComplete) notificationData));
        } else if (notificationData instanceof CommunicationError) {
            notification = createErrorNotification(((CommunicationError) notificationData));
        } else if (notificationData instanceof DeviceDisconnected) {
            notification = createDisconnectedNotification((DeviceDisconnected) notificationData);
        } else {
            throw new IllegalArgumentException("unhandled notificationData received: " + notificationData);
        }

        return notification;
    }

    private Notification createConnectingNotification() {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_connecting_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_connecting_message)))
                .build();
    }

    private Notification createConnectedNotification(final ConnectedToDevice message) {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_connected_title, message.getDevice().getMac()))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_connected_message, message.getBatteryPercent())))
                .build();
    }

    private Notification createSyncingDataNotification(final SyncingData message) {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_sync_title, message.getDevice().getMac()))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_sync_message)))
                .setProgress(0, message.getProgressPercent(), false)
                .build();
    }

    private Notification createTestRunningNotification(final RunningTest message) {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_test_running_title, message.getDevice().getMac()))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_test_running_message)))
                .setProgress(0, 100, true)
                .build();
    }

    private Notification createTestCompleteNotification(final TestComplete message) {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_test_complete_title, message.getDevice().getMac()))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_test_complete_message)))
                // TODO set content intent -- what to do here?
                .build();
    }

    private Notification createErrorNotification(final CommunicationError message) {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_error_title))
                .setContentText(message.getReason()))
                // TODO set content intent - how to handle errors?
                .build();
    }

    private Notification createDisconnectedNotification(final DeviceDisconnected notificationData) {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_error_disconnected_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_error_disconnected_message)))
                .setContentIntent(PendingIntent.getService(context, 0, new Intent(context, BluetoothService.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
    }

    private NotificationCompat.Builder applyCommonProperties(final NotificationCompat.Builder builder) {
        return builder.setSmallIcon(R.drawable.ic_play)
                .setVibrate(new long[]{0L})
                .setSound(null)
                .setDefaults(0)
                .setAutoCancel(true);
    }
}
