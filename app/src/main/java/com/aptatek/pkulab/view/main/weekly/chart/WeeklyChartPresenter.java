package com.aptatek.pkulab.view.main.weekly.chart;

import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class WeeklyChartPresenter extends MvpBasePresenter<WeeklyChartView> {

    private final TestResultInteractor testResultInteractor;
    private final WeeklyChartDataTransformer weeklyChartDataTransformer;
    private final RandomGenerator randomGenerator;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    WeeklyChartPresenter(final TestResultInteractor testResultInteractor,
                         final WeeklyChartDataTransformer weeklyChartDataTransformer,
                         final RandomGenerator randomGenerator) {
        this.testResultInteractor = testResultInteractor;
        this.weeklyChartDataTransformer = weeklyChartDataTransformer;
        this.randomGenerator = randomGenerator;
    }

    void getChartData(final int weekBefore) {
        Timber.d("Loading data from %s week(s) ago", weekBefore);
        final long weekBeforeTimestamp = TimeHelper.addWeeks(-1 * weekBefore, System.currentTimeMillis());
        final long start = TimeHelper.getEarliestTimeAtGivenWeek(weekBeforeTimestamp);
        final long end = TimeHelper.getLatestTimeAtGivenWeek(weekBeforeTimestamp);

        disposables.add(testResultInteractor.listBetween(start, end)
                .toFlowable()
                .flatMapIterable(it -> it)
                .flatMapSingle(weeklyChartDataTransformer::transform)
                .toList()
                .flatMap(weeklyChartDataTransformer::transformEntries)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataSet ->
                        ifViewAttached(view -> {
                            //TODO the random part was added for empty chart testing, the reason for adding it here that I didn't want to affect the other part of the app with it
                            //TODO remove it as soon as we can get valid data from the device
                            if (dataSet.getEntryCount() != 0 && randomGenerator.maybe()) {
                                view.displayChartData(dataSet);
                            }
                        })
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
