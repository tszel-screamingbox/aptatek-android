package com.aptatek.aptatek.device;

import com.aptatek.aptatek.domain.manager.FingerprintManager;
import com.aptatek.aptatek.domain.manager.SharedPreferencesManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Helper for easy access device provided data
 */

@Singleton
public final class DeviceHelper {

    private final FingerprintManager fingerprintManager;
    private final SharedPreferencesManager sharedPreferencesManager;

    @Inject
    public DeviceHelper(final FingerprintManager fingerprintManager,
                        final SharedPreferencesManager sharedPreferencesManager) {
        this.fingerprintManager = fingerprintManager;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    public boolean hasFingerprintHadrware() {
        return fingerprintManager.isFingerprintHadrwareDetected();
    }

    public boolean hasEnrolledFingerprints() {
        return fingerprintManager.hasEnrolledFingerprints();
    }

    public boolean isFingperprintAuthAvailable() {
        return hasEnrolledFingerprints() &&
                hasEnrolledFingerprints() &&
                sharedPreferencesManager.isFingerprintScanEnabled();
    }
}
