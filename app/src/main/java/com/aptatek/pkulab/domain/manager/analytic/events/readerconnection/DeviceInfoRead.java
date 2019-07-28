package com.aptatek.pkulab.domain.manager.analytic.events.readerconnection;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class DeviceInfoRead extends AnalyticsEvent {

    private final String firmwareVersion;
    private final String serialNumber;

    public DeviceInfoRead(String firmwareVersion, String serialNumber) {
        super("reader_device_info", null, EventCategory.READER_COMMUNICATION);

        this.firmwareVersion = firmwareVersion;
        this.serialNumber = serialNumber;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("firmware_version", firmwareVersion);
        map.put("serial_number", serialNumber);
        return map;
    }


    @Override
    public String toString() {
        return "DeviceInfoRead{" +
                "firmwareVersion='" + firmwareVersion + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
