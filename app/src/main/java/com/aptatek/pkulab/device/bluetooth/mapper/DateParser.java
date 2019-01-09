package com.aptatek.pkulab.device.bluetooth.mapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public class DateParser {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'kkmmss");

    public static long tryParseDate(final String date) {
        try {
            final Date parse = dateFormat.parse(date);
            return parse.getTime();
        } catch (ParseException e) {
            Timber.d("Failed to parse date: %s", e);
            return System.currentTimeMillis();
        }
    }

}
