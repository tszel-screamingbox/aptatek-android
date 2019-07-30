package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import no.nordicsemi.android.ble.data.Data;

public class FirmwareVersionReader implements CharacteristicReader<String> {

    @Inject
    public FirmwareVersionReader() {
    }

    @Override
    public String read(@NonNull Data data) {
        return data.getStringValue(0);
    }

}
