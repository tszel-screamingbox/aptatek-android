package com.aptatek.pkulab.device.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

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
        } else if (notificationData instanceof ConnectedToDeviceSilently) {
            notification = createConnectedNotification(((ConnectedToDeviceSilently) notificationData));
        } else if (notificationData instanceof ConnectedToDeviceTestWorkflow) {
            notification = createConnectedTestWorkflowNotification();
        } else if (notificationData instanceof SyncingData) {
            notification = createSyncingDataNotification();
        } else if (notificationData instanceof TestComplete) {
            notification = createTestCompleteNotification();
        } else if (notificationData instanceof BluetoothError) {
            notification = createErrorNotification(((BluetoothError) notificationData));
        } else {
            throw new IllegalArgumentException("unhandled notificationData received: " + notificationData);
        }

        return notification;
    }

    private Notification createConnectedTestWorkflowNotification() {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_connected_test_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_connected_test_message)))
                .build();
    }

    private Notification createConnectingNotification() {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_connecting_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_connecting_message)))
                .build();
    }

    private Notification createConnectedNotification(final ConnectedToDeviceSilently message) {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_connected_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_connected_message, message.getBatteryPercent())))
                .build();
    }

    private Notification createSyncingDataNotification() {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_sync_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_sync_message)))
                .build();
    }

    private Notification createTestCompleteNotification() {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_test_complete_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_test_complete_message)))
                // TODO set content intent -- what to do here?
                .build();
    }

    private Notification createErrorNotification(final BluetoothError message) {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_error_title))
                .setContentText(message.getReason()))
                // TODO set content intent - how to handle errors?
                .build();
    }

    private NotificationCompat.Builder applyCommonProperties(final NotificationCompat.Builder builder) {
        return builder.setSmallIcon(R.drawable.ic_notification)
                .setVibrate(new long[]{0L})
                .setSound(null)
                .setDefaults(0)
                .setAutoCancel(true);
    }
}
