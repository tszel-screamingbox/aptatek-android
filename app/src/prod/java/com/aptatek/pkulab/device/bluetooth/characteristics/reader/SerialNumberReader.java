package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import no.nordicsemi.android.ble.data.Data;

public class SerialNumberReader implements CharacteristicReader<String> {

    @Inject
    public SerialNumberReader() {
    }

    @Override
    public String read(@NonNull Data data) {
        return data.getStringValue( 0);
    }
}
