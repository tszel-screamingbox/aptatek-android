package com.aptatek.pkulab.device.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.test.TestActivity;
import com.aptatek.pkulab.view.test.result.TestResultActivity;

public class BluetoothNotificationFactoryImpl extends BaseNotificationFactory implements BluetoothNotificationFactory {

    private static final String READER_STATUS_CHANNEL = "pkulab.reader.status"; // silent channel for foreground notifications
    private static final String READER_EVENTS_CHANNEL = "pkulab.reader.events"; // "noisy" channel for reader events

    // status notification id for foreground service
    private static final int BT_READER_STATUS_NOTIFICATION_ID = 7827;

    public BluetoothNotificationFactoryImpl(@ApplicationContext final Context context,
                                            final ResourceInteractor resourceInteractor,
                                            final NotificationManager notificationManager) {
        super(context, resourceInteractor, notificationManager);
    }

    @Override
    protected String getChannelId() {
        return READER_EVENTS_CHANNEL;
    }

    private String createStatusChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    READER_STATUS_CHANNEL,
                    resourceInteractor.getStringResource(R.string.notification_channel_reader_status),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(false);
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        return READER_STATUS_CHANNEL;
    }

    @NonNull
    @Override
    public DisplayNotification createNotification(@NonNull final NotificationData notificationData) {
        final Notification notification;
        final int id;

        if (notificationData instanceof ConnectingToDevice) {
            notification = createConnectingNotification();
            id = BT_READER_STATUS_NOTIFICATION_ID;
        } else if (notificationData instanceof ConnectedToDeviceSilently) {
            notification = createConnectedNotification(((ConnectedToDeviceSilently) notificationData));
            id = BT_READER_STATUS_NOTIFICATION_ID;
        } else if (notificationData instanceof ConnectedToDeviceTestWorkflow) {
            notification = createConnectedTestWorkflowNotification();
            id = Constants.BT_READER_READY_NOTIFICATION_ID;
        } else if (notificationData instanceof SyncingData) {
            notification = createSyncingDataNotification();
            id = BT_READER_STATUS_NOTIFICATION_ID;
        } else if (notificationData instanceof TestComplete) {
            notification = createTestCompleteNotification();
            id = Constants.BT_READER_TEST_COMPLETE_NOTIFICATION_ID;
        } else if (notificationData instanceof BluetoothError) {
            notification = createErrorNotification(((BluetoothError) notificationData));
            id = Constants.BT_ERROR_NOTIFICATION_ID;
        } else if (notificationData instanceof MissingPermissionError) {
            notification = createMissingPermissionNotification(((MissingPermissionError) notificationData));
            id = Constants.BT_PERMISSION_NOTIFICATION_ID;
        } else if (notificationData instanceof MultipleReadersDiscovered) {
            notification = createMultipleReadersNotification(((MultipleReadersDiscovered) notificationData));
            id = Constants.BT_MULTIPLE_READERS_NOTIFICATION_ID;
        } else {
            throw new IllegalArgumentException("unhandled notificationData received: " + notificationData);
        }

        return new DisplayNotification(id, notification);
    }

    private Notification createMultipleReadersNotification(final MultipleReadersDiscovered notificationData) {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_multiplereaders_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_multiplereaders_message)))
                .setContentIntent(PendingIntent.getActivity(context, 0, TestActivity.createStarter(context), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
    }

    private Notification createMissingPermissionNotification(final MissingPermissionError notificationData) {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_permission_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_permission_message)))
                .setContentIntent(PendingIntent.getActivity(context, 0, TestActivity.createStarter(context), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
    }

    private Notification createConnectedTestWorkflowNotification() {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_connected_test_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_connected_test_message)))
                .setContentIntent(PendingIntent.getActivity(context, 0, TestActivity.createStarter(context), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
    }

    private Notification createConnectingNotification() {
        return applyCommonProperties(new NotificationCompat.Builder(context, createStatusChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_connecting_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_connecting_message)))
                .build();
    }

    private Notification createConnectedNotification(final ConnectedToDeviceSilently message) {
        return applyCommonProperties(new NotificationCompat.Builder(context, createStatusChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_connected_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_connected_message, message.getBatteryPercent())))
                .build();
    }

    private Notification createSyncingDataNotification() {
        return applyCommonProperties(new NotificationCompat.Builder(context, createStatusChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_sync_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_sync_message)))
                .build();
    }

    private Notification createTestCompleteNotification() {
        return applyCommonProperties(new NotificationCompat.Builder(context, createChannel())
                .setContentTitle(resourceInteractor.getStringResource(R.string.bluetooth_notification_test_complete_title))
                .setContentText(resourceInteractor.getStringResource(R.string.bluetooth_notification_test_complete_message)))
                .setContentIntent(PendingIntent.getActivity(context, 0, TestResultActivity.starter(context), PendingIntent.FLAG_CANCEL_CURRENT))
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
