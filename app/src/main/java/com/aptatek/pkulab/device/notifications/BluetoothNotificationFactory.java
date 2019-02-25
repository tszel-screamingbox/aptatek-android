package com.aptatek.pkulab.device.notifications;

import android.app.Notification;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;

public interface BluetoothNotificationFactory {

    interface NotificationData {}

    class ConnectingToDevice implements NotificationData {

    }

    static class ConnectedToDeviceSilently implements NotificationData {

        private final int batteryPercent;

        public ConnectedToDeviceSilently(int batteryPercent) {
            this.batteryPercent = batteryPercent;
        }

        public int getBatteryPercent() {
            return batteryPercent;
        }
    }

    static class ConnectedToDeviceTestWorkflow implements NotificationData {

    }

    static class SyncingData implements NotificationData {

    }

    static class TestComplete implements NotificationData {

    }

    static class BluetoothError implements NotificationData {

        private final String reason;

        public BluetoothError(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }

    }

    @NonNull
    Notification createNotification(@NonNull NotificationData notificationData);

}
