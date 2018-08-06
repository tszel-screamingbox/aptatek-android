package com.aptatek.aptatek.device.formatter;

import android.support.annotation.StringRes;
import android.support.v4.util.Preconditions;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.device.time.TimeHelper;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.view.weekly.WeeklyChartDateFormatter;

import java.text.SimpleDateFormat;
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
