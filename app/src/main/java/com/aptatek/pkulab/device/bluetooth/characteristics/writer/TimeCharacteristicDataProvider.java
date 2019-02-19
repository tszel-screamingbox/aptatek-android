package com.aptatek.pkulab.device.bluetooth.characteristics.writer;

import android.support.annotation.Nullable;

import com.aptatek.pkulab.device.bluetooth.model.UpdateTimeResponse;

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
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        updateTimeResponse.setYear(calendar.get(Calendar.YEAR));
        updateTimeResponse.setMonth(calendar.get(Calendar.MONTH) + 1);
        updateTimeResponse.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        updateTimeResponse.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        updateTimeResponse.setMinute(calendar.get(Calendar.MINUTE));
        updateTimeResponse.setSecond(calendar.get(Calendar.SECOND));

        return updateTimeResponse;
    }
}
