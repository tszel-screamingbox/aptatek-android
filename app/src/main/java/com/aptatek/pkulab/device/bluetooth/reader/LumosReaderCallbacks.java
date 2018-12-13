package com.aptatek.pkulab.device.bluetooth.reader;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.device.bluetooth.model.ErrorResponse;
import com.aptatek.pkulab.device.bluetooth.model.ResultResponse;
import com.aptatek.pkulab.device.bluetooth.model.ResultSyncResponse;
import com.aptatek.pkulab.device.bluetooth.model.WorkflowStateResponse;

import no.nordicsemi.android.ble.BleManagerCallbacks;

interface LumosReaderCallbacks extends BleManagerCallbacks {

    void onReadResultSync(@NonNull ResultSyncResponse resultSyncResponse);

    void onReadResult(@NonNull ResultResponse resultResponse);

    void onReadError(@NonNull ErrorResponse errorResponse);

    void onMtuSizeChanged(@NonNull BluetoothDevice device, int mtuSize);

    void onWorkflowStateChanged(@NonNull WorkflowStateResponse workflowStateResponse);

}
