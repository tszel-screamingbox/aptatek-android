package com.aptatek.pkulab.device.formatter;

import static java.util.Locale.getDefault;

import androidx.annotation.StringRes;
import androidx.core.util.Preconditions;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.main.weekly.WeeklyChartResourceFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeeklyChartResourceFormatterImpl implements WeeklyChartResourceFormatter {

    private final ResourceInteractor resourceInteractor;

    public WeeklyChartResourceFormatterImpl(final ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    @Override
    public String getWeeklyChartTitle(final int weeksBeforeNow) {
        final long actualWeekTimestamp = TimeHelper.addWeeks(-1 * weeksBeforeNow, TimeHelper.getEarliestTimeAtGivenWeek(System.currentTimeMillis()));
        final Date actualDate = new Date(actualWeekTimestamp);
        final String pattern = resourceInteractor.getStringResource(R.string.weekly_subtitle_dateformat,
                resourceInteractor.getStringResource(getDayOfMothSuffix(TimeHelper.getDayOfMonth(actualWeekTimestamp))));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, getDefault());
        final String formattedDate = simpleDateFormat.format(actualDate);

        return resourceInteractor.getStringResource(R.string.weekly_subtitle, formattedDate);
    }

    @Override
    public String getPdfFileNameDateFormat() {
        final Date date = Calendar.getInstance().getTime();

        final SimpleDateFormat df = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.pdf_export_file_name_dateformat), getDefault());
        return df.format(date);
    }

    @Override
    public String getPdfMonthFormat(final int monthsBeforeNow) {
        final long actualMonthTimestamp = TimeHelper.addMonths(-1 * monthsBeforeNow, System.currentTimeMillis());
        final Date actualDate = new Date(actualMonthTimestamp);
        final SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(resourceInteractor.getStringResource(R.string.pdf_export_subtitle_dateformat), getDefault());

        final String format = simpleDateFormat.format(actualDate);
        return format.substring(0, 1).toUpperCase() + format.substring(1);
    }

    @Override
    public String getFormattedCsvFileName() {
        final Date date = Calendar.getInstance().getTime();

        final SimpleDateFormat df = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.csv_export_file_name_format), getDefault());
        df.format(date);
        return resourceInteractor.getStringResource(R.string.csv_export_file_name, df.format(date));
    }

    @Override
    public String getFormattedCsvColumn(final long timeInMillis) {
        final Date date = new Date(timeInMillis);

        final SimpleDateFormat df = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.csv_export_file_name_format), getDefault());
        return df.format(date);
    }

    @Override
    public String getFormattedCsvBody(final long startDate, final long endDate, final String version) {
        final SimpleDateFormat df = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.csv_export_body_format), getDefault());
        return resourceInteractor.getStringResource(R.string.csv_export_body, df.format(startDate), df.format(endDate), version);
    }

    @Override
    public String getFormattedRemindersCsvFileName() {
        final Date date = Calendar.getInstance().getTime();

        final SimpleDateFormat df = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.csv_export_file_name_format), getDefault());
        df.format(date);
        return resourceInteractor.getStringResource(R.string.csv_export_reminders_file_name, df.format(date));
    }

    @Override
    public String getFormattedPkuRangeInfoCsvFileName() {
        final Date date = Calendar.getInstance().getTime();

        final SimpleDateFormat df = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.csv_export_file_name_format), getDefault());
        df.format(date);
        return resourceInteractor.getStringResource(R.string.csv_export_pku_range_info_file_name, df.format(date));
    }

    @StringRes
    private int getDayOfMothSuffix(final int dayOfMonth) {
        Preconditions.checkArgument(
                dayOfMonth >= 1 && dayOfMonth <= 31,
                "Illegal day of month: " + dayOfMonth);

        if (dayOfMonth >= 11 && dayOfMonth <= 13) {
            return R.string.weekly_suffix_dayofmonth_nth;
        }

        switch (dayOfMonth % 10) {
            case 1:
                return R.string.weekly_suffix_dayofmonth_first;
            case 2:
                return R.string.weekly_suffix_dayofmonth_second;
            case 3:
                return R.string.weekly_suffix_dayofmonth_third;
            default:
                return R.string.weekly_suffix_dayofmonth_nth;
        }
    }

}
