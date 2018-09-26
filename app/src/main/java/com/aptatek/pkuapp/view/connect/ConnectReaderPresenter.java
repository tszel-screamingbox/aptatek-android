package com.aptatek.pkuapp.view.connect;

import android.bluetooth.BluetoothDevice;

import com.aptatek.pkuapp.device.bluetooth.LumosReaderCallbacks;
import com.aptatek.pkuapp.device.bluetooth.LumosReaderManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

public class ConnectReaderPresenter extends MvpBasePresenter<ConnectReaderView> implements LumosReaderCallbacks {

    private final LumosReaderManager lumosReaderManager;

    @Inject
    public ConnectReaderPresenter(final LumosReaderManager lumosReaderManager) {
        this.lumosReaderManager = lumosReaderManager;
    }

    @Override
    public void onDeviceConnecting(BluetoothDevice device) {

    }

    @Override
    public void onDeviceConnected(BluetoothDevice device) {

    }

    @Override
    public void onDeviceDisconnecting(BluetoothDevice device) {

    }

    @Override
    public void onDeviceDisconnected(BluetoothDevice device) {

    }

    @Override
    public void onLinklossOccur(BluetoothDevice device) {

    }

    @Override
    public void onServicesDiscovered(BluetoothDevice device, boolean optionalServicesFound) {

    }

    @Override
    public void onDeviceReady(BluetoothDevice device) {

    }

    @Override
    public boolean shouldEnableBatteryLevelNotifications(BluetoothDevice device) {
        return false;
    }

    @Override
    public void onBatteryValueReceived(BluetoothDevice device, int value) {

    }

    @Override
    public void onBondingRequired(BluetoothDevice device) {

    }

    @Override
    public void onBonded(BluetoothDevice device) {

    }

    @Override
    public void onError(BluetoothDevice device, String message, int errorCode) {

    }

    @Override
    public void onDeviceNotSupported(BluetoothDevice device) {

    }

}
