package com.aptatek.pkulab.device.notifications;

import android.app.Notification;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.view.error.ErrorModel;

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

        private final String testId;


        public TestComplete(final String testId) {
            this.testId = testId;
        }

        public String getTestId() {
            return testId;
        }

        @Override
        public String toString() {
            return "TestComplete{" +
                    "testId='" + testId + '\'' +
                    '}';
        }
    }

    class ExplicitBtConnectionError implements NotificationData {

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

    final class WorkflowStateError implements NotificationData {
        private final ErrorModel model;

        public WorkflowStateError(ErrorModel model) {
            this.model = model;
        }

        public ErrorModel getModel() {
            return model;
        }

        @Override
        public String toString() {
            return "WorkflowStateError{" +
                    "model=" + model +
                    '}';
        }
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
