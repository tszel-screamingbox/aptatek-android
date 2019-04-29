package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.model.TestProgressResponse;
import com.aptatek.pkulab.device.bluetooth.model.WorkflowStateResponse;

import no.nordicsemi.android.ble.BleManagerCallbacks;

interface LumosReaderCallbacks extends BleManagerCallbacks {

    // define callback-style events here

    void onMtuSizeChanged(@NonNull BluetoothDevice device, int mtuSize);

    void onWorkflowStateChanged(@NonNull WorkflowStateResponse workflowStateResponse);

    void onTestProgressChanged(@NonNull TestProgressResponse testProgressResponse);

    void onBatteryLevelChanged(@NonNull BluetoothDevice device, int batteryLevel);

}
