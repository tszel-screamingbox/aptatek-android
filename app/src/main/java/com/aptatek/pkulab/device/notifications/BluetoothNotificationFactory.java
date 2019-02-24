package com.aptatek.pkulab.device.notifications;

import android.app.Notification;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.model.reader.ReaderDevice;

public interface BluetoothNotificationFactory {

    interface NotificationData {}

    class ConnectingToDevice implements NotificationData {

    }

    static class ConnectedToDevice implements NotificationData {

        private final ReaderDevice device;
        private final int batteryPercent;

        public ConnectedToDevice(ReaderDevice device, int batteryPercent) {
            this.device = device;
            this.batteryPercent = batteryPercent;
        }

        public ReaderDevice getDevice() {
            return device;
        }

        public int getBatteryPercent() {
            return batteryPercent;
        }
    }

    static class SyncingData implements NotificationData {

        private final ReaderDevice device;

        public SyncingData(ReaderDevice device) {
            this.device = device;
        }

        public ReaderDevice getDevice() {
            return device;
        }
    }

    static class RunningTest implements NotificationData {

        private final ReaderDevice device;

        public RunningTest(ReaderDevice device) {
            this.device = device;
        }

        public ReaderDevice getDevice() {
            return device;
        }

    }

    static class TestComplete implements NotificationData {

        private final ReaderDevice device;

        public TestComplete(ReaderDevice device) {
            this.device = device;
        }

        public ReaderDevice getDevice() {
            return device;
        }
    }

    static class CommunicationError implements NotificationData {

        private final String reason;

        public CommunicationError(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }

    }

    static class DeviceDisconnected implements NotificationData {

    }

    @NonNull
    Notification createNotification(@NonNull NotificationData notificationData);

}
