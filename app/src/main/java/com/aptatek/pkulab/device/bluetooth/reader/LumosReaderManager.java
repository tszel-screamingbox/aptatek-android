package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.characteristics.CharacteristicsHolder;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.CharacteristicReader;
import com.aptatek.pkulab.device.bluetooth.characteristics.writer.CharacteristicDataProvider;
import com.aptatek.pkulab.device.bluetooth.characteristics.writer.RequestResultCharacteristicDataProvider;
import com.aptatek.pkulab.device.bluetooth.error.CharacteristicReadError;
import com.aptatek.pkulab.device.bluetooth.error.CharacteristicWriteError;
import com.aptatek.pkulab.device.bluetooth.error.FailedToBondError;
import com.aptatek.pkulab.device.bluetooth.error.NoValueReceivedError;
import com.aptatek.pkulab.device.bluetooth.model.BluetoothReaderDevice;
import com.aptatek.pkulab.device.bluetooth.model.ResultResponse;
import com.aptatek.pkulab.device.bluetooth.model.ResultSyncResponse;
import com.aptatek.pkulab.device.bluetooth.model.WorkflowStateResponse;
import com.aptatek.pkulab.domain.error.MtuChangeFailedError;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.data.Data;
import timber.log.Timber;

public class LumosReaderManager extends BleManager<LumosReaderCallbacks> {

    private final CharacteristicsHolder characteristicsHolder;
    private final BleManagerGattCallback bleGattCallback;
    private final Map<String, CharacteristicReader> characteristicReaderMap;
    private final Map<String, CharacteristicDataProvider> characteristicDataProviderMap;

    @Inject
    LumosReaderManager(@ApplicationContext @NonNull final Context context,
                       final CharacteristicsHolder characteristicsHolder,
                       final Map<String, CharacteristicReader> characteristicReaderMap,
                       final Map<String, CharacteristicDataProvider> characteristicDataProviderMap) {
        super(context);

        this.characteristicsHolder = characteristicsHolder;
        this.characteristicReaderMap = characteristicReaderMap;
        this.characteristicDataProviderMap = characteristicDataProviderMap;
        bleGattCallback = new BleManagerGattCallback() {

            @Override
            public boolean isRequiredServiceSupported(@NonNull final BluetoothGatt gatt) {
                characteristicsHolder.collectCharacteristics(gatt);

                return characteristicsHolder.hasAllMandatoryCharacteristics();
            }

            @Override
            protected void onDeviceDisconnected() {
                characteristicsHolder.clear();
            }

            @Override
            protected void initialize() {
                updateTime().subscribe();

                // WORKFLOW STATE
                setNotificationCallback(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_WORKFLOW_STATE))
                        .with((device, data) -> {
                            Timber.d("Workflow state update: device [%s], data [%s]", device.getAddress(), data.toString());
                            mCallbacks.onWorkflowStateChanged((WorkflowStateResponse) characteristicReaderMap.get(LumosReaderConstants.READER_CHAR_WORKFLOW_STATE).read(data));
                        });
                enableNotifications(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_WORKFLOW_STATE))
                        .fail((device, status) -> Timber.d("Failed to enable Workflow State notifications: device [%s], status [%d]", device.getAddress(), status))
                        .done(device -> Timber.d("Successfully enabled Workflow State notifications: device [%s]", device.getAddress()))
                        .enqueue();
            }

        };
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return bleGattCallback;
    }

    @Override
    public void log(final int level, @NonNull final String message) {
        Timber.d(message);
    }

    @Override
    protected void onPairingRequestReceived(final @NonNull BluetoothDevice device, final int variant) {
        super.onPairingRequestReceived(device, variant);

        Timber.d("onPairingRequestReceived: device [%s], variant [%s]", device.getAddress(), pairingVariantToString(variant));
    }

    /**
     * We can suppress unchecked warning because it would be a dev error if we would have forgotten to fill up the readerMap with the proper values
     */
    @SuppressWarnings("unchecked")
    <T> Single<T> readCharacteristic(@NonNull final String characteristicId) {
        return Single.create(emitter ->
                readCharacteristic(characteristicsHolder.getCharacteristic(characteristicId))
                        .with(((device, data) -> {
                            Timber.d("readCharacteristic onData: device [%s], data [%s]", device.getAddress(), data.toString());
                            final T reading = (T) characteristicReaderMap.get(characteristicId).read(data);
                            emitter.onSuccess(reading);
                        }))
                        .fail((device, status) -> {
                            Timber.d("readCharacteristic error: device [%s], status: [%d]", device.getAddress(), status);
                            emitter.onError(new CharacteristicReadError(device, status));
                        })
                        .done(device -> {
                            Timber.d("readCharacteristic completed successfully");
                            emitter.tryOnError(new NoValueReceivedError(device));
                        })
                        .enqueue()
        );
    }

    Completable writeCharacteristic(@NonNull final String characteristicId, @Nullable final CharacteristicDataProvider.CharacteristicsData data) {
        return Completable.create(emitter ->
                writeCharacteristic(characteristicsHolder.getCharacteristic(characteristicId), characteristicDataProviderMap.get(characteristicId).provideData(data))
                        .fail(((device, status) -> {
                            Timber.d("writeCharacteristic error: device [%s], status [%d]", device.getAddress(), status);
                            emitter.onError(new CharacteristicWriteError(device, status));
                        }))
                        .done(device -> {
                            Timber.d("writeCharacteristic completed successfully");
                            emitter.onComplete();
                        })
                .enqueue()
        );
    }

    public Completable connectAndBound(@NonNull final BluetoothDevice bluetoothDevice) {
        return connectCompletable(bluetoothDevice)
                .andThen(bondCompletable());
    }

    private Completable bondCompletable() {
        return Completable.create(emitter ->
                createBond()
                        .done(device -> {
                            Timber.d("Bonded to device: [%s]", device.getAddress());
                            emitter.onComplete();
                        })
                        .fail((device, status) -> {
                            Timber.d("Failed to bond: device [%s], status [%d]", device.getAddress(), status);
                            emitter.onError(new FailedToBondError(device, status));
                        })
                        .enqueue()
        );
    }

    private Completable connectCompletable(@NonNull final BluetoothDevice bluetoothDevice) {
        return Completable.fromAction(() ->
                connect(bluetoothDevice)
                        .useAutoConnect(true)
                        .await()
        );
    }

    public Completable disconnectCompletable() {
        return Completable.fromAction(() ->
                disconnect().await()
        );
    }

    public Completable changeMtu(final int mtu) {
        return Completable.create(emitter ->
                requestMtu(mtu)
                        .with(((device, data) -> mCallbacks.onMtuSizeChanged(device, data)))
                        .done(device -> {
                                    Timber.d("Mtu change successful");
                                    emitter.onComplete();
                                }
                        ).fail((device, status) -> {
                            Timber.d("Mtu change failed: status [%d]", status);
                            mCallbacks.onError(device, "Failed to change MTU", LumosReaderConstants.ERROR_MTU_CHANGE_FAILED);
                            emitter.onError(new MtuChangeFailedError());
                })
                .enqueue()
        );
    }

    private Completable updateTime() {
        return writeCharacteristic(LumosReaderConstants.READER_CHAR_UPDATE_TIME, null);
    }

    public Single<ResultResponse> getResult(@NonNull final String id) {
        return writeCharacteristic(LumosReaderConstants.READER_CHAR_REQUEST_RESULT, new RequestResultCharacteristicDataProvider.RequestResultData(id))
                .andThen(readCharacteristic(LumosReaderConstants.READER_CHAR_RESULT));
    }

    public Single<List<ResultResponse>> syncResults() {
        return writeCharacteristic(LumosReaderConstants.READER_CHAR_RESULT_SYNC_REQUEST, null)
                .andThen(concatSyncResponse()
                        .map(ResultSyncResponse::getIdentifiers)
                        .toFlowable()
                        .flatMapIterable(it -> it)
                )
                .observeOn(Schedulers.io())
                .scan(new ArrayList<>(), (BiFunction<List<ResultResponse>, String, List<ResultResponse>>) (prevResults, identifier) -> {
                    prevResults.add(getResult(identifier).blockingGet());
                    return prevResults;
                })
                .lastOrError();
    }

    private Single<ResultSyncResponse> concatSyncResponse() {
        return Single.<String>create(emitter ->
                readCharacteristic(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_RESULT_SYNC_RESPONSE))
                        .with((device, data) -> {
                            final String stringValue = data.getStringValue(0);
                            Timber.d("Successfully read sync response: [%s]", stringValue);

                            emitter.onSuccess(stringValue);
                        })
                        .fail((device, status) -> {
                            Timber.d("Failed to read sync response: device [%s], status [%d]", device.getAddress(), status);

                            emitter.onError(new CharacteristicReadError(device, status));
                        })
                        .done(device -> {
                            Timber.d("Done reading sync response: device [%s]", device.getAddress());

                            emitter.tryOnError(new NoValueReceivedError(device));
                        })
                        .enqueue())
                .repeatWhen(objectFlowable -> objectFlowable)
                .takeUntil(s -> {
                    return TextUtils.isEmpty(s.trim());
                })
                .scan((current, next) -> current + next)
                .lastOrError()
                .map(rawString -> (ResultSyncResponse) characteristicReaderMap.get(LumosReaderConstants.READER_CHAR_RESULT_SYNC_RESPONSE).read(Data.from(rawString)));
    }

    public Maybe<ReaderDevice> getConnectedDevice() {
        final BluetoothDevice bluetoothDevice = getBluetoothDevice();
        if (bluetoothDevice == null) {
            return Maybe.never();
        }

        return Maybe.just(new BluetoothReaderDevice(bluetoothDevice));
    }
}