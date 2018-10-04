package com.aptatek.pkuapp.device.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.aptatek.pkuapp.device.bluetooth.model.CartridgeIdResponse;
import com.aptatek.pkuapp.domain.error.DeviceNotSupportedError;
import com.aptatek.pkuapp.domain.error.ReaderError;
import com.aptatek.pkuapp.domain.manager.reader.ReaderManager;
import com.aptatek.pkuapp.domain.model.ReaderConnectionEvent;
import com.aptatek.pkuapp.domain.model.ReaderConnectionState;
import com.aptatek.pkuapp.domain.model.ReaderDevice;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
import timber.log.Timber;

public class ReaderManagerImpl implements ReaderManager {

    private final LumosReaderManager lumosReaderManager;

    private final FlowableProcessor<Integer> batteryLevel = BehaviorProcessor.create();
    private final FlowableProcessor<String> cartridgeId = BehaviorProcessor.create();
    private final FlowableProcessor<ReaderError> readerError = BehaviorProcessor.create();
    private final FlowableProcessor<ReaderConnectionEvent> connectionState = BehaviorProcessor.createDefault(ReaderConnectionEvent.create(null, ReaderConnectionState.DISCONNECTED));

    @Inject
    public ReaderManagerImpl(final LumosReaderManager lumosReaderManager) {
        this.lumosReaderManager = lumosReaderManager;

        lumosReaderManager.setGattCallbacks(new LumosReaderCallbacks() {
            @Override
            public void onDeviceConnecting(final BluetoothDevice device) {
                connectionState.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.CONNECTING));
            }

            @Override
            public void onDeviceConnected(final BluetoothDevice device) {
                connectionState.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.CONNECTED));
            }

            @Override
            public void onDeviceDisconnecting(final BluetoothDevice device) {
                connectionState.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.DISCONNECTING));
            }

            @Override
            public void onDeviceDisconnected(final BluetoothDevice device) {
                connectionState.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.DISCONNECTED));
            }

            @Override
            public void onLinklossOccur(final BluetoothDevice device) {
                connectionState.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.DISCONNECTED));
            }

            @Override
            public void onServicesDiscovered(final BluetoothDevice device, final boolean optionalServicesFound) {

            }

            @Override
            public void onDeviceReady(final BluetoothDevice device) {
                connectionState.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.READY));
            }

            @Override
            public boolean shouldEnableBatteryLevelNotifications(final BluetoothDevice device) {
                return true;
            }

            @Override
            public void onBatteryValueReceived(final BluetoothDevice device, final int value) {
                batteryLevel.onNext(value);
            }

            @Override
            public void onBondingRequired(final BluetoothDevice device) {
                connectionState.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.BONDING_REQUIRED));
            }

            @Override
            public void onBonded(final BluetoothDevice device) {
                connectionState.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.CONNECTED));
            }

            @Override
            public void onError(final BluetoothDevice device, final String message, final int errorCode) {
                readerError.onNext(new ReaderError(message, errorCode));
            }

            @Override
            public void onDeviceNotSupported(final BluetoothDevice device) {
                readerError.onNext(new DeviceNotSupportedError());
            }

            @Override
            public void onReadCartridgeId(@NonNull final CartridgeIdResponse cartridgeIdResponse) {
                cartridgeId.onNext(cartridgeIdResponse.getCartridgeId());
            }
        });
    }

    @Override
    public void connect(@NonNull final ReaderDevice readerDevice) {
        if (readerDevice instanceof BluetoothReaderDevice) {
            lumosReaderManager.connect(((BluetoothReaderDevice) readerDevice).getBluetoothDevice());
        } else {
            Timber.e("Unhandled ReaderDevice implementation received!");
        }
    }

    @Override
    public void disconnect() {
        lumosReaderManager.disconnect();
    }

    @Override
    public void queryBatteryLevel() {
        lumosReaderManager.readBatteryLevel();
    }

    @Override
    public void queryCartridgeId() {
        lumosReaderManager.getCartridgeId();
    }

    @Override
    public Flowable<ReaderConnectionEvent> connectionEvents() {
        return connectionState;
    }

    @Override
    public Flowable<ReaderError> readerErrors() {
        return readerError;
    }

    @Override
    public Flowable<Integer> batteryLevel() {
        return batteryLevel;
    }

    @Override
    public Flowable<String> cartridgeId() {
        return cartridgeId;
    }
}
