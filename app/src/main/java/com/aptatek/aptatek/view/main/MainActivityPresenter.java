package com.aptatek.aptatek.view.main;

import android.text.format.DateUtils;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.respository.manager.FakeCubeDataManager;
import com.aptatek.aptatek.util.ChartUtils;
import com.aptatek.aptatek.view.main.adapter.ChartVM;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.aptatek.aptatek.util.CalendarUtils.*;

class MainActivityPresenter extends MvpBasePresenter<MainActivityView> {

    private static final String PATTERN_DAY = "MMM dd";
    private static final String PATTERN_WITH_TIME = "MMM dd - hh:mm a";

    private final FakeCubeDataManager fakeCubeDataManager;
    private final ChartUtils chartUtils;
    private final ResourceInteractor resourceInteractor;

    @Inject
    MainActivityPresenter(final FakeCubeDataManager fakeCubeDataManager,
                          final ChartUtils chartUtils,
                          final ResourceInteractor resourceInteractor) {
        this.fakeCubeDataManager = fakeCubeDataManager;
        this.chartUtils = chartUtils;
        this.resourceInteractor = resourceInteractor;
    }

    List<ChartVM> fakeData() {
        return chartUtils.asChartVMList(fakeCubeDataManager.listAll());
    }

    void itemChanged(final ChartVM chartVM) {
        final Date date = chartVM.getDate();
        final String subTitle = formatDate(date, chartVM.isEmpty() ? PATTERN_DAY : PATTERN_WITH_TIME);
        String title;

        if (DateUtils.isToday(date.getTime())) {
            title = resourceInteractor.getStringResource(R.string.main_title_today);
        } else if (DateUtils.isToday(date.getTime() + DateUtils.DAY_IN_MILLIS)) {
            title = resourceInteractor.getStringResource(R.string.main_title_yesterday);
        } else {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            title = dayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
        }

        ifViewAttached(view -> view.updateTitles(title, subTitle));
    }
}
