package com.aptatek.pkuapp.view.main;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Pair;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.cube.CubeInteractor;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkuapp.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkuapp.domain.interactor.wetting.WettingStatus;
import com.aptatek.pkuapp.domain.model.CubeData;
import com.aptatek.pkuapp.util.ChartUtils;
import com.aptatek.pkuapp.view.main.adapter.chart.ChartVM;
import com.aptatek.pkuapp.view.main.adapter.daily.DailyChartFormatter;
import com.aptatek.pkuapp.view.main.adapter.daily.DailyResultAdapterItem;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;

class MainActivityPresenter extends MvpBasePresenter<MainActivityView> {

    private final CubeInteractor cubeInteractor;
    private final ResourceInteractor resourceInteractor;
    private final PkuRangeInteractor rangeInteractor;
    private final DailyChartFormatter dailyChartFormatter;
    private final WettingInteractor wettingInteractor;

    private CompositeDisposable disposables;

    @Inject
    MainActivityPresenter(final CubeInteractor cubeInteractor,
                          final ResourceInteractor resourceInteractor,
                          final PkuRangeInteractor rangeInteractor,
                          final DailyChartFormatter dailyChartFormatter,
                          final WettingInteractor wettingInteractor) {
        this.cubeInteractor = cubeInteractor;
        this.resourceInteractor = resourceInteractor;
        this.rangeInteractor = rangeInteractor;
        this.dailyChartFormatter = dailyChartFormatter;
        this.wettingInteractor = wettingInteractor;
    }

    // TODO should load data on demand, per weeks / pages... Getting the whole dataSet will have perf impacts
    void loadData() {
        disposables.add(
                rangeInteractor.getInfo()
                        .flatMap(rangeInfo ->
                                cubeInteractor.listAll()
                                        .map(list -> new Pair<>(rangeInfo, list))
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(pair -> {
                            final List<ChartVM> chartVMS = ChartUtils.asChartVMList(pair.second, pair.first);
                            final ChartVM lastResult = chartVMS.get(chartVMS.size() - 1).toBuilder().setZoomed(true).build();
                            chartVMS.set(chartVMS.size() - 1, lastResult);

                            ifViewAttached(attachedView -> {
                                attachedView.updateTitles(
                                        formatTitle(lastResult),
                                        dailyChartFormatter.formatDate(lastResult.getDate().getTime(), lastResult.getNumberOfMeasures() > 0));
                                attachedView.displayData(chartVMS);
                            });
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

    void measureListToAdapterList(final List<CubeData> measures) {
        disposables.add(
                rangeInteractor.getInfo()
                        .map(rangeInfo ->
                                Ix.from(measures)
                                        .map(cubeData -> {
                                            final CharSequence details = dailyChartFormatter.getBubbleText(cubeData.getPkuLevel());
                                            final ChartUtils.State state = ChartUtils.getState(cubeData.getPkuLevel(), rangeInfo);
                                            return DailyResultAdapterItem.create(
                                                    details,
                                                    cubeData.getTimestamp(),
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
    public void attachView(final @NonNull MainActivityView view) {
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

    public void checkRunningTest() {
        disposables.add(
                wettingInteractor.getWettingStatus()
                .filter(wettingStatus -> wettingStatus != WettingStatus.NOT_STARTED)
                .subscribe(ignored ->
                    ifViewAttached(MainActivityView::navigateToTestScreen)
                )
        );
    }

    public void startNewTest() {
        disposables.add(wettingInteractor.resetWetting()
            .subscribe(() -> ifViewAttached(MainActivityView::navigateToTestScreen))
        );
    }

    private String formatTitle(final ChartVM chartVM) {
        final String title;

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
