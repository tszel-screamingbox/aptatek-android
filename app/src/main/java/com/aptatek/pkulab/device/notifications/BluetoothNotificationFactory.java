package com.aptatek.pkulab.device.notifications;

import android.app.Notification;
import android.support.annotation.NonNull;

public interface BluetoothNotificationFactory {

    interface NotificationData {
    }

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

    static final class DisplayNotification {

        private final int id;
        private final Notification notification;

        public DisplayNotification(final int id, final Notification notification) {
            this.id = id;
            this.notification = notification;
        }

        public int getId() {
            return id;
        }

        public Notification getNotification() {
            return notification;
        }
    }

    @NonNull
    DisplayNotification createNotification(@NonNull NotificationData notificationData);

}
