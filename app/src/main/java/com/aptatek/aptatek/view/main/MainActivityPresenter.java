package com.aptatek.aptatek.view.main;

import android.text.format.DateUtils;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.aptatek.aptatek.domain.respository.manager.FakeCubeDataManager;
import com.aptatek.aptatek.util.CalendarUtils;
import com.aptatek.aptatek.util.ChartUtils;
import com.aptatek.aptatek.util.StringUtils;
import com.aptatek.aptatek.view.main.adapter.ChartVM;
import com.aptatek.aptatek.view.main.adapter.DailyResultAdapterItem;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

class MainActivityPresenter extends MvpBasePresenter<MainActivityView> {

    public static final String PATTERN_DAY = "MMM dd";
    public static final String PATTERN_WITH_TIME = "MMM dd - hh:mm a";

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
        final List<ChartVM> chartVMS = chartUtils.asChartVMList(fakeCubeDataManager.listAll());
        chartVMS.set(chartVMS.size() - 1, chartVMS.get(chartVMS.size() - 1).toBuilder().setZoomed(true).build());
        return chartVMS;
    }

    void itemZoomIn(final ChartVM chartVM) {
        final Date date = chartVM.getDate();
        final String subTitle = CalendarUtils.formatDate(date, chartVM.getNumberOfMeasures() == 0 ? PATTERN_DAY : PATTERN_WITH_TIME);
        final String title;

        if (DateUtils.isToday(date.getTime())) {
            title = resourceInteractor.getStringResource(R.string.main_title_today);
        } else if (DateUtils.isToday(date.getTime() + DateUtils.DAY_IN_MILLIS)) {
            title = resourceInteractor.getStringResource(R.string.main_title_yesterday);
        } else {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            title = CalendarUtils.dayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
        }

        ifViewAttached(view -> {
            view.updateTitles(title, subTitle);
            view.changeItemZoomState(chartVM, chartVM.toBuilder().setZoomed(true).build());
        });
    }

    void itemZoomOut(final ChartVM chartVM) {
        ifViewAttached(view -> view.changeItemZoomState(chartVM, chartVM.toBuilder().setZoomed(false).build()));
    }

    void measureListToAdapterList(final List<PkuLevel> measures) {
        final List<DailyResultAdapterItem> dailyResultAdapterItems = new ArrayList<>();

        for (PkuLevel measure : measures) {
            final PkuRangeInfo userSettings = chartUtils.getUserSettings();
            final PkuLevel pkuLevelInSelectedUnit = userSettings.getPkuLevelUnit() == measure.getUnit()
                    ? measure
                    : PkuLevelConverter.convertTo(measure, userSettings.getPkuLevelUnit());
            final PkuLevel pkuLevelInAlternativeUnit = userSettings.getPkuLevelUnit() == measure.getUnit()
                    ? PkuLevelConverter.convertTo(measure, userSettings.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL ? PkuLevelUnits.MILLI_GRAM : PkuLevelUnits.MICRO_MOL)
                    : measure;

            final String alternativeText = chartUtils.format(pkuLevelInAlternativeUnit) + resourceInteractor.getStringResource(pkuLevelInAlternativeUnit.getUnit() == PkuLevelUnits.MICRO_MOL
                    ? R.string.rangeinfo_pkulevel_mmol
                    : R.string.rangeinfo_pkulevel_mg);

            final CharSequence details = StringUtils.highlightWord(
                    chartUtils.format(pkuLevelInSelectedUnit),
                    alternativeText);

            dailyResultAdapterItems.add(DailyResultAdapterItem.create(
                    details,
                    System.currentTimeMillis(),
                    chartUtils.getState(measure)));
        }

        ifViewAttached(view -> view.setMeasureList(dailyResultAdapterItems));
    }
}
