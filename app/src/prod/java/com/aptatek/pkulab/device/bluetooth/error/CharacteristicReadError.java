package com.aptatek.pkulab.device.bluetooth.error;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;

public class CharacteristicReadError extends BluetoothError {

    private final int status;

    private final String characteristicId;

    public CharacteristicReadError(@NonNull final BluetoothDevice device, final int status, final String characteristicId) {
        super(device);
        this.status = status;
        this.characteristicId = characteristicId;
    }

    public int getStatus() {
        return status;
    }

    public String getCharacteristicId() {
        return characteristicId;
    }

    @Override
    public String toString() {
        return "CharacteristicReadError{" +
                "status=" + status +
                ", characteristicId='" + characteristicId + '\'' +
                '}';
    }
}
