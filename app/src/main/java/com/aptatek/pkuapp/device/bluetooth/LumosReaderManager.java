package com.aptatek.pkuapp.device.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.injection.qualifier.ApplicationContext;

import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

import javax.inject.Inject;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.Request;
import timber.log.Timber;

public class LumosReaderManager extends BleManager<LumosReaderCallbacks> {

    private static final UUID READER_SERVICE_UUID = UUID.fromString("84A91C5E-24C5-4726-9860-0847BB1D01E7");
    private static final UUID READER_CHAR_WORKFLOW_STATE_UUID = UUID.fromString("84A91001-24C5-4726-9860-0847BB1D01E7");
    private static final UUID READER_CHAR_ERROR_UUID = UUID.fromString("84A94001-24C5-4726-9860-0847BB1D01E7");
    private static final UUID READER_CHAR_CARTRIDGE_ID_UUID = UUID.fromString("84A93001-24C5-4726-9860-0847BB1D01E7");
    // TODO add more characteristics

    private BluetoothGattCharacteristic workflowStateChar;
    private BluetoothGattCharacteristic errorChar;
    private BluetoothGattCharacteristic cartridgeIdChar;

    private final BleManagerGattCallback bleGattCallback = new BleManagerGattCallback() {

        @Override
        protected Deque<Request> initGatt(final BluetoothGatt gatt) {
            final LinkedList<Request> requests = new LinkedList<>();
            requests.push(Request.newReadRequest(workflowStateChar));
            requests.push(Request.newEnableNotificationsRequest(workflowStateChar));
            requests.push(Request.newReadRequest(errorChar));
            requests.push(Request.newEnableNotificationsRequest(errorChar));
            requests.push(Request.newReadRequest(cartridgeIdChar));
            return requests;
        }

        @Override
        public boolean isRequiredServiceSupported(final BluetoothGatt gatt) {
            final BluetoothGattService service = gatt.getService(READER_SERVICE_UUID);
            if (service != null) {
                workflowStateChar = service.getCharacteristic(READER_CHAR_WORKFLOW_STATE_UUID);
                errorChar = service.getCharacteristic(READER_CHAR_ERROR_UUID);
                cartridgeIdChar = service.getCharacteristic(READER_CHAR_CARTRIDGE_ID_UUID);
            }

            return workflowStateChar != null && errorChar != null && cartridgeIdChar != null;
        }

        @Override
        protected void onDeviceDisconnected() {
            workflowStateChar = null;
            errorChar = null;
            cartridgeIdChar = null;
        }

        @Override
        protected void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            if (characteristic == workflowStateChar) {
                // TODO parse workflow state
                Timber.d("Reading workflow state characteristic %s", characteristic);
            } else if (characteristic == errorChar) {
                // TODO parse error
                Timber.d("Reading error characteristic %s", characteristic);
            } else if (characteristic == cartridgeIdChar) {
                // TODO parse cartridge id
                Timber.d("Reading cartridgeId characteristic %s", characteristic);
            } else {
                Timber.d("Unhandled characteristic: %s", characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // TODO implement 'Request Previous Result' characteristic and write it
            Timber.d("onCharacteristicWrite: %s", characteristic);
        }

        @Override
        public void onCharacteristicNotified(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            if (characteristic == workflowStateChar) {
                // TODO read workflow state value
                Timber.d("Notified workflow state characteristic: %s", characteristic);
            } else if (characteristic == errorChar) {
                // TODO read error
                Timber.d("Notified error characteristic: %s", characteristic);
            } else {
                Timber.d("Unhandled characteristic: %s", characteristic);
            }
        }
    };

    @Inject
    public LumosReaderManager(@ApplicationContext @NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return bleGattCallback;
    }

    @Override
    protected boolean shouldAutoConnect() {
        return true;
    }

    @Override
    public void log(int level, @NonNull String message) {
        Timber.d(message);
    }

}
