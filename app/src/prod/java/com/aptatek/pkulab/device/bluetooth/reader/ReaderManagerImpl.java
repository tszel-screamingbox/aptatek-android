package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.mapper.CartridgeInfoMapper;
import com.aptatek.pkulab.device.bluetooth.mapper.ErrorMapper;
import com.aptatek.pkulab.device.bluetooth.mapper.TestProgressMapper;
import com.aptatek.pkulab.device.bluetooth.mapper.TestResultMapper;
import com.aptatek.pkulab.device.bluetooth.mapper.WorkflowStateMapper;
import com.aptatek.pkulab.device.bluetooth.model.BluetoothReaderDevice;
import com.aptatek.pkulab.device.bluetooth.model.CartridgeIdResponse;
import com.aptatek.pkulab.device.bluetooth.model.ErrorResponse;
import com.aptatek.pkulab.device.bluetooth.model.NumPreviousResultsResponse;
import com.aptatek.pkulab.device.bluetooth.model.ResultResponse;
import com.aptatek.pkulab.device.bluetooth.model.SyncProgress;
import com.aptatek.pkulab.device.bluetooth.model.TestProgressResponse;
import com.aptatek.pkulab.device.bluetooth.model.WorkflowStateResponse;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.error.DeviceBondingFailedError;
import com.aptatek.pkulab.domain.error.DeviceNotSupportedError;
import com.aptatek.pkulab.domain.error.GeneralReaderError;
import com.aptatek.pkulab.domain.error.MtuChangeFailedError;
import com.aptatek.pkulab.domain.error.ReaderError;
import com.aptatek.pkulab.domain.manager.reader.ReaderManager;
import com.aptatek.pkulab.domain.model.reader.CartridgeInfo;
import com.aptatek.pkulab.domain.model.reader.ConnectionEvent;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
import timber.log.Timber;

public class ReaderManagerImpl implements ReaderManager {

    private final LumosReaderManager lumosReaderManager;

    private final FlowableProcessor<ReaderError> readerErrorProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<ConnectionEvent> connectionStateProcessor = BehaviorProcessor.createDefault(ConnectionEvent.create(null, ConnectionState.DISCONNECTED));
    private final FlowableProcessor<Integer> mtuSizeProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<WorkflowState> workflowStateProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<TestProgress> testProgressProcessor = BehaviorProcessor.create();
    private final FlowableProcessor<Integer> batteryLevelProcessor = BehaviorProcessor.create();

    private final Map<Class<?>, Mapper<?, ?>> mappers;

    public ReaderManagerImpl(final LumosReaderManager lumosReaderManager, final Map<Class<?>, Mapper<?, ?>> mappers) {
        this.lumosReaderManager = lumosReaderManager;
        this.mappers = mappers;

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
                connectionStateProcessor.onNext(ConnectionEvent.create(new BluetoothReaderDevice(device), ConnectionState.READY));
//                }
            }

            @Override
            public void onBondingRequired(final @NonNull BluetoothDevice device) {
                Timber.d("onBondingRequired: device [%s]", device.getAddress());
            }

            @Override
            public void onBonded(final @NonNull BluetoothDevice device) {
                Timber.d("onBonded: device [%s]", device.getAddress());
//                connectionStateProcessor.onNext(ConnectionEvent.create(new BluetoothReaderDevice(device), ConnectionState.READY));
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
            public void onMtuSizeChanged(@NonNull final BluetoothDevice device, final int mtuSize) {
                Timber.d("onMtuSizeChanged: device [%s], mtu: [%d]", device.getAddress(), mtuSize);
                mtuSizeProcessor.onNext(mtuSize);
            }

            @Override
            public void onWorkflowStateChanged(@NonNull final WorkflowStateResponse workflowStateResponse) {
                Timber.d("onWorkflowStateChanged: %s", workflowStateResponse);
                workflowStateProcessor.onNext(((WorkflowStateMapper) mappers.get(WorkflowStateResponse.class)).mapToDomain(workflowStateResponse));
            }

            @Override
            public void onTestProgressChanged(@NonNull final TestProgressResponse testProgressResponse) {
                Timber.d("onTestProgressChanged: %s", testProgressResponse);
                testProgressProcessor.onNext(((TestProgressMapper) mappers.get(TestProgressResponse.class)).mapToDomain(testProgressResponse));
            }

            @Override
            public void onBatteryLevelChanged(@NonNull final BluetoothDevice device, final int batteryLevel) {
                Timber.d("onBatteryLevelChanged: device [%s], level: [%d]", device.getAddress(), batteryLevel);
                batteryLevelProcessor.onNext(batteryLevel);
            }
        });
    }

    @Override
    public Completable connect(@NonNull final ReaderDevice readerDevice) {
        if (readerDevice instanceof BluetoothReaderDevice) {
            return lumosReaderManager.connectCompletable(((BluetoothReaderDevice) readerDevice).getBluetoothDevice())
                    .andThen(connectionStateProcessor.filter(event -> event.getConnectionState() == ConnectionState.READY)
                            .take(1)
                            .singleOrError()
                            .ignoreElement()
                    );
        } else {
            Timber.e("Unhandled ReaderDevice implementation received!");
            return Completable.complete();
        }
    }

    @Override
    public Completable disconnect() {
        return lumosReaderManager.disconnectCompletable();
    }

    @Override
    public Completable changeMtu(final int mtuSize) {
        return lumosReaderManager.changeMtu(mtuSize);
    }

    @Override
    public Single<Integer> getBatteryLevel() {
        Single<Integer> integerSingle = withRetry(1, lumosReaderManager.readCharacteristic(LumosReaderConstants.BATTERY_CHAR_LEVEL), "getBatteryLevel");
        return integerSingle.onErrorReturnItem(-1);
    }

    @Override
    public Flowable<Integer> batteryLevel() {
        return batteryLevelProcessor.startWith(
                getBatteryLevel()
                        .toFlowable()
        );
    }

    private <T> Single<T> withRetry(final int times, final Single<T> single, final String debugHelper) {
        return single.retryWhen(errors -> {
                    final AtomicInteger atomicInteger = new AtomicInteger(0);
                    return errors
                            .doOnNext(e -> Timber.d("--- withRetry [%s] [%d] error received: [%s]", debugHelper, times, e))
                            .takeWhile(e -> atomicInteger.getAndIncrement() < times)
                            .flatMap(e -> {
                                Timber.d("--- withRetry [%s] will retry [%d/%d] after delay...", debugHelper, atomicInteger.get(), times);
                                return Flowable.timer(300L, TimeUnit.MILLISECONDS).doOnNext(ignored -> Timber.d("--- withRetry delay done!"));
                            });
                }
        );
    }

    @Override
    public Single<CartridgeInfo> getCartridgeInfo() {
        return withRetry(1,
                lumosReaderManager.<CartridgeIdResponse>readCharacteristic(LumosReaderConstants.READER_CHAR_CARTRIDGE_ID)
                        .map(cartridgeIdResponse -> ((CartridgeInfoMapper) mappers.get(CartridgeIdResponse.class)).mapToDomain(cartridgeIdResponse)),
                "getCartridgeInfo"
        );
    }

    @Override
    public Single<Integer> getNumberOfResults() {
        return withRetry(1, lumosReaderManager.<NumPreviousResultsResponse>readCharacteristic(LumosReaderConstants.READER_CHAR_NUM_RESULTS)
                .map(NumPreviousResultsResponse::getNumberOfResults),
                "getNumberOfResults"
        );
    }

    @Override
    public Single<TestResult> getResult(@NonNull final String id) {
        return withRetry(1,
                lumosReaderManager.getResult(id)
                        .map(resultResponse -> ((TestResultMapper) mappers.get(ResultResponse.class)).mapToDomain(resultResponse)),
                "getResult"
        );
    }

    @Override
    public Single<Error> getError() {
        return withRetry(1,
                lumosReaderManager.<ErrorResponse>readCharacteristic(LumosReaderConstants.READER_CHAR_ERROR)
                        .map(errorResponse -> ((ErrorMapper) mappers.get(ErrorResponse.class)).mapToDomain(errorResponse)),
                "getError"
        );
    }

    @Override
    public Maybe<ReaderDevice> getConnectedDevice() {
        return lumosReaderManager.getConnectedDevice();
    }

    @Override
    public Single<List<TestResult>> syncAllResults() {
        return syncResults(null);
    }

    @Override
    public Single<List<TestResult>> syncResultsAfter(final @NonNull String lastResultId) {
        return syncResults(lastResultId);
    }

    @Override
    public Flowable<TestResult> syncAllResultsFlowable() {
        return syncResultFlowable(null);
    }

    @Override
    public Flowable<TestResult> syncResultsAfterFlowable(@NonNull String lastResultId) {
        return syncResultFlowable(lastResultId);
    }

    private Single<List<TestResult>> syncResults(final @Nullable String lastResultId) {
        return lumosReaderManager.getConnectedDevice()
                .toSingle()
                .map(ReaderDevice::getMac)
                .flatMap(deviceId -> {
                            final Single<List<ResultResponse>> single = TextUtils.isEmpty(lastResultId) ? lumosReaderManager.syncAllResults() : lumosReaderManager.syncResultsAfter(lastResultId);
                            return single.map(list -> {
                                        final List<TestResult> mapped = new ArrayList<>();
                                        final Mapper<TestResult, ResultResponse> mapper = (TestResultMapper) mappers.get(ResultResponse.class);

                                        for (ResultResponse resultResponse : list) {
                                            mapped.add(mapper.mapToDomain(resultResponse)
                                                    .toBuilder()
                                                    .setReaderMac(deviceId)
                                                    .build());
                                        }

                                        return mapped;
                                    }
                            );
                        }
                );
    }

    private Flowable<TestResult> syncResultFlowable(final @Nullable String lastResultId) {
        final Mapper<TestResult, ResultResponse> mapper = (TestResultMapper) mappers.get(ResultResponse.class);
        return lumosReaderManager.getConnectedDevice()
                .toFlowable()
                .map(ReaderDevice::getMac)
                .flatMap(deviceId -> {
                    final Flowable<ResultResponse> flowable = TextUtils.isEmpty(lastResultId) ? lumosReaderManager.syncAllResultsFlowable() : lumosReaderManager.syncResultsAfterFlowable(lastResultId);
                    return flowable.map(mapper::mapToDomain).map(mod -> mod.toBuilder().setReaderMac(deviceId).build());
                });
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
        return Flowable.concat(
                connectionStateProcessor.filter(event -> event.getConnectionState() == ConnectionState.READY)
                        .take(1)
                        .flatMap(ignored -> getWorkflowState().toFlowable()),
                workflowStateProcessor
        );
    }

    private Single<WorkflowState> getWorkflowState() {
        return withRetry(1,
                lumosReaderManager.<WorkflowStateResponse>readCharacteristic(LumosReaderConstants.READER_CHAR_WORKFLOW_STATE)
                        .map(workflowStateResponse -> ((WorkflowStateMapper) mappers.get(WorkflowStateResponse.class)).mapToDomain(workflowStateResponse)),
                "getWorkflowState"
        );
    }

    @Override
    public Flowable<TestProgress> testProgress() {
        return testProgressProcessor;
    }

    @Override
    public Flowable<SyncProgress> getSyncProgressFlowable() {
        return lumosReaderManager.getSyncProgress();
    }
}
