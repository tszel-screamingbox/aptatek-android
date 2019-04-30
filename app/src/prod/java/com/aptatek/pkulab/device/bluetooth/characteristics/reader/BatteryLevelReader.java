package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import no.nordicsemi.android.ble.data.Data;

public class BatteryLevelReader implements CharacteristicReader<Integer> {

    @Inject
    public BatteryLevelReader() {
    }

    @Override
    public Integer read(@NonNull Data data) {
        return data.getIntValue(Data.FORMAT_UINT8, 0);
    }

}
