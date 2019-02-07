package com.aptatek.pkulab.device.bluetooth.scanner;

import android.Manifest;
import android.content.Context;
import android.support.v4.content.PermissionChecker;

import com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter;
import com.aptatek.pkulab.domain.manager.reader.BluetoothConditionChecker;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class BluetoothConditionCheckerImpl implements BluetoothConditionChecker {

    private final Context context;
    private final BluetoothAdapter bluetoothAdapter;

    public BluetoothConditionCheckerImpl(final @ApplicationContext Context context,
                                         final BluetoothAdapter bluetoothAdapter) {
        this.context = context;
        this.bluetoothAdapter = bluetoothAdapter;
    }

    private List<String> gatherMissingPermissions() {
        final boolean bluetoothGranted = PermissionChecker.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PermissionChecker.PERMISSION_GRANTED;
        final boolean locationGranted = PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED;
        final List<String> missingPermissions = new ArrayList<>();

        if (!bluetoothGranted) {
            missingPermissions.add(Manifest.permission.BLUETOOTH_ADMIN);
        } else if (!locationGranted) {
            missingPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        return missingPermissions;
    }

    @Override
    public boolean hasAllPermissions() {
        return gatherMissingPermissions().isEmpty();
    }

    @Override
    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }
}
