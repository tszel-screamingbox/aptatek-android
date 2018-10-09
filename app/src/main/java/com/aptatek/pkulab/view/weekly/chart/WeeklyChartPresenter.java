package com.aptatek.pkulab.view.weekly.chart;

import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.cube.CubeInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class WeeklyChartPresenter extends MvpBasePresenter<WeeklyChartView> {

    private final CubeInteractor cubeInteractor;
    private final WeeklyChartDataTransformer weeklyChartDataTransformer;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    WeeklyChartPresenter(final CubeInteractor cubeInteractor,
                         final WeeklyChartDataTransformer weeklyChartDataTransformer) {
        this.cubeInteractor = cubeInteractor;
        this.weeklyChartDataTransformer = weeklyChartDataTransformer;
    }

    void getChartData(final int weekBefore) {
        Timber.d("Loading data from %s week(s) ago", weekBefore);
        final long weekBeforeTimestamp = TimeHelper.addWeeks(-1 * weekBefore, System.currentTimeMillis());
        final long start = TimeHelper.getEarliestTimeAtGivenWeek(weekBeforeTimestamp);
        final long end = TimeHelper.getLatestTimeAtGivenWeek(weekBeforeTimestamp);

        disposables.add(cubeInteractor.listBetween(start, end)
                .toFlowable()
                .flatMapIterable(it -> it)
                .flatMapSingle(weeklyChartDataTransformer::transform)
                .toList()
                .flatMap(weeklyChartDataTransformer::transformEntries)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataSet ->
                        ifViewAttached(attachedView -> attachedView.displayChartData(dataSet))
                ));

    }

    @Override
    public void detachView() {
        if (!disposables.isDisposed()) {
            disposables.clear();
        }

        super.detachView();
    }
}
