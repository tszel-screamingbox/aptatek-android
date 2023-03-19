package com.aptatek.pkulab.device.bluetooth.characteristics.writer;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.device.bluetooth.model.UpdateTimeResponse;

import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;

import java.util.Calendar;
import java.util.TimeZone;

import javax.inject.Inject;

public class TimeCharacteristicDataProvider implements CharacteristicDataProvider<CharacteristicDataProvider.CharacteristicsData> {

    private final JsonCharacteristicDataConverter writer;

    @Inject
    TimeCharacteristicDataProvider(final JsonCharacteristicDataConverter writer) {
        this.writer = writer;
    }

    @Override
    public byte[] provideData(@Nullable final CharacteristicsData data) {
        return writer.convertData(createTimeResponse());
    }

    private UpdateTimeResponse createTimeResponse() {
        final UpdateTimeResponse updateTimeResponse = new UpdateTimeResponse();
        final DateTime now = DateTime.now(DateTimeZone.UTC);
        updateTimeResponse.setYear(now.getYear());
        updateTimeResponse.setMonth(now.getMonthOfYear());
        updateTimeResponse.setDay(now.getDayOfMonth());
        updateTimeResponse.setHour(now.getHourOfDay());
        updateTimeResponse.setMinute(now.getMinuteOfHour());
        updateTimeResponse.setSecond(now.getSecondOfMinute());

        return updateTimeResponse;
    }
}
