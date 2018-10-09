package com.aptatek.pkulab.domain.error;

public class DeviceNotSupportedError extends ReaderError {

    public DeviceNotSupportedError() {
        super("Device is not supported", -1);
    }
}
