package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.model.BluetoothReaderDevice;
import com.aptatek.pkulab.device.bluetooth.model.ErrorResponse;
import com.aptatek.pkulab.device.bluetooth.model.ResultResponse;
import com.aptatek.pkulab.device.bluetooth.model.ResultSyncResponse;
import com.aptatek.pkulab.device.bluetooth.model.WorkflowStateResponse;
import com.aptatek.pkulab.domain.error.DeviceBondingFailedError;
import com.aptatek.pkulab.domain.error.DeviceNotSupportedError;
import com.aptatek.pkulab.domain.error.GeneralReaderError;
import com.aptatek.pkulab.domain.error.MtuChangeFailedError;
import com.aptatek.pkulab.domain.error.ReaderError;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.reader.ConnectionEvent;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.util.Constants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
import ix.Ix;
import timber.log.Timber;

public class ReaderManagerImpl implements ReaderManager {

    private final LumosReaderManager lumosReaderManager;

    private final FlowableProcessor<ReaderError> readerErrorProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<ConnectionEvent> connectionStateProcessor = BehaviorProcessor.createDefault(ConnectionEvent.create(null, ConnectionState.DISCONNECTED));
    private final FlowableProcessor<Integer> mtuSizeProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<WorkflowState> workflowStateProcessor = BehaviorProcessor.create();

    @Inject
    public ReaderManagerImpl(final LumosReaderManager lumosReaderManager) {
        this.lumosReaderManager = lumosReaderManager;

        lumosReaderManager.setGattCallbacks(new LumosReaderCallbacks() {
            @Override
            public void onDeviceConnecting(final @NonNull BluetoothDevice device) {
                Timber.d("onDeviceConnecting: %s", device.getAddress());
                connectionStateProcessor.onNext(ConnectionEvent.create(new BluetoothReaderDevice(device), ConnectionState.CONNECTING));
            }

            @Override
            public void onDeviceConnected(final @NonNull BluetoothDevice device) {
                Timber.d("onDeviceConnected: %s", device.getAddress());
                connectionStateProcessor.onNext(ConnectionEvent.create(new BluetoothReaderDevice(device), ConnectionState.CONNECTED));
            }

            @Override
            public void onDeviceDisconnecting(final @NonNull BluetoothDevice device) {
                Timber.d("onDeviceDisconnecting: device [%s]", device.getAddress());
                connectionStateProcessor.onNext(ConnectionEvent.create(new BluetoothReaderDevice(device), ConnectionState.DISCONNECTING));
            }

            @Override
            public void onDeviceDisconnected(final @NonNull BluetoothDevice device) {
                Timber.d("onDeviceDisconnected: device [%s]", device.getAddress());
                connectionStateProcessor.onNext(ConnectionEvent.create(new BluetoothReaderDevice(device), ConnectionState.DISCONNECTED));
            }

            @Override
            public void onServicesDiscovered(final @NonNull BluetoothDevice device, final boolean optionalServicesFound) {
                Timber.d("onServicesDiscovered device [%s]", device.getAddress());
            }

            @Override
            public void onDeviceReady(final @NonNull BluetoothDevice device) {
                Timber.d("onDeviceReady device [%s]", device.getAddress());
//                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
//                    lumosReaderManager.queueMtuChange(requestedMtuSize);
//                }
                connectionStateProcessor.onNext(ConnectionEvent.create(new BluetoothReaderDevice(device), ConnectionState.READY));
            }

            @Override
            public void onBondingRequired(final @NonNull BluetoothDevice device) {
                Timber.d("onBondingRequired: device [%s]", device.getAddress());
                connectionStateProcessor.onNext(ConnectionEvent.create(new BluetoothReaderDevice(device), ConnectionState.BONDING_REQUIRED));
            }

            @Override
            public void onBonded(final @NonNull BluetoothDevice device) {
                Timber.d("onBonded: device [%s]", device.getAddress());
                connectionStateProcessor.onNext(ConnectionEvent.create(new BluetoothReaderDevice(device), ConnectionState.BOND));
            }

            @Override
            public void onError(final @NonNull BluetoothDevice device, final @NonNull String message, final int errorCode) {
                Timber.d("onError: device [%s], message: [%s], errorCode: [%d]", device.getAddress(), message, errorCode);
                final ReaderError parsedError;

                if (errorCode == LumosReaderConstants.ERROR_MTU_CHANGE_FAILED) {
                    parsedError = new MtuChangeFailedError();
                } else {
                    parsedError = new GeneralReaderError(message, errorCode);
                }

                readerErrorProcessor.onNext(parsedError);
            }

            @Override
            public void onDeviceNotSupported(final @NonNull BluetoothDevice device) {
                Timber.d("onDeviceNotSupported: device [%s]", device.getAddress());
                readerErrorProcessor.onNext(new DeviceNotSupportedError());
            }

            @Override
            public void onLinkLossOccurred(final @NonNull BluetoothDevice device) {
                Timber.d("onLinkLossOccurred: device [%s]", device.getAddress());
                connectionStateProcessor.onNext(ConnectionEvent.create(new BluetoothReaderDevice(device), ConnectionState.DISCONNECTED));
            }

            @Override
            public void onBondingFailed(final @NonNull BluetoothDevice device) {
                Timber.d("onBondingFailed: device [%s]", device.getAddress());
                readerErrorProcessor.onNext(new DeviceBondingFailedError());
                disconnect();
            }

            @Override
            public void onReadResultSync(@NonNull final ResultSyncResponse resultSyncResponse) {
                Timber.d("onReadResultSync: %s", resultSyncResponse);
            }

            @Override
            public void onReadResult(@NonNull final ResultResponse resultResponse) {
                Timber.d("onReadResult: %s", resultResponse);
            }

            @Override
            public void onReadError(@NonNull final ErrorResponse errorResponse) {
                Timber.d("onReadError: %s", errorResponse);
            }

            @Override
            public void onMtuSizeChanged(@NonNull final BluetoothDevice device, final int mtuSize) {
                Timber.d("onMtuSizeChanged: device [%s], mtu: [%d]", device.getAddress(), mtuSize);
                mtuSizeProcessor.onNext(mtuSize);
//                connectionStateProcessor.onNext(ConnectionEvent.create(new BluetoothReaderDevice(device), ConnectionState.READY));
            }
            
            @Override
            public void onWorkflowStateChanged(@NonNull final WorkflowStateResponse workflowStateResponse) {
                Timber.d("onWorkflowStateChanged: %s", workflowStateResponse);
                workflowStateProcessor.onNext(mapToDomain(workflowStateResponse));
            }
        });
    }

    // TODO refactor this to mapper
    private WorkflowState mapToDomain(final WorkflowStateResponse response) {
        switch (response.getState()) {
            case "TEST RUNNING": {
                return WorkflowState.TEST_RUNNING;
            }
            case "TEST COMPLETE": {
                return WorkflowState.TEST_COMPLETE;
            }
            case "default":
            default: {
                return WorkflowState.DEFAULT;
            }
        }
    }

    @Override
    public Completable connect(@NonNull final ReaderDevice readerDevice) {
        if (readerDevice instanceof BluetoothReaderDevice) {
            return lumosReaderManager.connectAndBound(((BluetoothReaderDevice) readerDevice).getBluetoothDevice());

//            // TODO handle errors during connect!!!
//            return Completable.fromAction(() -> lumosReaderManager.queueConnect(((BluetoothReaderDevice) readerDevice).getBluetoothDevice()))
//                    .andThen(connectionStateProcessor.map(ConnectionEvent::getConnectionState)
//                    .filter(ConnectionState.READY::equals)
//                    .take(1)
//                    .flatMapCompletable(it -> Completable.complete()));
        } else {
            Timber.e("Unhandled ReaderDevice implementation received!");
            return Completable.complete();
        }
    }

    @Override
    public Completable disconnect() {
        return lumosReaderManager.disconnectCompletable();

//        return Completable.fromAction(lumosReaderManager::queueDisconnect)
//                .andThen(connectionStateProcessor.map(ConnectionEvent::getConnectionState)
//                        .filter(ConnectionState.DISCONNECTED::equals)
//                        .take(1)
//                        .flatMapCompletable(it -> Completable.complete()));
    }

    @Override
    public Completable changeMtu(int mtuSize) {
        return Completable.fromAction(() -> lumosReaderManager.queueMtuChange(mtuSize));
    }

    @Override
    public Single<Integer> getBatteryLevel() {
        return lumosReaderManager.readCharacteristic(LumosReaderConstants.BATTERY_CHAR_LEVEL);
    }

    @Override
    public Single<String> getCartridgeId() {
        return lumosReaderManager.readCharacteristic(LumosReaderConstants.READER_CHAR_CARTRIDGE_ID);
    }

    @Override
    public Single<Integer> getNumberOfResults() {
        return lumosReaderManager.readCharacteristic(LumosReaderConstants.READER_CHAR_NUM_RESULTS);
    }

    @Override
    public Single<TestResult> getResult(@NonNull String id) {
        return lumosReaderManager.readCharacteristic(LumosReaderConstants.READER_CHAR_RESULT);
    }

    @Override
    public Single<Error> getError() {
        return lumosReaderManager.readCharacteristic(LumosReaderConstants.READER_CHAR_ERROR);
    }

    @Override
    public Single<List<TestResult>> syncResults() {
        return lumosReaderManager.syncResults()
                .map(list -> Ix.from(list)
                        .map(data -> TestResult.builder()
                            .setId(data.getId())
                            .setPkuLevel(PkuLevel.create(Constants.DEFAULT_PKU_NORMAL_FLOOR, PkuLevelUnits.MICRO_MOL))
                            .setDate(System.currentTimeMillis()) // TODO convert string to long
                            .build())
                        .toList()
                );
    }

    @Override
    public Flowable<ConnectionEvent> connectionEvents() {
        return connectionStateProcessor;
    }

    @Override
    public Flowable<Integer> mtuSize() {
        return mtuSizeProcessor;
    }

    @Override
    public Flowable<WorkflowState> workflowState() {
        return workflowStateProcessor;
    }

}
