package com.aptatek.pkuapp.device.bluetooth;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.device.bluetooth.model.CartridgeIdResponse;

import no.nordicsemi.android.ble.BleManagerCallbacks;

public interface LumosReaderCallbacks extends BleManagerCallbacks {

    void onReadCartridgeId(@NonNull CartridgeIdResponse cartridgeIdResponse);

    // TODO extend with our custom callbacks

}
