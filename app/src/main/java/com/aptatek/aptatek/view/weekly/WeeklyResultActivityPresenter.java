package com.aptatek.aptatek.view.weekly;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.respository.manager.FakeCubeDataManager;
import com.aptatek.aptatek.util.CalendarUtils;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import static com.aptatek.aptatek.util.CalendarUtils.dayNumberSuffix;

public class WeeklyResultActivityPresenter extends MvpBasePresenter<WeeklyResultActivityView> {

    private static final int WEEK_IN_DAYS = 7;

    private final FakeCubeDataManager fakeCubeDataManager;
    private final ResourceInteractor resourceInteractor;

    @Inject
    public WeeklyResultActivityPresenter(final FakeCubeDataManager fakeCubeDataManager,
                                         final ResourceInteractor resourceInteractor) {
        this.fakeCubeDataManager = fakeCubeDataManager;
        this.resourceInteractor = resourceInteractor;
    }

    int numberOfWeeks() {
        return 12;
    }

    void subTitle(final int week) {
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -WEEK_IN_DAYS * week);
        calendar.getTime();
        final String pattern = resourceInteractor.getStringResource(R.string.weekly_subtitle_dateformat, dayNumberSuffix(calendar.get(Calendar.DAY_OF_MONTH)));
        final String formatDate = CalendarUtils.formatDate(calendar.getTime(), pattern);
        final String subtitle = resourceInteractor.getStringResource(R.string.weekly_subtitle, formatDate);
        ifViewAttached(view -> view.onSubtitleChanged(subtitle));
    }

    void showPage(final int pageNum) {


    }
}
