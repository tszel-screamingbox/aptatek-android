package com.aptatek.pkulab.domain.manager.reader;

public interface BluetoothConditionChecker {

    boolean hasBleFeature();
    boolean hasAllPermissions();
    boolean isBluetoothEnabled();

}
