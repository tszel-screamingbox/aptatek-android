package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aptatek.pkulab.device.bluetooth.LumosReaderConstants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import ix.Ix;

public class CharacteristicsHolder {

    // READER UUIDs
    private static final UUID READER_SERVICE_UUID = UUID.fromString(LumosReaderConstants.READER_SERVICE);
    private static final UUID READER_CHAR_WORKFLOW_STATE_UUID = UUID.fromString(LumosReaderConstants.READER_CHAR_WORKFLOW_STATE);
    private static final UUID READER_CHAR_RESULT_UUID = UUID.fromString(LumosReaderConstants.READER_CHAR_RESULT);
    private static final UUID READER_CHAR_NUM_RESULTS_UUID = UUID.fromString(LumosReaderConstants.READER_CHAR_NUM_RESULTS);
    private static final UUID READER_CHAR_REQUEST_RESULT_UUID = UUID.fromString(LumosReaderConstants.READER_CHAR_REQUEST_RESULT);
    private static final UUID READER_CHAR_CARTRIDGE_ID_UUID = UUID.fromString(LumosReaderConstants.READER_CHAR_CARTRIDGE_ID);
    private static final UUID READER_CHAR_ERROR_UUID = UUID.fromString(LumosReaderConstants.READER_CHAR_ERROR);
    private static final UUID READER_CHAR_UPDATE_ASSAY_DETAILS_UUID = UUID.fromString(LumosReaderConstants.READER_CHAR_UPDATE_ASSAY_DETAILS);

    private final Set<BluetoothGattCharacteristic> characteristics = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> mandatoryCharacteristicIds;

    @Inject
    public CharacteristicsHolder() {
        final Set<String> mandatoryChars = new HashSet<>();
        mandatoryChars.add(LumosReaderConstants.READER_CHAR_WORKFLOW_STATE);
        mandatoryChars.add(LumosReaderConstants.READER_CHAR_RESULT);
        mandatoryChars.add(LumosReaderConstants.READER_CHAR_NUM_RESULTS);
        mandatoryChars.add(LumosReaderConstants.READER_CHAR_REQUEST_RESULT);
        mandatoryChars.add(LumosReaderConstants.READER_CHAR_ERROR);
        mandatoryChars.add(LumosReaderConstants.READER_CHAR_CARTRIDGE_ID);
        mandatoryChars.add(LumosReaderConstants.READER_CHAR_UPDATE_ASSAY_DETAILS);

        mandatoryCharacteristicIds = Collections.unmodifiableSet(mandatoryChars);
    }

    public void collectCharacteristics(@NonNull final BluetoothGatt gatt) {
        clear();

        final BluetoothGattService readerService = gatt.getService(READER_SERVICE_UUID);
        if (readerService != null) {
            characteristics.add(readerService.getCharacteristic(READER_CHAR_WORKFLOW_STATE_UUID));
            characteristics.add(readerService.getCharacteristic(READER_CHAR_RESULT_UUID));
            characteristics.add(readerService.getCharacteristic(READER_CHAR_NUM_RESULTS_UUID));
            characteristics.add(readerService.getCharacteristic(READER_CHAR_REQUEST_RESULT_UUID));
            characteristics.add(readerService.getCharacteristic(READER_CHAR_CARTRIDGE_ID_UUID));
            characteristics.add(readerService.getCharacteristic(READER_CHAR_ERROR_UUID));
            characteristics.add(readerService.getCharacteristic(READER_CHAR_UPDATE_ASSAY_DETAILS_UUID));
        }

        // TODO parse other services' characteristics

    }

    public boolean hasAllMandatoryCharacteristics() {
        return Ix.from(mandatoryCharacteristicIds)
                .filter(charId -> getById(charId) == null)
                .count()
                .single() == 0;
    }

    @NonNull
    public BluetoothGattCharacteristic getCharacteristic(@NonNull final String id) {
        final BluetoothGattCharacteristic byId = getById(id);
        if (byId == null) {
            throw new IllegalArgumentException("Characteristic not found: " + id);
        }

        return byId;
    }

    public void clear() {
        characteristics.clear();
    }

    @Nullable
    private BluetoothGattCharacteristic getById(@NonNull final String id) {
        return Ix.from(characteristics)
                .filter(characteristic -> characteristic.getUuid().equals(UUID.fromString(id)))
                .single(null);
    }

}
