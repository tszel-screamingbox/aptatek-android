package com.aptatek.pkulab.domain.manager.analytic.events.readerconnection;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class ReaderDataSynced extends AnalyticsEvent {

    private final int numberOfResults;
    private final long elapsedTime;
    private final String firmwareVersion;
    private final String serialNumber;

    public ReaderDataSynced(int numberOfResults, long elapsedTime, String firmwareVersion, String serialNumber) {
        super("reader_data_synced", null, EventCategory.READER_COMMUNICATION);
        this.numberOfResults = numberOfResults;
        this.elapsedTime = elapsedTime;
        this.firmwareVersion = firmwareVersion;
        this.serialNumber = serialNumber;
    }

    public int getNumberOfResults() {
        return numberOfResults;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    // total_num_of_items, ellapsed_time (total time of sync), firmware_version, serial_number
    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("total_num_of_items", String.valueOf(numberOfResults));
        map.put("ellapsed_time", String.valueOf(elapsedTime));
        map.put("firmware_version", firmwareVersion);
        map.put("serial_number", serialNumber);
        return map;
    }

    @Override
    public String toString() {
        return "ReaderDataSynced{" +
                "numberOfResults=" + numberOfResults +
                ", elapsedTime=" + elapsedTime +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
