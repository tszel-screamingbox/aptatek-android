package com.aptatek.pkuapp.view.weekly.chart;

import com.aptatek.pkuapp.device.time.TimeHelper;
import com.aptatek.pkuapp.domain.interactor.cube.CubeInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class WeeklyChartPresenter extends MvpBasePresenter<WeeklyChartView> {

    private final CubeInteractor cubeInteractor;
    private final WeeklyChartDataTransformer weeklyChartDataTransformer;

    private Disposable disposable;

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

        disposable = cubeInteractor.listBetween(start, end)
                .toFlowable()
                .flatMapIterable(it -> it)
                .flatMapSingle(weeklyChartDataTransformer::transform)
                .toList()
                .flatMap(weeklyChartDataTransformer::transformEntries)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataSet ->
                        ifViewAttached(attachedView -> attachedView.displayChartData(dataSet))
                );

    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
