package com.aptatek.pkuapp.view.weekly;

public interface WeeklyChartDateFormatter {

    String getWeeklyChartTitle(final int weeksBeforeNow);

    String getPdfFileNameDateFormat();

    String getPdfMonthFormat(final int weeksBeforeNow);
}
