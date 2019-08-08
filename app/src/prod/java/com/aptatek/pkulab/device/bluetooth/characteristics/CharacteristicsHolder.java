package com.aptatek.pkulab.device.bluetooth.characteristics;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    private static final UUID READER_CHAR_UPDATE_TIME_UUID = UUID.fromString(LumosReaderConstants.READER_CHAR_UPDATE_TIME);
    private static final UUID READER_CHAR_SYNC_REQUEST = UUID.fromString(LumosReaderConstants.READER_CHAR_RESULT_SYNC_REQUEST);
    private static final UUID READER_CHAR_SYNC_RESPONSE = UUID.fromString(LumosReaderConstants.READER_CHAR_RESULT_SYNC_RESPONSE);
    private static final UUID READER_CHAR_TEST_PROGRESS = UUID.fromString(LumosReaderConstants.READER_CHAR_TEST_PROGRESS);

    // Battery UUIDs
    private static final UUID BATTERY_SERVICE_UUID = UUID.fromString(LumosReaderConstants.BATTERY_SERVICE);
    private static final UUID BATTERY_CHAR_LEVEL_UUID = UUID.fromString(LumosReaderConstants.BATTERY_CHAR_LEVEL);

    // Device Info UUDs
    private static final UUID DEVICE_INFO_SERVICE_UUID = UUID.fromString(LumosReaderConstants.DEVICE_INFO_SERVICE);
    private static final UUID DEVICE_INFO_FIRMWARE_VERSION_UUID = UUID.fromString(LumosReaderConstants.DEVICE_INFO_FIRMWARE);
    private static final UUID DEVICE_INFO_SERIAL_NUMBER_UUID = UUID.fromString(LumosReaderConstants.DEVICE_INFO_SERIAL);

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
        mandatoryChars.add(LumosReaderConstants.READER_CHAR_UPDATE_TIME);
        mandatoryChars.add(LumosReaderConstants.READER_CHAR_RESULT_SYNC_REQUEST);
        mandatoryChars.add(LumosReaderConstants.READER_CHAR_RESULT_SYNC_RESPONSE);
        mandatoryChars.add(LumosReaderConstants.BATTERY_CHAR_LEVEL);
        mandatoryChars.add(LumosReaderConstants.READER_CHAR_TEST_PROGRESS);
        mandatoryChars.add(LumosReaderConstants.DEVICE_INFO_FIRMWARE);
        mandatoryChars.add(LumosReaderConstants.DEVICE_INFO_SERIAL);

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
            characteristics.add(readerService.getCharacteristic(READER_CHAR_SYNC_REQUEST));
            characteristics.add(readerService.getCharacteristic(READER_CHAR_SYNC_RESPONSE));
            characteristics.add(readerService.getCharacteristic(READER_CHAR_UPDATE_TIME_UUID));
            characteristics.add(readerService.getCharacteristic(READER_CHAR_TEST_PROGRESS));
        }

        final BluetoothGattService batteryService = gatt.getService(BATTERY_SERVICE_UUID);
        if (batteryService != null) {
            characteristics.add(batteryService.getCharacteristic(BATTERY_CHAR_LEVEL_UUID));
        }

        final BluetoothGattService deviceInfoService = gatt.getService(DEVICE_INFO_SERVICE_UUID);
        if (deviceInfoService != null) {
            characteristics.add(deviceInfoService.getCharacteristic(DEVICE_INFO_FIRMWARE_VERSION_UUID));
            characteristics.add(deviceInfoService.getCharacteristic(DEVICE_INFO_SERIAL_NUMBER_UUID));
        }
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
