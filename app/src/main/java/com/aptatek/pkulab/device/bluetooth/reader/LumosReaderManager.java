package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;
import com.aptatek.pkulab.device.bluetooth.model.CartridgeIdResponse;
import com.aptatek.pkulab.device.bluetooth.parser.CartridgeIdReader;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

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
                              final Map<String, CharacteristicReader> characteristicReaderMap) {
        super(context);

        this.characteristicsHolder = characteristicsHolder;
        bleGattCallback = new BleManagerGattCallback() {

            @Override
            protected Deque<Request> initGatt(final BluetoothGatt gatt) {
                final LinkedList<Request> requests = new LinkedList<>();
                final BluetoothGattCharacteristic workflowChar = characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_WORKFLOW_STATE);
                requests.push(Request.newReadRequest(workflowChar));
                requests.push(Request.newEnableNotificationsRequest(workflowChar));

                final BluetoothGattCharacteristic errorChar = characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_ERROR);
                requests.push(Request.newReadRequest(errorChar));
                requests.push(Request.newEnableNotificationsRequest(errorChar));

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
                // TODO implement WRITE characteristics
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
        };
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
    public void log(int level, @NonNull String message) {
        Timber.d(message);
    }

    public void getCartridgeId() {
        readCharacteristic(characteristicsHolder.getCharacteristic(LumosReaderConstants.READER_CHAR_CARTRIDGE_ID));
    }
}
