package com.aptatek.pkulab.data.model;

import com.aptatek.pkulab.domain.model.reader.ReaderDevice;

public class MockReaderDevice implements ReaderDevice {

    private final String name;
    private final String mac;

    public MockReaderDevice(final String name, final String mac) {
        this.name = name;
        this.mac = mac;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMac() {
        return mac;
    }
}
