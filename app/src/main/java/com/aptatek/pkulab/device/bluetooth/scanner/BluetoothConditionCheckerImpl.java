package com.aptatek.pkulab.device.bluetooth.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

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

    @Override
    public List<String> getMissingPermissions() {
        final boolean adminGranted;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            adminGranted = PermissionChecker.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PermissionChecker.PERMISSION_GRANTED;
        } else {
            adminGranted = true;
        }

        final boolean connectGranted;
        final boolean scanGranted;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            connectGranted = PermissionChecker.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PermissionChecker.PERMISSION_GRANTED;
            scanGranted = PermissionChecker.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PermissionChecker.PERMISSION_GRANTED;
        } else {
            connectGranted = true;
            scanGranted = true;
        }

        final boolean locationGranted = PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED;
        final List<String> missingPermissions = new ArrayList<>();

        if (!adminGranted) {
            missingPermissions.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
        if (!locationGranted) {
            missingPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!connectGranted) {
            missingPermissions.add(Manifest.permission.BLUETOOTH_CONNECT);
        }
        if (!scanGranted) {
            missingPermissions.add(Manifest.permission.BLUETOOTH_SCAN);
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
        final boolean showAdmin;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            showAdmin = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.BLUETOOTH);
        } else {
            showAdmin = false;
        }
        final boolean showLocationRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        final boolean showConnect;
        final boolean showScan;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            showConnect = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.BLUETOOTH_CONNECT);
            showScan = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.BLUETOOTH_SCAN);
        } else {
            showConnect = false;
            showScan = false;
        }
        return showLocationRationale || showAdmin || showConnect || showScan;
    }

    @Override
    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }
}
