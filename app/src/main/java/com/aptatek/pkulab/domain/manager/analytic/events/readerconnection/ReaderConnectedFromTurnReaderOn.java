package com.aptatek.pkulab.domain.manager.analytic.events.readerconnection;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class ReaderConnectedFromTurnReaderOn extends AnalyticsEvent {

    private final String serialNumber;
    private final String firmwareVersion;
    private final String stepId;
    private final long elapsedTime;
    private final int readerBatteryPercent;

    public ReaderConnectedFromTurnReaderOn(final String serialNumber, final String firmwareVersion, final String stepId, final long elapsedTime, final int readerBatteryPercent) {
        super("reader_turn_on_done", null, EventCategory.USER_BEHAVIOUR);

        this.serialNumber = serialNumber;
        this.firmwareVersion = firmwareVersion;
        this.stepId = stepId;
        this.elapsedTime = elapsedTime;
        this.readerBatteryPercent = readerBatteryPercent;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getStepId() {
        return stepId;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public int getReaderBatteryPercent() {
        return readerBatteryPercent;
    }

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("ellapsed_time", String.valueOf(elapsedTime));
        map.put("reader_battery", String.valueOf(readerBatteryPercent));
        map.put("step_id", String.valueOf(stepId));
        map.put("firmware_version", firmwareVersion);
        map.put("serial_number", serialNumber);
        return map;
    }

    @Override
    public String toString() {
        return "ReaderConnectedFromTurnReaderOn{" +
                "serialNumber='" + serialNumber + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", stepId='" + stepId + '\'' +
                ", elapsedTime=" + elapsedTime +
                ", readerBatteryPercent=" + readerBatteryPercent +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
