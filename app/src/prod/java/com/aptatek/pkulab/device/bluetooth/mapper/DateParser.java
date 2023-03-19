package com.aptatek.pkulab.device.bluetooth.mapper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import timber.log.Timber;

public class DateParser {

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd'T'kkmmss")
            .withZone(DateTimeZone.UTC);

    public static long tryParseDate(final String date) {
        try {
            final DateTime parsed = DateTime.parse(date, formatter);
            return parsed.getMillis();
        } catch (Exception e) {
            Timber.d("Failed to parse date: %s", e);
            return DateTime.now(DateTimeZone.UTC).getMillis();
        }
    }

}
