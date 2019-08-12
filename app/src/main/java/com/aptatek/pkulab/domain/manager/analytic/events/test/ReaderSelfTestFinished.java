package com.aptatek.pkulab.domain.manager.analytic.events.test;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.events.AnalyticsEvent;

import java.util.HashMap;
import java.util.Map;

public class ReaderSelfTestFinished extends AnalyticsEvent {

    private final long elapsedTime;
    private final int deviceBatteryPercent;

    public ReaderSelfTestFinished(final long elapsedTime, final int deviceBatteryPercent) {
        super("testing_reader_self_testing", null, EventCategory.READER_COMMUNICATION);
        this.elapsedTime = elapsedTime;
        this.deviceBatteryPercent = deviceBatteryPercent;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public int getDeviceBatteryPercent() {
        return deviceBatteryPercent;
    }

    @Nullable
    @Override
    public Map<String, String> getAdditionalInfo() {
        final Map<String, String> map = new HashMap<>();
        map.put("reader_battery", String.valueOf(deviceBatteryPercent));
        map.put("ellapsed_screentime_sec", String.valueOf(elapsedTime));
        return map;
    }

    @Override
    public String toString() {
        return "ReaderSelfTestFinished{" +
                "elapsedTime=" + elapsedTime +
                ", deviceBatteryPercent=" + deviceBatteryPercent +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", eventCategory=" + eventCategory +
                '}';
    }
}
