package com.aptatek.pkulab.domain.manager.analytic.events.readerconnection;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class ReaderWorkflowStateError extends AnalyticsEvent {

    private final String serialNumber;
    private final String firmwareVersion;

    public ReaderWorkflowStateError(final String serialNumber, final String firmwareVersion) {
        super("error_reader_workflow", null, EventCategory.ERROR);
        this.serialNumber = serialNumber;
        this.firmwareVersion = firmwareVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
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
        return "ReaderWorkflowStateError{" +
                "serialNumber='" + serialNumber + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
