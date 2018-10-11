package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.model.CartridgeIdResponse;
import com.aptatek.pkulab.device.bluetooth.model.UpdateTimeResponse;
import com.aptatek.pkulab.device.bluetooth.parser.CartridgeIdReader;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import java.util.Calendar;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.Request;
import timber.log.Timber;

public class LumosReaderManager extends BleManager<LumosReaderCallbacks> {

    private final CharacteristicsHolder characteristicsHolder;
    private final BleManagerGattCallback bleGattCallback;

    @Inject
    public LumosReaderManager(@ApplicationContext @NonNull final Context context,
                              final CharacteristicsHolder characteristicsHolder,
                              final Map<String, CharacteristicReader> characteristicReaderMap,
                              final CharacteristicWriter characteristicWriter) {
        super(context);

        this.characteristicsHolder = characteristicsHolder;
        bleGattCallback = new BleManagerGattCallback() {

            @Override
            protected Deque<Request> initGatt(final BluetoothGatt gatt) {
                final LinkedList<Request> requests = new LinkedList<>();
                requests.push(Request.createBond());
                return requests;
            }

            @Override
            public boolean isRequiredServiceSupported(final BluetoothGatt gatt) {
                characteristicsHolder.collectCharacteristics(gatt);

                return characteristicsHolder.hasAllMandatoryCharacteristics();
            }

            @Override
            protected void onDeviceDisconnected() {
                characteristicsHolder.clear();
            }

            @Override
            protected void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
                final String charId = characteristic.getUuid().toString();
                Timber.d("Reading characteristic [%s]", charId);

                switch (charId) {
                    case LumosReaderConstants.READER_CHAR_WORKFLOW_STATE: {
                        // TODO parse and callback

                        break;
                    }
                    case LumosReaderConstants.READER_CHAR_ERROR: {
                        // TODO parse and callback

                        break;
                    }
                    case LumosReaderConstants.READER_CHAR_RESULT: {
                        // TODO parse and callback

                        break;
                    }
                    case LumosReaderConstants.READER_CHAR_NUM_RESULTS: {
                        // TODO parse and callback

                        break;
                    }
                    case LumosReaderConstants.READER_CHAR_REQUEST_RESULT: {
                        // TODO parse and callback

                        break;
                    }
                    case LumosReaderConstants.READER_CHAR_CARTRIDGE_ID: {
                        final CartridgeIdReader cartridgeIdReader = (CartridgeIdReader) characteristicReaderMap.get(charId);
                        final CartridgeIdResponse cartridgeIdResponse = cartridgeIdReader.readValue(characteristic);

                        mCallbacks.onReadCartridgeId(cartridgeIdResponse);

                        break;
                    }
                    case LumosReaderConstants.READER_CHAR_UPDATE_ASSAY_DETAILS: {
                        // TODO parse and callback

                        break;
                    }
                    default: {
                        Timber.d("Unhandled READ characteristic: [%s]", charId);

                        break;
                    }
                }
            }

            @Override
            public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
                Timber.d("onCharacteristicWrite: [%s]", characteristic.getUuid().toString());
            }

            @Override
            public void onCharacteristicNotified(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
                final String charId = characteristic.getUuid().toString();
                Timber.d("Notified characteristic [%s]", charId);

                switch (charId) {
                    case LumosReaderConstants.READER_CHAR_WORKFLOW_STATE: {

                        break;
                    }
                    case LumosReaderConstants.READER_CHAR_ERROR: {

                        break;
                    }
                    default: {
                        Timber.d("Unhandled NOTIFY characteristic: [%s]", charId);
                        break;
                    }
                }
            }

            @Override
            public void onMtuChanged(final BluetoothGatt gatt, final int mtu, final int status) {
                super.onMtuChanged(gatt, mtu, status);

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Timber.d("Successfully changed MTU size to [%d] on device [%s]", mtu, gatt.getDevice().getAddress());

                    final BluetoothGattCharacteristic timeChar = characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_UPDATE_TIME);
                    enqueue(Request.newWriteRequest(timeChar, characteristicWriter.toBytes(createTimeResponse())));

                    final BluetoothGattCharacteristic workflowChar = characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_WORKFLOW_STATE);
                    enqueue(Request.newReadRequest(workflowChar));
                    enqueue(Request.newEnableNotificationsRequest(workflowChar));

                    final BluetoothGattCharacteristic errorChar = characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_ERROR);
                    enqueue(Request.newReadRequest(errorChar));
                    enqueue(Request.newEnableNotificationsRequest(errorChar));

                } else {
                    Timber.d("Failed to set MTU size to [%d] on device [%s], status [%d]", mtu, gatt.getDevice().getAddress(), status);

                    mCallbacks.onError(gatt.getDevice(), "Failed to set MTU", LumosReaderConstants.ERROR_MTU_CHANGE_FAILED);
                }
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
    protected boolean shouldAutoConnect() {
        return true;
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

    public void getCartridgeId() {
        readCharacteristic(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_CARTRIDGE_ID));
    }

    public void requestMtuChange() {
        if (getMtu() != LumosReaderConstants.MTU_SIZE) {
            enqueue(Request.newMtuRequest(LumosReaderConstants.MTU_SIZE));
        }
    }
}
