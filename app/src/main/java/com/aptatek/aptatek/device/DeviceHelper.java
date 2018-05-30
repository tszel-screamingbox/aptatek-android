package com.aptatek.aptatek.device;

import com.aptatek.aptatek.domain.manager.FingerprintManager;

/**
 * Helper for easy access device provided data
 */

public class DeviceHelper {

    private final FingerprintManager fingerprintManager;

    public DeviceHelper(FingerprintManager fingerprintManager) {
        this.fingerprintManager = fingerprintManager;
    }

    public boolean isFingerprintHadrwareDetected() {
        return fingerprintManager.isFingerprintHadrwareDetected();
    }

    public boolean hasEnrolledFingerprints() {
        return fingerprintManager.hasEnrolledFingerprints();
    }
}
