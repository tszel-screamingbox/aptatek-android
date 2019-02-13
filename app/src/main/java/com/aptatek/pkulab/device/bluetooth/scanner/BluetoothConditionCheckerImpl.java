package com.aptatek.pkulab.device.bluetooth.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import com.aptatek.pkulab.domain.manager.reader.BluetoothAdapter;
import com.aptatek.pkulab.domain.manager.reader.BluetoothConditionChecker;
import com.aptatek.pkulab.injection.qualifier.ActivityContext;
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

    @Override
    public List<String> getMissingPermissions() {
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
    public boolean hasBleFeature() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    @Override
    public boolean hasAllPermissions() {
        return getMissingPermissions().isEmpty();
    }

    @SuppressLint("NewApi")
    @Override
    public boolean shouldShowRationale(final Activity activity) {
        final boolean showBluetoothRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.BLUETOOTH);
        final boolean showLocationRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        return showBluetoothRationale || showLocationRationale;
    }

    @Override
    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }
}
