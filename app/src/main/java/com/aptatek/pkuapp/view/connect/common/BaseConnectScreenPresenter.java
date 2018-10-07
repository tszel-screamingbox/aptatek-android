package com.aptatek.pkuapp.view.connect.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;

import com.aptatek.pkuapp.injection.qualifier.ActivityContext;
import com.aptatek.pkuapp.view.connect.ConnectReaderScreen;
import com.aptatek.pkuapp.view.connect.permission.PermissionRequiredFragment;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import ix.Ix;
import timber.log.Timber;

public abstract class BaseConnectScreenPresenter<V extends BaseConnectScreenView> extends MvpBasePresenter<V> {

    protected final Context context;

    protected BaseConnectScreenPresenter(@ActivityContext final Context context) {
        this.context = context;
    }

    public void checkMandatoryRequirements() {
        if (!hasAllPermissions()) {
            onMissingPermissionsFound();
        } else if (!isBluetoothEnabled()) {
            ifViewAttached(attachedView -> attachedView.showScreen(ConnectReaderScreen.ENABLE_BLUETOOTH));
        } else {
            onRequiredConditionsMet();
        }
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

    public boolean hasAllPermissions() {
        return gatherMissingPermissions().isEmpty();
    }

    public boolean isBluetoothEnabled() {
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // This should not happen since we strictly require the device to have Bluetooth in the Manifest.
            Timber.e("No bluetooth adapter!");
        } else return mBluetoothAdapter.isEnabled();

        return false;
    }

    @SuppressLint("NewApi")
    protected void requestMissingPermissions() {
        ifViewAttached(attachedView -> attachedView.requestMissingPermissions(gatherMissingPermissions()));
    }

    public void evaluatePermissionResults(@NonNull final List<PermissionResult> results) {
        final Boolean hasAllPermissions = Ix.from(results)
                .map(PermissionResult::getResult)
                .map(result -> result == PermissionChecker.PERMISSION_GRANTED)
                .scan((prev, current) -> prev && current)
                .single(false);

        if (!hasAllPermissions) {
            ifViewAttached(attachedView -> attachedView.showScreen(ConnectReaderScreen.PERMISSION_REQUIRED));
        } else if (!isBluetoothEnabled()) {
            ifViewAttached(attachedView -> attachedView.showScreen(ConnectReaderScreen.ENABLE_BLUETOOTH));
        } else {
            onRequiredConditionsMet();
        }
    }

    protected abstract void onRequiredConditionsMet();

    protected abstract void onMissingPermissionsFound();

}
