package com.aptatek.pkulab.view.main.home;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Pair;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingStatus;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.util.ChartUtils;
import com.aptatek.pkulab.view.main.home.adapter.chart.ChartVM;
import com.aptatek.pkulab.view.main.home.adapter.daily.DailyChartFormatter;
import com.aptatek.pkulab.view.main.home.adapter.daily.DailyResultAdapterItem;
import com.aptatek.pkulab.view.rangeinfo.PkuValueFormatter;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;


class HomeFragmentPresenter extends MvpBasePresenter<HomeFragmentView> {

    private static final int NUMBERS_OF_MONTHS = 6;

    private final TestResultInteractor testResultInteractor;
    private final ResourceInteractor resourceInteractor;
    private final PkuRangeInteractor rangeInteractor;
    private final DailyChartFormatter dailyChartFormatter;
    private final WettingInteractor wettingInteractor;
    private final PreferenceManager preferenceManager;
    private final TestInteractor testInteractor;
    private final PkuValueFormatter pkuValueFormatter;
    private CompositeDisposable disposables;
    private ChartVM lastResult;

    @Inject
    HomeFragmentPresenter(final TestResultInteractor testResultInteractor,
                          final ResourceInteractor resourceInteractor,
                          final PkuRangeInteractor rangeInteractor,
                          final DailyChartFormatter dailyChartFormatter,
                          final WettingInteractor wettingInteractor,
                          final PreferenceManager preferenceManager,
                          final TestInteractor testInteractor,
                          final PkuValueFormatter pkuValueFormatter) {
        this.testResultInteractor = testResultInteractor;
        this.resourceInteractor = resourceInteractor;
        this.rangeInteractor = rangeInteractor;
        this.dailyChartFormatter = dailyChartFormatter;
        this.wettingInteractor = wettingInteractor;
        this.preferenceManager = preferenceManager;
        this.testInteractor = testInteractor;
        this.pkuValueFormatter = pkuValueFormatter;
    }

    void initView() {
        final String unit = pkuValueFormatter.formatFromUnits(preferenceManager.getPkuRangeUnit());
        ifViewAttached(view -> view.updateUnitText(unit));
    }

    // TODO should load data on demand, per weeks / pages... Getting the whole dataSet will have perf impacts
    void loadData() {
        disposables.add(
                rangeInteractor.getInfo()
                        .flatMap(rangeInfo -> testResultInteractor.getLatest()
                                .map(TestResult::getTimestamp)
                                .onErrorReturn(error -> System.currentTimeMillis())
                                .flatMap(timeStamp -> {
                                    final long past = TimeHelper.addMonths(-NUMBERS_OF_MONTHS, timeStamp);
                                    return testResultInteractor.listBetween(past, timeStamp);
                                }).map(list -> new Pair<>(rangeInfo, list)))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(pair -> {
                            if (pair.second.isEmpty()) {
                                ifViewAttached(HomeFragmentView::showNoResults);
                            } else {
                                final List<ChartVM> chartVMS = ChartUtils.asChartVMList(pair.second, pair.first);
                                lastResult = chartVMS.get(chartVMS.size() - 1).toBuilder().setZoomed(true).build();
                                chartVMS.set(chartVMS.size() - 1, lastResult);

                                ifViewAttached(attachedView -> {
                                    attachedView.updateTitles(
                                            formatTitle(lastResult),
                                            dailyChartFormatter.formatDate(lastResult.getDate().getTime(), lastResult.getNumberOfMeasures() > 0));
                                    attachedView.displayData(chartVMS);
                                });
                            }
                        })
        );
    }

    void itemZoomIn(final ChartVM chartVM) {
        ifViewAttached(view -> {
            view.updateTitles(
                    formatTitle(chartVM),
                    dailyChartFormatter.formatDate(chartVM.getDate().getTime(), chartVM.getNumberOfMeasures() > 0));
            view.changeItemZoomState(chartVM, chartVM.toBuilder().setZoomed(true).build());
        });
    }

    void itemZoomOut(final ChartVM chartVM) {
        ifViewAttached(view -> view.changeItemZoomState(chartVM, chartVM.toBuilder().setZoomed(false).build()));
    }

    void measureListToAdapterList(final List<TestResult> measures) {
        disposables.add(
                rangeInteractor.getInfo()
                        .map(rangeInfo ->
                                Ix.from(measures)
                                        .map(testResult -> {
                                            final ChartUtils.State state = ChartUtils.getState(testResult.getPkuLevel(), rangeInfo);
                                            return DailyResultAdapterItem.create(
                                                    dailyChartFormatter.getBubbleValue(testResult.getPkuLevel()),
                                                    testResult.getTimestamp(),
                                                    ChartUtils.smallBubbleBackground(state),
                                                    ChartUtils.stateColor(state));
                                        })
                                        .orderBy((o1, o2) -> Long.compare(o1.getTimestamp(), o2.getTimestamp()))
                                        .toList()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(adapterItems ->
                                ifViewAttached(view -> view.setMeasureList(adapterItems))
                        )
        );
    }

    @Override
    public void attachView(final @NonNull HomeFragmentView view) {
        super.attachView(view);

        disposables = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }

        super.detachView();
    }

    void checkRunningTest() {
        disposables.add(
                wettingInteractor.getWettingStatus()
                        .filter(wettingStatus -> wettingStatus != WettingStatus.NOT_STARTED)
                        .subscribe(ignored ->
                                ifViewAttached(HomeFragmentView::navigateToTestScreen)
                        )
        );
    }

    void startNewTest() {
        disposables.add(wettingInteractor.resetWetting()
                .andThen(testInteractor.resetTest())
                .subscribe(() -> ifViewAttached(HomeFragmentView::navigateToTestScreen))
        );
    }

    private String formatTitle(final ChartVM chartVM) {
        final String title;

        if (lastResult != null &&
                org.apache.commons.lang3.time.DateUtils.isSameDay(chartVM.getDate(), lastResult.getDate())) {
            return resourceInteractor.getStringResource(R.string.main_title_today);
        }

        if (DateUtils.isToday(chartVM.getDate().getTime())) {
            title = resourceInteractor.getStringResource(R.string.main_title_today);
        } else if (DateUtils.isToday(chartVM.getDate().getTime() + DateUtils.DAY_IN_MILLIS)) {
            title = resourceInteractor.getStringResource(R.string.main_title_yesterday);
        } else {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(chartVM.getDate());
            title = dailyChartFormatter.getNameOfDay(chartVM.getDate().getTime());
        }
        return title;
    }
}
