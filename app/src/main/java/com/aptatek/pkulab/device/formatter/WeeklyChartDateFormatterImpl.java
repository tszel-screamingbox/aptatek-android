package com.aptatek.pkulab.device.formatter;

import android.support.annotation.StringRes;
import android.support.v4.util.Preconditions;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.weekly.WeeklyChartDateFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeeklyChartDateFormatterImpl implements WeeklyChartDateFormatter {

    private final ResourceInteractor resourceInteractor;

    public WeeklyChartDateFormatterImpl(final ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    @Override
    public String getWeeklyChartTitle(final int weeksBeforeNow) {
        final long actualWeekTimestamp = TimeHelper.addWeeks(-1 * weeksBeforeNow, System.currentTimeMillis());
        final Date actualDate = new Date(actualWeekTimestamp);
        final String pattern = resourceInteractor.getStringResource(R.string.weekly_subtitle_dateformat,
                resourceInteractor.getStringResource(getDayOfMothSuffix(TimeHelper.getDayOfMonth(actualWeekTimestamp))));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        final String formattedDate = simpleDateFormat.format(actualDate);

        return resourceInteractor.getStringResource(R.string.weekly_subtitle, formattedDate);
    }

    @Override
    public String getPdfFileNameDateFormat() {
        final Date date = Calendar.getInstance().getTime();

        final SimpleDateFormat df = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.pdf_export_file_name_dateformat), Locale.getDefault());
        return df.format(date);
    }

    @Override
    public String getPdfMonthFormat(final int weeksBeforeNow) {
        final long actualWeekTimestamp = TimeHelper.addWeeks(-1 * weeksBeforeNow, System.currentTimeMillis());
        final Date actualDate = new Date(actualWeekTimestamp);
        final SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(resourceInteractor.getStringResource(R.string.pdf_export_subtitle_dateformat), Locale.getDefault());

        final String format = simpleDateFormat.format(actualDate);
        return format.substring(0, 1).toUpperCase() + format.substring(1);
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
