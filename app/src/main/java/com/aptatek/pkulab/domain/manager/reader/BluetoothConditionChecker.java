package com.aptatek.pkulab.domain.manager.reader;

public interface BluetoothConditionChecker {

    boolean hasAllPermissions();
    boolean isBluetoothEnabled();

}
