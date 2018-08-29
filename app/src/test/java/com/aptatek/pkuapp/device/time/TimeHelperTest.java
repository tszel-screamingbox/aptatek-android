package com.aptatek.pkuapp.device.time;


import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.aptatek.pkuapp.device.time.TimeHelper.addDays;
import static com.aptatek.pkuapp.device.time.TimeHelper.addWeeks;
import static com.aptatek.pkuapp.device.time.TimeHelper.getDayOfMonth;
import static com.aptatek.pkuapp.device.time.TimeHelper.getDayOfWeek;
import static com.aptatek.pkuapp.device.time.TimeHelper.getDaysBetween;
import static com.aptatek.pkuapp.device.time.TimeHelper.getEarliestTimeAtGivenDay;
import static com.aptatek.pkuapp.device.time.TimeHelper.getHourOfDay;
import static com.aptatek.pkuapp.device.time.TimeHelper.getLatestTimeAtGivenDay;
import static com.aptatek.pkuapp.device.time.TimeHelper.getMinuteOfDay;
import static com.aptatek.pkuapp.device.time.TimeHelper.getNameOfDay;
import static com.aptatek.pkuapp.device.time.TimeHelper.getWeeksBetween;
import static junit.framework.Assert.assertEquals;

public class TimeHelperTest {

    private static final int TWO_WEEK_IN_DAYS = 14;
    private static final int WEEK_IN_DAYS = 7;
    private static final int FIVE_DAYS = 5;

    private Date now;
    private Calendar calendar;

    @Before
    public void setUp() {
        calendar = Calendar.getInstance();
        now = new Date();
        calendar.setTime(now);
    }

    @Test
    public void testHourOfDay() {
        final int hour = getHourOfDay(now.getTime());
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), hour);
    }

    @Test
    public void testMinuteOfDay() {
        final int minute = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
        assertEquals(minute, getMinuteOfDay(now.getTime()));
    }

    @Test
    public void testDayOfMonth() {
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        assertEquals(day, getDayOfMonth(now.getTime()));
    }

    @Test
    public void testDayOfWeek() {
        final int day = org.joda.time.LocalDate.fromCalendarFields(calendar).getDayOfWeek();
        assertEquals(day, getDayOfWeek(now.getTime()));
    }

    @Test
    public void testNameOfDay() {
        final String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        assertEquals(day, getNameOfDay(now.getTime()));
    }

    @Test
    public void testWeeksBetween() {
        calendar.add(Calendar.DAY_OF_YEAR, WEEK_IN_DAYS);
        assertEquals(1, getWeeksBetween(now.getTime(), calendar.getTime().getTime()));
    }

    @Test
    public void testDaysBetween() {
        calendar.add(Calendar.DAY_OF_YEAR, FIVE_DAYS);
        assertEquals(FIVE_DAYS, getDaysBetween(now.getTime(), calendar.getTime().getTime()));
    }

    @Test
    public void testLatestTimeAtGivenWeek() {
        final Date endOfWeek = new Date(getLatestTimeAtGivenDay(now.getTime()));
        calendar.setTime(endOfWeek);
        final String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
        assertEquals(59, calendar.get(Calendar.SECOND));
        assertEquals(day, getNameOfDay(endOfWeek.getTime()));
    }

    @Test
    public void testEarliestTimeAtGivenWeek() {
        final Date endOfWeek = new Date(getEarliestTimeAtGivenDay(now.getTime()));
        calendar.setTime(endOfWeek);
        final String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(day, getNameOfDay(endOfWeek.getTime()));
    }

    @Test
    public void testLatestTimeAtGivenDay() {
        final Date almostMidnight = new Date(getLatestTimeAtGivenDay(now.getTime()));
        calendar.setTime(almostMidnight);
        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
        assertEquals(59, calendar.get(Calendar.SECOND));
        assertEquals(999, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void testEarliestTimeAtGivenDay() {
        final Date midnight = new Date(getEarliestTimeAtGivenDay(now.getTime()));
        calendar.setTime(midnight);
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void testAddWeeksPlus() {
        final long future = addWeeks(2, now.getTime());
        final long present = getEarliestTimeAtGivenDay(now.getTime());
        assertEquals(TWO_WEEK_IN_DAYS, getDaysBetween(present, future));
    }

    @Test
    public void testAddWeeksMinus() {
        final long past = addWeeks(-2, now.getTime());
        final long present = getEarliestTimeAtGivenDay(now.getTime());
        assertEquals(TWO_WEEK_IN_DAYS, getDaysBetween(past, present));
    }

    @Test
    public void testAddDaysPlus() {
        calendar.add(Calendar.DAY_OF_YEAR, WEEK_IN_DAYS);
        final long future = calendar.getTime().getTime();
        assertEquals(getEarliestTimeAtGivenDay(future), addDays(WEEK_IN_DAYS, now.getTime()));
    }

    @Test
    public void testAddDaysMinus() {
        calendar.add(Calendar.DAY_OF_YEAR, -FIVE_DAYS);
        final long past = calendar.getTime().getTime();
        assertEquals(getEarliestTimeAtGivenDay(past), addDays(-FIVE_DAYS, now.getTime()));
    }
}
