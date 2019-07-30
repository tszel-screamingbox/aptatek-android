package com.aptatek.pkulab.device.bluetooth.model;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.aptatek.pkulab.domain.model.reader.ReaderDevice;

import java.util.Objects;

public class BluetoothReaderDevice implements ReaderDevice {

    private final BluetoothDevice bluetoothDevice;
    private String firmwareVersion;
    private String serialNumber;

    public BluetoothReaderDevice(@NonNull final BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    @Override
    public String getName() {
        final String name = bluetoothDevice.getName();
        return TextUtils.isEmpty(name) ? "" : name;
    }

    @Override
    public String getMac() {
        return bluetoothDevice.getAddress();
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String getSerial() {
        return serialNumber;
    }

    @Override
    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BluetoothReaderDevice that = (BluetoothReaderDevice) o;
        return Objects.equals(bluetoothDevice, that.bluetoothDevice) &&
                firmwareVersion.equals(that.firmwareVersion) &&
                serialNumber.equals(that.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bluetoothDevice, firmwareVersion, serialNumber);
    }

    @Override
    public String toString() {
        return "BluetoothReaderDevice{" +
                "name='" + getName() + '\'' +
                ", mac='" + getMac() + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                '}';
    }
}
