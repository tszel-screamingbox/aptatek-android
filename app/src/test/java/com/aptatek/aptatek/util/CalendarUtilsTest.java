package com.aptatek.aptatek.util;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CalendarUtilsTest {

    private static final String PATTERN = "yyyy-MM-dd hh:mm";

    private Date now;
    private Date yesterday;

    @Before
    public void setUp() {

        now = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, -1);
        yesterday = calendar.getTime();
    }

    @Test
    public void testSameDay() {
        assertTrue(CalendarUtils.isSameDay(now, now));
    }

    @Test
    public void testNotTheSameDay() {
        assertTrue(!CalendarUtils.isSameDay(now, yesterday));
    }

    @Test
    public void testDaysOfWeek() {
        assertEquals(CalendarUtils.dayOfWeek(1), "Sunday");
        assertEquals(CalendarUtils.dayOfWeek(2), "Monday");
        assertEquals(CalendarUtils.dayOfWeek(3), "Tuesday");
        assertEquals(CalendarUtils.dayOfWeek(4), "Wednesday");
        assertEquals(CalendarUtils.dayOfWeek(5), "Thursday");
        assertEquals(CalendarUtils.dayOfWeek(6), "Friday");
        assertEquals(CalendarUtils.dayOfWeek(7), "Saturday");
        assertEquals(CalendarUtils.dayOfWeek(0), null);
    }

    @Test
    public void testFormatDate() {
        final String formatDate = CalendarUtils.formatDate(now, PATTERN);
        assertEquals(formatDate, new SimpleDateFormat(PATTERN, Locale.ENGLISH).format(now));
    }

    @Test
    public void testFormatYear() {
        final String year = CalendarUtils.formatDate(now, "yyyy");
        final Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        assertEquals(year, String.valueOf(cal.get(Calendar.YEAR)));
    }

    @Test
    public void testDayNumberSuffix() {
        assertEquals(CalendarUtils.dayNumberSuffix(1), "st");
        assertEquals(CalendarUtils.dayNumberSuffix(2), "nd");
        assertEquals(CalendarUtils.dayNumberSuffix(3), "rd");
        assertEquals(CalendarUtils.dayNumberSuffix(4), "th");
        assertEquals(CalendarUtils.dayNumberSuffix(10), "th");
        assertEquals(CalendarUtils.dayNumberSuffix(11), "th");
        assertEquals(CalendarUtils.dayNumberSuffix(12), "th");
        assertEquals(CalendarUtils.dayNumberSuffix(13), "th");
        assertEquals(CalendarUtils.dayNumberSuffix(21), "st");
        assertEquals(CalendarUtils.dayNumberSuffix(22), "nd");
        assertEquals(CalendarUtils.dayNumberSuffix(23), "rd");
        assertEquals(CalendarUtils.dayNumberSuffix(31), "st");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDayNumberSuffix() throws Exception {
        CalendarUtils.dayNumberSuffix(32);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDayNumberSuffixWithZero() throws Exception {
        CalendarUtils.dayNumberSuffix(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDayNumberSuffixWithNegative() throws Exception {
        CalendarUtils.dayNumberSuffix(-1);
    }
}