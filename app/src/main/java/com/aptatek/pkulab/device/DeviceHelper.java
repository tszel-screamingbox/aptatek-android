package com.aptatek.pkulab.device;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.aptatek.pkulab.domain.manager.FingerprintManager;
import com.aptatek.pkulab.injection.qualifier.ApplicationContext;
import com.scottyab.rootbeer.RootBeer;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

import static android.content.Intent.ACTION_BATTERY_CHANGED;
import static android.os.BatteryManager.EXTRA_LEVEL;
import static android.os.BatteryManager.EXTRA_SCALE;

/**
 * Helper for easy access device provided data
 */

@Singleton
public class DeviceHelper {

    private static final float BATTERY_LEVEL_LOW = 0.2f;
    private static final int MINIMUM_STORAGE_LIMIT = (1024 * 1024) * 100;

    private final Context context;
    private final FingerprintManager fingerprintManager;
    private final PreferenceManager preferenceManager;

    @Inject
    public DeviceHelper(@ApplicationContext final Context context,
                        final FingerprintManager fingerprintManager,
                        final PreferenceManager preferenceManager) {
        this.context = context;
        this.fingerprintManager = fingerprintManager;
        this.preferenceManager = preferenceManager;
    }

    public boolean hasFingerprintHadrware() {
        return fingerprintManager.isFingerprintHadrwareDetected();
    }

    public boolean hasEnrolledFingerprints() {
        return fingerprintManager.hasEnrolledFingerprints();
    }

    public boolean isFingperprintAuthAvailable() {
        return hasEnrolledFingerprints()
                && hasEnrolledFingerprints()
                && preferenceManager.isFingerprintScanEnabled();
    }

    public boolean isStorageLimitReached() {
        return new StatFs(Environment.getDataDirectory().getPath()).getAvailableBytes() < MINIMUM_STORAGE_LIMIT;
    }

    public boolean isRooted() {
        final RootBeer rootBeer = new RootBeer(context);
        return rootBeer.isRootedWithoutBusyBoxCheck();
    }

    public String getDeviceModel() {
        return Build.MODEL;
    }

    public String getDeviceBrand() {
        return Build.BRAND;
    }

    public String getDeviceOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getAvailableBytes() {
        return String.valueOf(new StatFs(Environment.getDataDirectory().getPath()).getAvailableBytes());
    }

    public boolean isBatteryLow() {
        final IntentFilter ifilter = new IntentFilter(ACTION_BATTERY_CHANGED);
        final Intent batteryStatus = context.registerReceiver(null, ifilter);
        final int level = batteryStatus.getIntExtra(EXTRA_LEVEL, -1);
        final int scale = batteryStatus.getIntExtra(EXTRA_SCALE, -1);
        return level / (float) scale <= BATTERY_LEVEL_LOW;
    }

    public String getAppVersion() {
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.d("Faield to get package version: %s", e);
        }

        // should not happen
        return null;
    }
}
