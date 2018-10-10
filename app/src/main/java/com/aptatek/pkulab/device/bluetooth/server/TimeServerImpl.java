package com.aptatek.pkulab.device.bluetooth.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.domain.error.BluetoothNotEnabledError;
import com.aptatek.pkulab.domain.error.TimeServerError;
import com.aptatek.pkulab.domain.manager.reader.TimeServer;
import com.aptatek.pkulab.domain.manager.reader.TimeServerCallbacks;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import timber.log.Timber;

public class TimeServerImpl implements TimeServer {

    private final @ApplicationContext Context context;
    private final BluetoothManager bluetoothManager;
    private final AdvertiseSettings advertiseSettings;
    private final AdvertiseData advertiseData;
    private final BluetoothGattService timeService;
    private AdvertiseCallback advertiseCallback;

    private final BluetoothGattServerCallback gattServerCallback;

    private BluetoothGattServer gattServer;

    public TimeServerImpl(@ApplicationContext final Context context,
                          final BluetoothManager bluetoothManager,
                          final AdvertiseSettings advertiseSettings,
                          final AdvertiseData advertiseData,
                          final BluetoothGattService timeService) {
        this.context = context;
        this.bluetoothManager = bluetoothManager;
        this.advertiseSettings = advertiseSettings;
        this.advertiseData = advertiseData;
        this.timeService = timeService;

        gattServerCallback = new BluetoothGattServerCallback() {
            @Override
            public void onConnectionStateChange(final BluetoothDevice device, final int status, final int newState) {
                super.onConnectionStateChange(device, status, newState);
                Timber.d("onConnectionStateChange: device [%s], status [%d], newState [%d]", device, status, newState);
            }

            @Override
            public void onServiceAdded(final int status, final BluetoothGattService service) {
                super.onServiceAdded(status, service);
                Timber.d("onServiceAdded: status [%d], service [%s]", status, service);
            }

            @Override
            public void onCharacteristicReadRequest(final BluetoothDevice device, final int requestId, final int offset, final BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
                Timber.d("onCharacteristicReadRequest: device [%s], requestId [%d], offset [%d], characteristic [%s]", device, requestId, offset, characteristic.getUuid());

                final String uuid = characteristic.getUuid().toString();
                if (TextUtils.equals(uuid, LumosReaderConstants.TIME_SERVICE_CHAR_CURRENT_TIME)) {
                    gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, TimeServerHelper.getExactTime());
                } else if (TextUtils.equals(uuid, LumosReaderConstants.TIME_SERVICE_CHAR_LOCAL_TIME_INFO)) {
                    gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, TimeServerHelper.getLocalTimeInfo());
                } else {
                    Timber.d("Unsupported characteristic read: [%s]", uuid);
                    gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_FAILURE, 0, null);
                }

            }

            @Override
            public void onCharacteristicWriteRequest(final BluetoothDevice device, final int requestId, final BluetoothGattCharacteristic characteristic, final boolean preparedWrite, final boolean responseNeeded, final int offset, final byte[] value) {
                super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
                Timber.d("onCharacteristicWriteRequest: device [%s], requestId [%d], characteristic [%s]", device, requestId, characteristic.getUuid());
            }

            @Override
            public void onDescriptorReadRequest(final BluetoothDevice device, final int requestId, final int offset, final BluetoothGattDescriptor descriptor) {
                super.onDescriptorReadRequest(device, requestId, offset, descriptor);
                Timber.d("onDescriptorReadRequest: device [%s], requestId [%d], offset [%d], descriptor [%s]", device, requestId, offset, descriptor.getUuid());
            }

            @Override
            public void onDescriptorWriteRequest(final BluetoothDevice device, final int requestId, final BluetoothGattDescriptor descriptor, final boolean preparedWrite, final boolean responseNeeded, final int offset, final byte[] value) {
                super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
                Timber.d("onDescriptorWriteRequest: device [%s], requestId [%d], descriptor [%s]", device, requestId, descriptor.getUuid());
            }

            @Override
            public void onExecuteWrite(final BluetoothDevice device, final int requestId, final boolean execute) {
                super.onExecuteWrite(device, requestId, execute);
                Timber.d("onExecuteWrite: device [%s], requestId [%d]", device, requestId);
            }

            @Override
            public void onNotificationSent(final BluetoothDevice device, final int status) {
                super.onNotificationSent(device, status);
                Timber.d("onNotificationSent: device [%s], status [%d]", device, status);
            }

            @Override
            public void onMtuChanged(final BluetoothDevice device, final int mtu) {
                super.onMtuChanged(device, mtu);
                Timber.d("onMtuChanged: device [%s], mtu [%d]", device, mtu);
            }

            @Override
            public void onPhyUpdate(final BluetoothDevice device, final int txPhy, final int rxPhy, final int status) {
                super.onPhyUpdate(device, txPhy, rxPhy, status);
                Timber.d("onPhyUpdate: device [%s], txPhy [%d], rxPhy [%d], status [%d]", device, txPhy, rxPhy, status);
            }

            @Override
            public void onPhyRead(final BluetoothDevice device, final int txPhy, final int rxPhy, final int status) {
                super.onPhyRead(device, txPhy, rxPhy, status);
                Timber.d("onPhyRead: device [%s], txPhy [%d], rxPhy [%d], status [%d]", device, txPhy, rxPhy, status);
            }
        };
    }

    private void startGattServer(@NonNull final TimeServerCallbacks timeServerCallbacks) {
        try {
            gattServer = bluetoothManager.openGattServer(context, gattServerCallback);
            gattServer.addService(timeService);
            timeServerCallbacks.onOperationSuccessful();
        } catch (Exception e) {
            timeServerCallbacks.onOperationFailed(new TimeServerError());
        }
    }

    @Override
    public void startServer(@NonNull final TimeServerCallbacks timeServerCallbacks) {
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        if (adapter == null || !adapter.isEnabled()) {
            Timber.d("Bluetooth adapter is not enabled!");

            timeServerCallbacks.onOperationFailed(new BluetoothNotEnabledError());

            return;
        }

        final BluetoothLeAdvertiser advertiser = adapter.getBluetoothLeAdvertiser();

        advertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);

                startGattServer(timeServerCallbacks);
            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                timeServerCallbacks.onOperationFailed(new TimeServerError()); // TODO distinguish error code when needed
            }
        };

        advertiser.startAdvertising(advertiseSettings, advertiseData, advertiseCallback);
    }

    @Override
    public void stopServer(@NonNull final TimeServerCallbacks timeServerCallbacks) {
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        if (adapter == null || !adapter.isEnabled()) {
            Timber.d("Bluetooth adapter is not enabled!");

            timeServerCallbacks.onOperationFailed(new BluetoothNotEnabledError());

            return;
        }

        if (gattServer != null) {
            gattServer.close();
        }

        if (advertiseCallback != null) {
            adapter.getBluetoothLeAdvertiser().stopAdvertising(advertiseCallback);
        }

        timeServerCallbacks.onOperationSuccessful();
    }
}
