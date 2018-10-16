package com.aptatek.pkulab.device.time;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Weeks;

import java.util.Date;

public final class TimeHelper {

    private TimeHelper() {

    }

    public static long addDays(final int numOfDays, final long targetTimestamp) {
        final LocalDate localDate = dateFromTimestamp(targetTimestamp);
        final LocalDate daysAdded = localDate.plusDays(numOfDays);
        return daysAdded.toDate().getTime();
    }

    public static long addWeeks(final int numOfWeeks, final long targetTimestamp) {
        final LocalDate localDate = dateFromTimestamp(targetTimestamp);
        final LocalDate weeksAdded = localDate.plusWeeks(numOfWeeks);
        return weeksAdded.toDate().getTime();
    }

    public static long addMonths(final int numOfMonths, final long targetTimestamp) {
        final LocalDate localDate = dateFromTimestamp(targetTimestamp);
        final LocalDate monthsAdded = localDate.plusMonths(numOfMonths);
        return monthsAdded.toDate().getTime();
    }

    public static long getEarliestTimeAtGivenDay(final long timestamp) {
        final LocalDateTime localTime = timeFromTimestamp(timestamp);
        final LocalDateTime earliest = localTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        return earliest.toDate().getTime();
    }

    public static long getLatestTimeAtGivenDay(final long timestamp) {
        final LocalDateTime localTime = timeFromTimestamp(timestamp);
        final LocalDateTime latest = localTime.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
        return latest.toDate().getTime();
    }

    public static long getEarliestTimeAtGivenWeek(final long timestamp) {
        final LocalDate localDate = dateFromTimestamp(timestamp);
        final LocalDate mondayThatWeek = localDate.withDayOfWeek(DateTimeConstants.MONDAY);
        return getEarliestTimeAtGivenDay(mondayThatWeek.toDate().getTime());
    }

    public static long getLatestTimeAtGivenWeek(final long timestamp) {
        final LocalDate localDate = dateFromTimestamp(timestamp);
        final LocalDate sundayThatWeek = localDate.withDayOfWeek(DateTimeConstants.SUNDAY);
        return getLatestTimeAtGivenDay(sundayThatWeek.toDate().getTime());
    }

    public static long getEarliestTimeAtGivenMonth(final long timestamp) {
        final LocalDate localDate = dateFromTimestamp(timestamp);
        final LocalDate mondayThatMonth = localDate.dayOfMonth().withMinimumValue();
        return getEarliestTimeAtGivenDay(mondayThatMonth.toDate().getTime());
    }

    public static long getLatestTimeAtGivenMonth(final long timestamp) {
        final LocalDate localDate = dateFromTimestamp(timestamp);
        final LocalDate sundayThatMonth = localDate.dayOfMonth().withMaximumValue();
        return getLatestTimeAtGivenDay(sundayThatMonth.toDate().getTime());
    }

    public static int getDaysBetween(final long start, final long end) {
        final LocalDateTime startLocalDate = timeFromTimestamp(start);
        final LocalDateTime endLocalDate = timeFromTimestamp(end);
        return Days.daysBetween(startLocalDate, endLocalDate).getDays();
    }

    public static int getWeeksBetween(final long start, final long end) {
        final LocalDateTime startLocalDate = timeFromTimestamp(start);
        final LocalDateTime endLocalDate = timeFromTimestamp(end);
        return Weeks.weeksBetween(startLocalDate, endLocalDate).getWeeks();
    }

    public static String getNameOfDay(final long timestamp) {
        return dateFromTimestamp(timestamp).dayOfWeek().getAsText();
    }

    public static int getDayOfWeek(final long timestamp) {
        final LocalDate localDate = dateFromTimestamp(timestamp);
        return localDate.getDayOfWeek();
    }

    public static int getDayOfMonth(final long timestamp) {
        final LocalDate localDate = dateFromTimestamp(timestamp);
        return localDate.getDayOfMonth();
    }

    public static int getHourOfDay(final long timestamp) {
        final LocalDateTime localDate = timeFromTimestamp(timestamp);
        return localDate.getHourOfDay();
    }

    public static int getMinuteOfDay(final long timestamp) {
        final LocalDateTime localDateTime = timeFromTimestamp(timestamp);
        final int minuteOfHour = localDateTime.getMinuteOfHour();
        final int hourOfDay = getHourOfDay(timestamp);
        return hourOfDay * 60 + minuteOfHour;
    }

    private static LocalDate dateFromTimestamp(final long timestamp) {
        return LocalDate.fromDateFields(new Date(timestamp));
    }

    private static LocalDateTime timeFromTimestamp(final long timestamp) {
        return LocalDateTime.fromDateFields(new Date(timestamp));
    }
}
