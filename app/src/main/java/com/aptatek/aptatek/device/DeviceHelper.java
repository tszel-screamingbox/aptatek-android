package com.aptatek.aptatek.device;

import com.aptatek.aptatek.domain.manager.FingerprintManager;

import javax.inject.Inject;

/**
 * Helper for easy access device provided data
 */

public class DeviceHelper {

    private final FingerprintManager fingerprintManager;

    @Inject
    public DeviceHelper(FingerprintManager fingerprintManager) {
        this.fingerprintManager = fingerprintManager;
    }

    public boolean hasFingerprintHadrware() {
        return fingerprintManager.isFingerprintHadrwareDetected();
    }

    public boolean hasEnrolledFingerprints() {
        return fingerprintManager.hasEnrolledFingerprints();
    }
}
