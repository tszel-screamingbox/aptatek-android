package com.aptatek.aptatek.view.weekly;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.respository.manager.FakeCubeDataManager;
import com.aptatek.aptatek.util.CalendarUtils;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ix.Ix;

import static com.aptatek.aptatek.util.CalendarUtils.dayNumberSuffix;

public class WeeklyResultActivityPresenter extends MvpBasePresenter<WeeklyResultActivityView> {

    private static final int EMPTY_LIST = -1;

    private final FakeCubeDataManager fakeCubeDataManager;
    private final ResourceInteractor resourceInteractor;
    private final List<Integer> weekList = new ArrayList<>();

    @Inject
    public WeeklyResultActivityPresenter(final FakeCubeDataManager fakeCubeDataManager,
                                         final ResourceInteractor resourceInteractor) {
        this.fakeCubeDataManager = fakeCubeDataManager;
        this.resourceInteractor = resourceInteractor;
    }

    List<Integer> validWeekList() {
        Ix.from(fakeCubeDataManager.listAll()).foreach(cubeData -> {
            final int week = CalendarUtils.differenceInWeeks(cubeData.getDate(), new Date());
            if (!weekList.contains(week) && cubeData.getMeasure().getValue() >= 0) {
                weekList.add(week);
            }
        });

        if (weekList.isEmpty()) {
            weekList.add(EMPTY_LIST);
        }
        Collections.reverse(weekList);
        return weekList;
    }

    void subTitle(final int page) {
        final Date actualDate = CalendarUtils.weekAgo(weekList.get(page));
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(actualDate);
        final String pattern = resourceInteractor.getStringResource(R.string.weekly_subtitle_dateformat, dayNumberSuffix(calendar.get(Calendar.DAY_OF_MONTH)));
        final String formatDate = CalendarUtils.formatDate(actualDate, pattern);
        final String subtitle = resourceInteractor.getStringResource(R.string.weekly_subtitle, formatDate);
        ifViewAttached(view -> view.onSubtitleChanged(subtitle));
    }

    void showPage(final int pageNum) {
        ifViewAttached(view -> view.onLoadNextPage(pageNum));
        updateArrows(pageNum);
    }

    void updateArrows(final int page) {
        ifViewAttached(view -> view.onUpdateLeftArrow(page != 0));
        ifViewAttached(view -> view.onUpdateRightArrow(page != validWeekList().size() - 1));
    }
}
