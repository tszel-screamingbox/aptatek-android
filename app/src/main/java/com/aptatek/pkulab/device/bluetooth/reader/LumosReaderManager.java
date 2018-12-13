package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.characteristics.CharacteristicsHolder;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.CharacteristicReader;
import com.aptatek.pkulab.device.bluetooth.characteristics.writer.JsonCharacteristicWriter;
import com.aptatek.pkulab.device.bluetooth.error.CharacteristicReadError;
import com.aptatek.pkulab.device.bluetooth.error.FailedToBondError;
import com.aptatek.pkulab.device.bluetooth.error.FailedToConnectError;
import com.aptatek.pkulab.device.bluetooth.error.FailedToDisconnectError;
import com.aptatek.pkulab.device.bluetooth.error.NoValueReceivedError;
import com.aptatek.pkulab.device.bluetooth.model.RequestResultRequest;
import com.aptatek.pkulab.device.bluetooth.model.ResultResponse;
import com.aptatek.pkulab.device.bluetooth.model.ResultSyncResponse;
import com.aptatek.pkulab.device.bluetooth.model.UpdateTimeResponse;
import com.aptatek.pkulab.device.bluetooth.model.WorkflowStateResponse;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.BooleanSupplier;
import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.ble.exception.BluetoothDisabledException;
import no.nordicsemi.android.ble.exception.DeviceDisconnectedException;
import no.nordicsemi.android.ble.exception.InvalidRequestException;
import no.nordicsemi.android.ble.exception.RequestFailedException;
import no.nordicsemi.android.ble.response.ReadResponse;
import timber.log.Timber;

public class LumosReaderManager extends BleManager<LumosReaderCallbacks> {

    private final CharacteristicsHolder characteristicsHolder;
    private final BleManagerGattCallback bleGattCallback;
    private final Map<String, CharacteristicReader> characteristicReaderMap;
    private final JsonCharacteristicWriter jsonCharacteristicWriter;

    @Inject
    LumosReaderManager(@ApplicationContext @NonNull final Context context,
                       final CharacteristicsHolder characteristicsHolder,
                       final Map<String, CharacteristicReader> characteristicReaderMap,
                       final JsonCharacteristicWriter jsonCharacteristicWriter) {
        super(context);

        this.characteristicsHolder = characteristicsHolder;
        this.characteristicReaderMap = characteristicReaderMap;
        this.jsonCharacteristicWriter = jsonCharacteristicWriter;
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
                updateTime();

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

    private UpdateTimeResponse createTimeResponse() {
        final UpdateTimeResponse updateTimeResponse = new UpdateTimeResponse();
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        updateTimeResponse.setYear(calendar.get(Calendar.YEAR));
        updateTimeResponse.setMonth(calendar.get(Calendar.MONTH));
        updateTimeResponse.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        updateTimeResponse.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        updateTimeResponse.setMinute(calendar.get(Calendar.MINUTE));
        updateTimeResponse.setSecond(calendar.get(Calendar.SECOND));

        return updateTimeResponse;
    }

    private RequestResultRequest createRequestResultRequest(@NonNull final String id) {
        final RequestResultRequest requestResultRequest = new RequestResultRequest();
        requestResultRequest.setResultId(id);

        return requestResultRequest;
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

    public void queueMtuChange(final int mtu) {
        requestMtu(mtu)
                .with(((device, data) -> mCallbacks.onMtuSizeChanged(device, data)))
                .done(device ->
                        Timber.d("Mtu change successful")
                ).fail((device, status) -> {
            Timber.d("Mtu change failed: status [%d]", status);
            mCallbacks.onError(device, "Failed to change MTU", LumosReaderConstants.ERROR_MTU_CHANGE_FAILED);
        })
                .enqueue();
    }

    private void updateTime() {
        writeCharacteristic(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_UPDATE_TIME), jsonCharacteristicWriter.convertData(createTimeResponse()))
                .done(device -> Timber.d("Update time write successful"))
                .fail((device, aStatus) -> Timber.d("Failed to write time: status [%d]", aStatus))
                .enqueue();

    }

    public Single<ResultResponse> getResult(@NonNull String id) {
        return Completable.fromAction(() -> {
                    writeCharacteristic(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_REQUEST_RESULT), jsonCharacteristicWriter.convertData(createRequestResultRequest(id)))
                            .await();
                    Timber.d("Successfully written Result Request: id [%s]", id);
                }
        ).andThen(readCharacteristic(LumosReaderConstants.READER_CHAR_RESULT));
    }

    public Single<List<ResultResponse>> syncResults() {
        return Completable.fromAction(() -> {
                    writeCharacteristic(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_RESULT_SYNC_REQUEST), new byte[]{0x0})
                            .await();
                    Timber.d("Successfully written Sync Request");
                }
        ).andThen(concatSyncResponse()
                .map(ResultSyncResponse::getIdentifiers)
                .toFlowable()
                .flatMapIterable(it -> it)
                .flatMapSingle(this::getResult)
                .toList()
        );
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
                .repeatWhen(objectFlowable -> objectFlowable
                        .doOnNext(a -> Timber.d("repeatWhen next: [%s]", a.toString()))
                        .flatMapSingle(it -> ((Integer) it) > 100 ? Single.just(it): Single.never())
                )
                .scan((current, next) -> current + next)
                .singleOrError()
                .map(rawString -> (ResultSyncResponse) characteristicReaderMap.get(LumosReaderConstants.READER_CHAR_RESULT_SYNC_RESPONSE).read(Data.from(rawString)));
    }
}
