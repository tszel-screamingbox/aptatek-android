package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.characteristics.CharacteristicsHolder;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.CharacteristicReader;
import com.aptatek.pkulab.device.bluetooth.characteristics.reader.ResultReader;
import com.aptatek.pkulab.device.bluetooth.characteristics.writer.CharacteristicDataProvider;
import com.aptatek.pkulab.device.bluetooth.characteristics.writer.RequestResultCharacteristicDataProvider;
import com.aptatek.pkulab.device.bluetooth.characteristics.writer.SyncRequestCharacteristicDataProvider;
import com.aptatek.pkulab.device.bluetooth.error.CharacteristicReadError;
import com.aptatek.pkulab.device.bluetooth.error.CharacteristicWriteError;
import com.aptatek.pkulab.device.bluetooth.error.ChecksumError;
import com.aptatek.pkulab.device.bluetooth.error.NoValueReceivedError;
import com.aptatek.pkulab.device.bluetooth.model.BluetoothReaderDevice;
import com.aptatek.pkulab.device.bluetooth.model.ResultResponse;
import com.aptatek.pkulab.device.bluetooth.model.ResultSyncResponse;
import com.aptatek.pkulab.device.bluetooth.model.TestProgressResponse;
import com.aptatek.pkulab.device.bluetooth.model.WorkflowStateResponse;
import com.aptatek.pkulab.domain.error.MtuChangeFailedError;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;
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
                createBond()
                        .done(device -> Timber.d("Bonded to device: [%s]", device.getAddress()))
                        .fail((device, status) -> Timber.d("Failed to bond: device [%s], status [%d]", device.getAddress(), status))
                        .enqueue();

//                had to remove it because the latest firmware doesn't support MTU change requests in this direction
//                requestMtu(LumosReaderConstants.MTU_SIZE)
//                        .with(((device, data) -> mCallbacks.onMtuSizeChanged(device, data)))
//                        .done(device -> Timber.d("Mtu change successful"))
//                        .fail((device, status) -> {
//                            Timber.d("Mtu change failed: status [%d]", status);
//                            mCallbacks.onError(device, "Failed to change MTU", LumosReaderConstants.ERROR_MTU_CHANGE_FAILED);
//                        })
//                        .enqueue();

                // add 500ms after service discovery before doing anything.
                sleep(LumosReaderConstants.DELAY_AFTER_DISCOVERY_MS)
                        .done((device) -> Timber.d("Slept %d ms after service discovery", LumosReaderConstants.DELAY_AFTER_DISCOVERY_MS))
                        .fail((device, error) -> Timber.d("Failed to sleep device : %d", error))
                        .enqueue();

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

                // TEST PROGRESS
                setNotificationCallback(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_TEST_PROGRESS))
                        .with((device, data) -> {
                            Timber.d("Test progress update: device [%s], data [%s]", device.getAddress(), data.toString());
                            mCallbacks.onTestProgressChanged(((TestProgressResponse) characteristicReaderMap.get(LumosReaderConstants.READER_CHAR_TEST_PROGRESS).read(data)));
                        });
                enableNotifications(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_TEST_PROGRESS))
                        .fail((device, status) -> Timber.d("Failed to enable Test Progress notifications: device [%s], status [%d]", device.getAddress(), status))
                        .done(device -> Timber.d("Successfully enabled Test Progress notifications: device [%s]", device.getAddress()))
                        .enqueue();

                // BATTERY
                setNotificationCallback(characteristicsHolder.getCharacteristic(LumosReaderConstants.BATTERY_CHAR_LEVEL))
                        .with(((device, data) -> {
                            Timber.d("Battery level update: device [%s], data [%s]", device.getAddress(), data.toString());
                            mCallbacks.onBatteryLevelChanged(device, (int) characteristicReaderMap.get(LumosReaderConstants.BATTERY_CHAR_LEVEL).read(data));
                        }));
                enableNotifications(characteristicsHolder.getCharacteristic(LumosReaderConstants.BATTERY_CHAR_LEVEL))
                        .fail((device, status) -> Timber.d("Failed to enable Battery Level notifications: device [%s], status: [%d]", device.getAddress(), status))
                        .done(device -> Timber.d("Successfully enabled Battery Level notifications: device [%s]", device.getAddress()))
                        .enqueue();

                updateTime().subscribe(
                        () -> Timber.d("Time updated"),
                        Timber::e
                );
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
        // Timber.d("onLog: %d -> %s", level, message);
    }

    @Override
    protected void onPairingRequestReceived(final @NonNull BluetoothDevice device, final int variant) {
        super.onPairingRequestReceived(device, variant);

        Timber.d("onPairingRequestReceived: device [%s], variant [%s]", device.getAddress(), pairingVariantToString(variant));
    }

    private int getMTUPayloadLength() {
        return getMtu() - 3;
    }

    @SuppressWarnings("unchecked")
    <T> Single<T> readCharacteristic(@NonNull final String characteristicId, final boolean validateChecksum) {
        return Single.create(emitter ->
                readCharacteristic(characteristicsHolder.getCharacteristic(characteristicId))
                        .with(((device, data) -> {
                            final String rawString = data.getStringValue(0);
                            Timber.d("readCharacteristic -- %s -- doChecksum: %s, onData: device [%s], data [%s]", characteristicId, validateChecksum, device.getAddress(), rawString);

                            String payload = null;

                            if (validateChecksum) {

                                try {
                                    payload = validateChecksumAndReturnPayload(rawString);
                                } catch (final ChecksumError | Exception error) {
                                    if (!emitter.isDisposed()) {
                                        emitter.onError(new ChecksumError(device, -1, characteristicId, error.getMessage()));
                                    }
                                }

                            } else {
                                payload = rawString;
                            }

                            try {
                                final T reading = (T) characteristicReaderMap.get(characteristicId).read(Data.from(payload));
                                if (!emitter.isDisposed()) {
                                    emitter.onSuccess(reading);
                                }
                            } catch (final Exception ex) {
                                if (!emitter.isDisposed()) {
                                    emitter.onError(new CharacteristicReadError(device, -1, characteristicId));
                                }
                            }

                        }))
                        .fail((device, status) -> {
                            Timber.d("readCharacteristic -- %s -- error: device [%s], status: [%d]", characteristicId, device.getAddress(), status);
                            if (!emitter.isDisposed()) {
                                emitter.onError(new CharacteristicReadError(device, status, characteristicId));
                            }
                        })
                        .done(device -> {
                            Timber.d("readCharacteristic -- %s -- completed successfully", characteristicId);
                            emitter.tryOnError(new NoValueReceivedError(device));
                        })
                        .enqueue()
        );
    }

    private String validateChecksumAndReturnPayload(@NonNull final String rawMessage) throws Exception, ChecksumError {
        Timber.d("validateChecksum: rawMessage=%s", rawMessage);

        final int checkSumLineBreakIndex = rawMessage.lastIndexOf('\n');

        if (checkSumLineBreakIndex < 0) {
            throw new Exception();
        }

        final String checksum = rawMessage.substring(checkSumLineBreakIndex).trim();
        final int checksumDelimiterIndex = checksum.lastIndexOf(':');

        if (checksumDelimiterIndex < 0) {
            throw new Exception();
        }

        final long checksumValue = Long.decode(checksum.substring(checksumDelimiterIndex + 1));

        final String payload = rawMessage.substring(0, checkSumLineBreakIndex);
        Timber.d("validateChecksum checksumValue=%d, payload=%s", checksumValue, payload);
        final CRC32 crc32 = new CRC32();
        crc32.update(payload.getBytes());
        final long calculatedChecksum = crc32.getValue();

        if (calculatedChecksum != checksumValue) {
            Timber.d("validateChecksum checksum error! actual=%d vs expected=%d", calculatedChecksum, checksumValue);
            throw new ChecksumError(null, -1, null, String.format("Expected = %d, actual = %d, calculated on string = %s", checksumValue, calculatedChecksum, payload));
        }

        return payload;
    }

    <T> Single<T> readCharacteristic(@NonNull final String characteristicId) {
        return readCharacteristic(characteristicId, false);
    }

    Completable writeCharacteristic(@NonNull final String characteristicId, @Nullable final CharacteristicDataProvider.CharacteristicsData data) {
        return Completable.create(emitter ->
                writeCharacteristic(characteristicsHolder.getCharacteristic(characteristicId), characteristicDataProviderMap.get(characteristicId).provideData(data))
                        .fail(((device, status) -> {
                            Timber.d("writeCharacteristic error: device [%s], status [%d]", device.getAddress(), status);
                            if (!emitter.isDisposed()) {
                                emitter.onError(new CharacteristicWriteError(device, status, characteristicId));
                            }
                        }))
                        .done(device -> {
                            Timber.d("writeCharacteristic [%s] completed successfully, data written = [%s]", characteristicId, new String(characteristicDataProviderMap.get(characteristicId).provideData(data)));
                            if (!emitter.isDisposed()) {
                                emitter.onComplete();
                            }
                        })
                        .enqueue()
        );
    }

    public Completable connectCompletable(@NonNull final BluetoothDevice bluetoothDevice) {
        return Completable.fromAction(() ->
                connect(bluetoothDevice)
                        .useAutoConnect(false)
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
                                    if (!emitter.isDisposed()) {
                                        emitter.onComplete();
                                    }
                                }
                        ).fail((device, status) -> {
                    Timber.d("Mtu change failed: status [%d]", status);
                    mCallbacks.onError(device, "Failed to change MTU", LumosReaderConstants.ERROR_MTU_CHANGE_FAILED);
                    if (!emitter.isDisposed()) {
                        emitter.onError(new MtuChangeFailedError());
                    }
                })
                        .enqueue()
        );
    }

    private Completable updateTime() {
        return writeCharacteristic(LumosReaderConstants.READER_CHAR_UPDATE_TIME, null);
    }

    public Single<ResultResponse> getResult(@NonNull final String id) {
        return writeCharacteristic(LumosReaderConstants.READER_CHAR_REQUEST_RESULT, new RequestResultCharacteristicDataProvider.RequestResultData(id))
                .delay(LumosReaderConstants.DELAY_AFTER_DISCOVERY_MS, TimeUnit.MILLISECONDS) // give some time for reader to settle Result characteristic...
                .andThen(readResult()
                        .onErrorResumeNext(error -> {
                            Timber.d("onErrorResumeNext: %s", error);
                            if (error instanceof JsonParseException) {  // if the good old initial value is read from Result char, give it a chance to settle and try again...
                                Timber.d("Caught a JsonParseException, but no worries, will retry soon...");
                                return Flowable.timer(LumosReaderConstants.DELAY_AFTER_DISCOVERY_MS, TimeUnit.MILLISECONDS)
                                        .take(1)
                                        .singleOrError()
                                        .flatMap(ignored -> readResult());
                            }

                            return Single.error(error);
                        })
                        .repeat()
                        .takeUntil((Predicate<ResultResponse>) resultResponse -> resultResponse.getDate().equals(id)) // make sure we have the result we requested...
                )
                .lastOrError()
                .doOnSuccess(result -> Timber.d("Requested resultId: %s, result: %s", id, result));
    }

    private Single<ResultResponse> readResult() {
        return readLongPayloadWithChecksum(LumosReaderConstants.READER_CHAR_RESULT)
                .map(payload -> ((ResultReader) characteristicReaderMap.get(LumosReaderConstants.READER_CHAR_RESULT)).read(Data.from(payload)));
    }

    private Single<List<ResultResponse>> syncResults(@Nullable final SyncRequestCharacteristicDataProvider.SyncAfterRequestData data) {
        return writeCharacteristic(LumosReaderConstants.READER_CHAR_RESULT_SYNC_REQUEST, data)
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

    public Single<List<ResultResponse>> syncAllResults() {
        return syncResults(null);
    }

    public Single<List<ResultResponse>> syncResultsAfter(final @NonNull String lastResultId) {
        return syncResults(new SyncRequestCharacteristicDataProvider.SyncAfterRequestData(lastResultId));
    }

    private Single<ResultSyncResponse> concatSyncResponse() {
        return readLongPayloadWithChecksum(LumosReaderConstants.READER_CHAR_RESULT_SYNC_RESPONSE)
                .map(payload -> (ResultSyncResponse) characteristicReaderMap.get(LumosReaderConstants.READER_CHAR_RESULT_SYNC_RESPONSE).read(Data.from(payload)));
    }

    private Single<String> readLongPayloadWithChecksum(final String characteristicId) {
        return Single.<String>create(emitter ->
                readCharacteristic(characteristicsHolder.getCharacteristic(characteristicId))
                        .with((device, data) -> {
                            final String rawString = data.getStringValue(0);

                            if (!emitter.isDisposed()) {
                                Timber.d("readLongPayloadWithChecksum -- %s -- partial: [%s]", characteristicId, rawString);
                                emitter.onSuccess(rawString);
                            }
                        })
                        .fail((device, status) -> {
                            Timber.d("Failed to read partial response -- %s --: device [%s], status [%d]", characteristicId, device.getAddress(), status);

                            if (!emitter.isDisposed()) {
                                emitter.onError(new CharacteristicReadError(device, status, LumosReaderConstants.READER_CHAR_RESULT_SYNC_RESPONSE));
                            }
                        })
                        .done(device -> {
                            Timber.d("Done reading partial response -- %s -- : device [%s]", characteristicId, device.getAddress());

                            if (!emitter.isDisposed()) {
                                emitter.tryOnError(new NoValueReceivedError(device));
                            }
                        })
                        .enqueue())
                .repeatWhen(objectFlowable -> objectFlowable)
                .delay(300L, TimeUnit.MILLISECONDS)
                .takeUntil(s ->
                           s.length() < getMTUPayloadLength() || TextUtils.isEmpty(s.trim())
                )
                .scan((current, next) -> current + next)
                .lastOrError()
                .map(input -> {
                    try {
                        return this.validateChecksumAndReturnPayload(input);
                    } catch (ChecksumError e) {
                        throw new Exception(e.getMessage(), e);
                    }
                })
                .onErrorResumeNext(error -> Single.error(new ChecksumError(getBluetoothDevice(), -1, characteristicId, error.getCause() != null ? error.getCause().getMessage() : error.getMessage())));
    }

    public Maybe<ReaderDevice> getConnectedDevice() {
        return Maybe.<BluetoothDevice>create(emitter -> {
            final BluetoothDevice bluetoothDevice = getBluetoothDevice();
            if (bluetoothDevice != null && getConnectionState() == BluetoothProfile.STATE_CONNECTED) {
                emitter.onSuccess(bluetoothDevice);
            } else {
                emitter.onComplete();
            }
        }).flatMap(readerDevice -> Single.zip(
                readCharacteristic(LumosReaderConstants.DEVICE_INFO_FIRMWARE),
                readCharacteristic(LumosReaderConstants.DEVICE_INFO_SERIAL),
                (BiFunction<String, String, ReaderDevice>) (firmwareVersion, serialNumber) -> {
                    BluetoothReaderDevice bluetoothReaderDevice = new BluetoothReaderDevice(readerDevice);
                    bluetoothReaderDevice.setFirmwareVersion(firmwareVersion);
                    bluetoothReaderDevice.setSerialNumber(serialNumber);
                    return bluetoothReaderDevice;
                }
        ).toMaybe());
    }
}
