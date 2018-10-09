package com.aptatek.pkuapp.domain.error;

public class DeviceDiscoveryError extends Throwable {

    private final int errorCode;

    public DeviceDiscoveryError(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
