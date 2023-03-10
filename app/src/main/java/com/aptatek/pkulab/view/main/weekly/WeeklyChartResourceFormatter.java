package com.aptatek.pkulab.view.main.weekly;

public interface WeeklyChartResourceFormatter {

    String getWeeklyChartTitle(final int weeksBeforeNow);

    String getPdfFileNameDateFormat();

    String getPdfMonthFormat(final int weeksBeforeNow);

    String getFormattedCsvFileName();

    String getFormattedCsvColumn(long timeInMillis);

    String getFormattedCsvBody(long startDate, long endDate, String version);

    String getFormattedRemindersCsvFileName();

    String getFormattedPkuRangeInfoCsvFileName();
}
