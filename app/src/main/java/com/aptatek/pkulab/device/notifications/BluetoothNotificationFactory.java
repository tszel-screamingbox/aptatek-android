package com.aptatek.pkulab.device.notifications;

import android.app.Notification;
import android.support.annotation.NonNull;

public interface BluetoothNotificationFactory {

    interface NotificationData {
    }

    class ConnectingToDevice implements NotificationData {
    }

    class ConnectedToDeviceSilently implements NotificationData {

        private final int batteryPercent;

        public ConnectedToDeviceSilently(int batteryPercent) {
            this.batteryPercent = batteryPercent;
        }

        public int getBatteryPercent() {
            return batteryPercent;
        }
    }

    class ConnectedToDeviceTestWorkflow implements NotificationData {

    }

    class SyncingData implements NotificationData {

    }

    class TestComplete implements NotificationData {

    }

    class BluetoothError implements NotificationData {

        private final String reason;

        public BluetoothError(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }

    }

    class MissingPermissionError implements NotificationData {

    }

    class MultipleReadersDiscovered implements NotificationData {

    }

    final class DisplayNotification {

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
