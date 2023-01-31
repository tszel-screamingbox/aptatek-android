package com.aptatek.pkulab.domain.manager.reader;

import android.app.Activity;

import java.util.List;

public interface BluetoothConditionChecker {

    boolean hasBleFeature();

    boolean hasAllPermissions();

    boolean shouldShowRationale(Activity activity);

    boolean isBluetoothEnabled();

    List<String> getMissingPermissions();

}
