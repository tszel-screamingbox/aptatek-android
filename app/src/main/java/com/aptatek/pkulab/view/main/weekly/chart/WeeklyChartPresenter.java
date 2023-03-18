package com.aptatek.pkulab.view.main.weekly.chart;

import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

class WeeklyChartPresenter extends MvpBasePresenter<WeeklyChartView> {

    private final TestResultInteractor testResultInteractor;
    private final WeeklyChartDataTransformer weeklyChartDataTransformer;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    WeeklyChartPresenter(final TestResultInteractor testResultInteractor,
                         final WeeklyChartDataTransformer weeklyChartDataTransformer) {
        this.testResultInteractor = testResultInteractor;
        this.weeklyChartDataTransformer = weeklyChartDataTransformer;
    }

    void getChartData(final int weekBefore) {
        Timber.d("Loading data from %s week(s) ago", weekBefore);
        final long weekBeforeTimestamp = TimeHelper.addWeeks(-1 * weekBefore, System.currentTimeMillis());
        final long start = TimeHelper.getEarliestTimeAtGivenWeek(weekBeforeTimestamp);
        final long end = TimeHelper.getLatestTimeAtGivenWeek(weekBeforeTimestamp);

        disposables.add(testResultInteractor.listBetween(start, end)
                .take(1)
                .flatMapIterable(it -> it)
                .filter(TestResult::isValid)
                .flatMapSingle(weeklyChartDataTransformer::transform)
                .toList()
                .flatMap(weeklyChartDataTransformer::transformEntries)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataSet -> {
                            if (dataSet.getEntryCount() > 0) {
                                ifViewAttached(attachedView -> attachedView.displayChartData(dataSet));
                            }
                        }
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
