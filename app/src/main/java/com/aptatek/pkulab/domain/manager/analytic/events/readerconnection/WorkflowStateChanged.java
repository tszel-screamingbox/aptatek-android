package com.aptatek.pkulab.domain.manager.analytic.events.readerconnection;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class WorkflowStateChanged extends AnalyticsEvent {

    private final String prevState;
    private final String newState;
    private final String firmwareVersion;
    private final String serialNumber;

    public WorkflowStateChanged(String prevState, String newState, String firmwareVersion, String serialNumber) {
        super("reader_workflow_state_changed", null, EventCategory.READER_COMMUNICATION);
        this.prevState = prevState;

        this.newState = newState;
        this.firmwareVersion = firmwareVersion;
        this.serialNumber = serialNumber;
    }

    public String getPrevState() {
        return prevState;
    }

    public String getNewState() {
        return newState;
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
        map.put("prev_state", prevState);
        map.put("new_state", newState);
        map.put("firmware_version", firmwareVersion);
        map.put("serial_number", serialNumber);
        return map;
    }

    @Override
    public String toString() {
        return "WorkflowStateChanged{" +
                "prevState='" + prevState + '\'' +
                ", newState='" + newState + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
