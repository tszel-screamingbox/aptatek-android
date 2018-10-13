package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.model.CartridgeIdResponse;

import no.nordicsemi.android.ble.BleManagerCallbacks;

public interface LumosReaderCallbacks extends BleManagerCallbacks {

    void onReadCartridgeId(@NonNull CartridgeIdResponse cartridgeIdResponse);

    void onMtuSizeChanged(@NonNull BluetoothDevice device, int mtuSize);

    // TODO extend with our custom callbacks

}
