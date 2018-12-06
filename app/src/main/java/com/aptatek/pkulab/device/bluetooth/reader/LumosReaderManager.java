package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.model.UpdateTimeResponse;
import com.aptatek.pkulab.device.bluetooth.parser.CartridgeIdReader;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.data.Data;
import timber.log.Timber;

public class LumosReaderManager extends BleManager<LumosReaderCallbacks> {

    private final CharacteristicsHolder characteristicsHolder;
    private final BleManagerGattCallback bleGattCallback;
    private final Map<String, CharacteristicReader> characteristicReaderMap;
    private final CharacteristicWriter characteristicWriter;

    @Inject
    public LumosReaderManager(@ApplicationContext @NonNull final Context context,
                              final CharacteristicsHolder characteristicsHolder,
                              final Map<String, CharacteristicReader> characteristicReaderMap,
                              final CharacteristicWriter characteristicWriter) {
        super(context);

        this.characteristicsHolder = characteristicsHolder;
        this.characteristicReaderMap = characteristicReaderMap;
        this.characteristicWriter = characteristicWriter;
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
                        .fail((device, status) -> {
                            Timber.d("Failed to bond: status [%d]", status);
                            queueDisconnect();
                        })
                        .enqueue();

            }

        };
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
    protected void onPairingRequestReceived(final BluetoothDevice device, final int variant) {
        super.onPairingRequestReceived(device, variant);

        Timber.d("onPairingRequestReceived: device [%s], variant [%s]", device.getAddress(), pairingVariantToString(variant));
    }

    public void queueCartridgeId() {
        readCharacteristic(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_CARTRIDGE_ID))
                .with((device, data) -> {
                    final CartridgeIdReader characteristicReader = (CartridgeIdReader) characteristicReaderMap.get(LumosReaderConstants.READER_CHAR_CARTRIDGE_ID);
                    mCallbacks.onReadCartridgeId(characteristicReader.parseFromString(data.getStringValue(0)));
                })
                .done(device -> Timber.d("CartridgeId read successful"))
                .fail((device, status) -> Timber.d("CartridgeId read failed: status [%d]", status))
                .enqueue();
    }

    public void queueBatteryRead() {
        readCharacteristic(characteristicsHolder.getCharacteristic(LumosReaderConstants.BATTERY_CHAR_LEVEL))
                .with((device, data) -> {
                    Timber.d("Battery level received: [%s]", data);
                    mCallbacks.onReadBatteryLevel(data.getIntValue(Data.FORMAT_UINT8, 0));
                })
                .done(device -> Timber.d("Battery level read successful"))
                .fail((device, status) -> Timber.d("Battery level read failed: status [%d]", status))
                .enqueue();
    }

    public void queueMtuChange(final int mtu) {
//        requestMtu(mtu)
//                .with(((device, data) -> mCallbacks.onMtuSizeChanged(device, data)))
//                .done(device -> {
                    Timber.d("Mtu change successful");
                    writeCharacteristic(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_UPDATE_TIME), characteristicWriter.toBytes(createTimeResponse()))
                            .done(aDevice -> Timber.d("Update time write successful"))
                            .fail((aDevice, aStatus) -> Timber.d("Failed to write time: status [%d]", aStatus))
                            .enqueue();

                    writeCharacteristic(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_RESULT_SYNC_REQUEST),new byte[] {0x0})
                            .done(device -> {
                                Timber.d("Successfully written sync request on device: [%s]", device.getAddress());
                                final StringBuilder stringBuilder = new StringBuilder();
                                final Callback callback = response -> {
                                    Timber.d("SYNC RESPONSE: %s", response);
                                };

                                readSyncResponse(callback, stringBuilder);
                            })
                            .fail(((device, status) -> Timber.d("Failed to write sync request: status [%d]", status)))
                            .enqueue();
//                })
//                .fail((device, status) -> {
//                    Timber.d("Mtu change failed: status [%d]", status);
//                    mCallbacks.onError(device, "Failed to change MTU", LumosReaderConstants.ERROR_MTU_CHANGE_FAILED);
//                })
//                .enqueue();
    }

    private void readSyncResponse(final Callback callback, final StringBuilder stringBuilder) {
        readCharacteristic(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_RESULT_SYNC_RESPONSE))
                .with((device1, data) -> {
                    final String stringValue = data.getStringValue(0);
                    Timber.d("Successfully read sync response: [%s]", stringValue);
                    if (TextUtils.isEmpty(stringValue.trim())) {
                        Timber.d("STOP READING SYNC RESPONSE");
                        callback.onSyncResponseRead(stringBuilder.toString());
                    } else {
                        stringBuilder.append(stringValue);
                        readSyncResponse(callback, stringBuilder);

                    }
                })
                .fail((device1, status) -> Timber.d("Failed to write sync request: status [%d]", status))
                .enqueue();
    }

    private interface Callback {
        void onSyncResponseRead(String response);
    }

    public void queueConnect(@NonNull final BluetoothDevice bluetoothDevice) {
        connect(bluetoothDevice)
                .useAutoConnect(true)
                .done(device -> {
                    Timber.d("Connect successful");
                    createBond()
                            .done(aDevice -> Timber.d("Successfully bounded"))
                            .fail((aDevice, status) -> {
                                Timber.d("Failed to bound: status [%d]", status);
                                disconnect().enqueue();
                            })
                            .enqueue();
                })
                .fail((device, status) -> Timber.d("Failed to connect: status [%d]", status))
                .enqueue();
    }

    public void queueDisconnect() {
        disconnect()
                .done(device -> Timber.d("Disconnect successful"))
                .fail((device, status) -> Timber.d("Failed to disconnect: status [%d]", status))
                .enqueue();
    }
}
