package com.aptatek.pkulab.device.time;


import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.aptatek.pkulab.device.time.TimeHelper.addDays;
import static com.aptatek.pkulab.device.time.TimeHelper.addWeeks;
import static com.aptatek.pkulab.device.time.TimeHelper.getDayOfMonth;
import static com.aptatek.pkulab.device.time.TimeHelper.getDayOfWeek;
import static com.aptatek.pkulab.device.time.TimeHelper.getDaysBetween;
import static com.aptatek.pkulab.device.time.TimeHelper.getEarliestTimeAtGivenDay;
import static com.aptatek.pkulab.device.time.TimeHelper.getHourOfDay;
import static com.aptatek.pkulab.device.time.TimeHelper.getLatestTimeAtGivenDay;
import static com.aptatek.pkulab.device.time.TimeHelper.getMinuteOfDay;
import static com.aptatek.pkulab.device.time.TimeHelper.getNameOfDay;
import static com.aptatek.pkulab.device.time.TimeHelper.getWeeksBetween;
import static junit.framework.Assert.assertEquals;

/**
 * @test.layer Device / Time
 * @test.feature Helper class for time/date operations
 * @test.type Unit tests
 */
public class TimeHelperTest {

    private static final int TWO_WEEK_IN_DAYS = 14;
    private static final int WEEK_IN_DAYS = 7;
    private static final int FIVE_DAYS = 5;

    private Date now;
    private Calendar calendar;

    /**
     * Setting up the required variabels.
     */
    @Before
    public void setUp() {
        calendar = Calendar.getInstance();
        now = new Date();
        calendar.setTime(now);
    }

    /**
     * Get the hour of the given day
     *
     * @test.input Current time in milliseconds
     * @test.expected Asserts numbers of the hours, without any error.
     */
    @Test
    public void testHourOfDay() {
        final int hour = getHourOfDay(now.getTime());
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), hour);
    }

    /**
     * Get the minutes of the given day
     *
     * @test.input Current time in milliseconds
     * @test.expected Verifies the numbers of the minutes, without any error.
     */
    @Test
    public void testMinuteOfDay() {
        final int minute = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
        assertEquals(minute, getMinuteOfDay(now.getTime()));
    }

    /**
     * Get the day of the month
     *
     * @test.input Current time in milliseconds
     * @test.expected Verifies the numbers of the days in the specific month, without any error.
     */
    @Test
    public void testDayOfMonth() {
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        assertEquals(day, getDayOfMonth(now.getTime()));
    }

    /**
     * Get the day of the week
     *
     * @test.input Current time in milliseconds
     * @test.expected Verifies the day number in the week, without any error.
     */
    @Test
    public void testDayOfWeek() {
        final int day = org.joda.time.LocalDate.fromCalendarFields(calendar).getDayOfWeek();
        assertEquals(day, getDayOfWeek(now.getTime()));
    }

    /**
     * Get the name of the given day
     *
     * @test.input Current time in milliseconds
     * @test.expected Verifies the given day's name, without any error.
     */
    @Test
    public void testNameOfDay() {
        final String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        assertEquals(day, getNameOfDay(now.getTime()));
    }

    /**
     * Calculate the numbers of weeks between two date
     *
     * @test.input Current time in milliseconds and one week later date in milliseconds
     * @test.expected Verifies the numbers of weeks, without any error.
     */
    @Test
    public void testWeeksBetween() {
        calendar.add(Calendar.DAY_OF_YEAR, WEEK_IN_DAYS);
        assertEquals(1, getWeeksBetween(now.getTime(), calendar.getTime().getTime()));
    }

    /**
     * Calculate the number of days between two date
     *
     * @test.input Current time in milliseconds and 5 days later in milliseconds
     * @test.expected Verifies the numbers of days, without any error.
     */
    @Test
    public void testDaysBetween() {
        calendar.add(Calendar.DAY_OF_YEAR, FIVE_DAYS);
        assertEquals(FIVE_DAYS, getDaysBetween(now.getTime(), calendar.getTime().getTime()));
    }

    /**
     * Calculate the last instant of the given week
     *
     * @test.input Current time in milliseconds
     * @test.expected Verifies the last instant, without any error.
     */
    @Test
    public void testLatestTimeAtGivenWeek() {
        final Date endOfWeek = new Date(getLatestTimeAtGivenDay(now.getTime()));
        calendar.setTime(endOfWeek);
        final String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
        assertEquals(59, calendar.get(Calendar.SECOND));
        assertEquals(999, calendar.get(Calendar.MILLISECOND));
        assertEquals(day, getNameOfDay(endOfWeek.getTime()));
    }

    /**
     * Calculate the first instant of the given week
     *
     * @test.input Current time in milliseconds
     * @test.expected Verifies the first instant, without any error.
     */
    @Test
    public void testEarliestTimeAtGivenWeek() {
        final Date endOfWeek = new Date(getEarliestTimeAtGivenDay(now.getTime()));
        calendar.setTime(endOfWeek);
        final String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
        assertEquals(day, getNameOfDay(endOfWeek.getTime()));
    }

    /**
     * Calculate the last instant of the given day
     *
     * @test.input Current time in milliseconds
     * @test.expected Verifies the last instant, without any error.
     */
    @Test
    public void testLatestTimeAtGivenDay() {
        final Date almostMidnight = new Date(getLatestTimeAtGivenDay(now.getTime()));
        calendar.setTime(almostMidnight);
        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
        assertEquals(59, calendar.get(Calendar.SECOND));
        assertEquals(999, calendar.get(Calendar.MILLISECOND));
    }

    /**
     * Calculate the first instant of the given day
     *
     * @test.input Current time in milliseconds
     * @test.expected Verifies the first instant, without any error.
     */
    @Test
    public void testEarliestTimeAtGivenDay() {
        final Date midnight = new Date(getEarliestTimeAtGivenDay(now.getTime()));
        calendar.setTime(midnight);
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    /**
     * Calculate the date 2 weeks later
     *
     * @test.input Numbers of weeks (2) and current time in milliseconds
     * @test.expected Verifies the date, without any error.
     */
    @Test
    public void testAddWeeksPlus() {
        final long future = addWeeks(2, now.getTime());
        final long present = getEarliestTimeAtGivenDay(now.getTime());
        assertEquals(TWO_WEEK_IN_DAYS, getDaysBetween(present, future));
    }

    /**
     * Calculate the date 2 weeks before
     *
     * @test.input Numbers of weeks (-2) and current time in milliseconds
     * @test.expected Verifies the date, without any error.
     */
    @Test
    public void testAddWeeksMinus() {
        final long past = addWeeks(-2, now.getTime());
        final long present = getEarliestTimeAtGivenDay(now.getTime());
        assertEquals(TWO_WEEK_IN_DAYS, getDaysBetween(past, present));
    }

    /**
     * Calculates the date 5 days later
     *
     * @test.input Numbers of days (5) and current time in milliseconds
     * @test.expected Verifies the date, without any error.
     */
    @Test
    public void testAddDaysPlus() {
        calendar.add(Calendar.DAY_OF_YEAR, FIVE_DAYS);
        final long future = calendar.getTime().getTime();
        assertEquals(getEarliestTimeAtGivenDay(future), addDays(FIVE_DAYS, now.getTime()));
    }

    /**
     * Calculates the date 5 days before
     *
     * @test.input Numbers of days (-5) and current time in milliseconds
     * @test.expected Verifies the date, without any error.
     */
    @Test
    public void testAddDaysMinus() {
        calendar.add(Calendar.DAY_OF_YEAR, -FIVE_DAYS);
        final long past = calendar.getTime().getTime();
        assertEquals(getEarliestTimeAtGivenDay(past), addDays(-FIVE_DAYS, now.getTime()));
    }
}
