package com.aptatek.pkulab.device.bluetooth.characteristics.reader;

import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.data.Data;

public interface CharacteristicReader<T> {

    T read(@NonNull Data data);

}
