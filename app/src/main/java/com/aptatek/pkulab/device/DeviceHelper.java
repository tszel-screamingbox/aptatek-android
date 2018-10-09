package com.aptatek.pkulab.device;

import com.aptatek.pkulab.domain.manager.FingerprintManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Helper for easy access device provided data
 */

@Singleton
public class DeviceHelper {

    private final FingerprintManager fingerprintManager;
    private final PreferenceManager preferenceManager;

    @Inject
    public DeviceHelper(final FingerprintManager fingerprintManager,
                        final PreferenceManager preferenceManager) {
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
}