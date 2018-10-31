package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.model.BluetoothReaderDevice;
import com.aptatek.pkulab.device.bluetooth.model.CartridgeIdResponse;
import com.aptatek.pkulab.domain.error.DeviceBondingFailedError;
import com.aptatek.pkulab.domain.error.DeviceNotSupportedError;
import com.aptatek.pkulab.domain.error.GeneralReaderError;
import com.aptatek.pkulab.domain.error.MtuChangeFailedError;
import com.aptatek.pkulab.domain.error.ReaderError;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.domain.model.ReaderConnectionEvent;
import com.aptatek.pkulab.domain.model.ReaderConnectionState;
import com.aptatek.pkulab.domain.model.ReaderDevice;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
import timber.log.Timber;

public class ReaderManagerImpl implements ReaderManager {

    private final LumosReaderManager lumosReaderManager;

    private final FlowableProcessor<Integer> batteryLevelProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<String> cartridgeIdProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<ReaderError> readerErrorProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<ReaderConnectionEvent> connectionStateProcessor = BehaviorProcessor.createDefault(ReaderConnectionEvent.create(null, ReaderConnectionState.DISCONNECTED));
    private final FlowableProcessor<Integer> mtuSizeProcessor = BehaviorProcessor.create();

    private int requestedMtuSize;

    @Inject
    public ReaderManagerImpl(final LumosReaderManager lumosReaderManager) {
        this.lumosReaderManager = lumosReaderManager;

        lumosReaderManager.setGattCallbacks(new LumosReaderCallbacks() {
            @Override
            public void onDeviceConnecting(final BluetoothDevice device) {
                connectionStateProcessor.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.CONNECTING));
            }

            @Override
            public void onDeviceConnected(final BluetoothDevice device) {
                connectionStateProcessor.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.CONNECTED));
            }

            @Override
            public void onDeviceDisconnecting(final BluetoothDevice device) {
                connectionStateProcessor.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.DISCONNECTING));
            }

            @Override
            public void onDeviceDisconnected(final BluetoothDevice device) {
                connectionStateProcessor.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.DISCONNECTED));
            }

            @Override
            public void onServicesDiscovered(final BluetoothDevice device, final boolean optionalServicesFound) {
                Timber.d("onServicesDiscovered");
            }

            @Override
            public void onDeviceReady(final BluetoothDevice device) {
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    lumosReaderManager.queueMtuChange(requestedMtuSize);
                }
            }

            @Override
            public void onBondingRequired(final BluetoothDevice device) {
                connectionStateProcessor.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.BONDING_REQUIRED));
            }

            @Override
            public void onBonded(final BluetoothDevice device) {
                connectionStateProcessor.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.BOND));
            }

            @Override
            public void onError(final BluetoothDevice device, final String message, final int errorCode) {
                final ReaderError parsedError;

                if (errorCode == LumosReaderConstants.ERROR_MTU_CHANGE_FAILED) {
                    parsedError = new MtuChangeFailedError();
                } else {
                    parsedError = new GeneralReaderError(message, errorCode);
                }

                readerErrorProcessor.onNext(parsedError);
            }

            @Override
            public void onDeviceNotSupported(final BluetoothDevice device) {
                readerErrorProcessor.onNext(new DeviceNotSupportedError());
            }

            @Override
            public void onLinkLossOccurred(final BluetoothDevice device) {
                connectionStateProcessor.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.DISCONNECTED));
            }

            @Override
            public void onBondingFailed(final BluetoothDevice device) {
                readerErrorProcessor.onNext(new DeviceBondingFailedError());
                disconnect();
            }

            @Override
            public void onReadCartridgeId(@NonNull final CartridgeIdResponse cartridgeIdResponse) {
                cartridgeIdProcessor.onNext(cartridgeIdResponse.getCartridgeId());
            }

            @Override
            public void onMtuSizeChanged(@NonNull final BluetoothDevice device, final int mtuSize) {
                mtuSizeProcessor.onNext(mtuSize);
                connectionStateProcessor.onNext(ReaderConnectionEvent.create(new BluetoothReaderDevice(device), ReaderConnectionState.READY));
            }

            @Override
            public void onReadBatteryLevel(final int batteryLevel) {
                batteryLevelProcessor.onNext(batteryLevel);
            }
        });
    }

    @Override
    public void connect(@NonNull final ReaderDevice readerDevice, final int mtuSize) {
        if (readerDevice instanceof BluetoothReaderDevice) {
            requestedMtuSize = mtuSize;

            lumosReaderManager.queueConnect(((BluetoothReaderDevice) readerDevice).getBluetoothDevice());
        } else {
            Timber.e("Unhandled ReaderDevice implementation received!");
        }
    }

    @Override
    public void disconnect() {
        lumosReaderManager.queueDisconnect();
    }

    @Override
    public void queryBatteryLevel() {
        lumosReaderManager.queueBatteryRead();
    }

    @Override
    public void queryCartridgeId() {
        lumosReaderManager.queueCartridgeId();
    }

    @Override
    public Flowable<ReaderConnectionEvent> connectionEvents() {
        return connectionStateProcessor;
    }

    @Override
    public Flowable<ReaderError> readerErrors() {
        return readerErrorProcessor;
    }

    @Override
    public Flowable<Integer> batteryLevel() {
        return batteryLevelProcessor;
    }

    @Override
    public Flowable<String> cartridgeId() {
        return cartridgeIdProcessor;
    }

    @Override
    public Flowable<Integer> mtuSize() {
        return mtuSizeProcessor;
    }
}
