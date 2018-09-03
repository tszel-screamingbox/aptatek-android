package com.aptatek.pkuapp.view.weekly.chart;

import com.aptatek.pkuapp.device.time.TimeHelper;
import com.aptatek.pkuapp.domain.interactor.cube.CubeInteractor;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuLevelUnits;
import com.aptatek.pkuapp.view.weekly.pdf.PdfEntryData;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class WeeklyChartPresenter extends MvpBasePresenter<WeeklyChartView> {

    private final CubeInteractor cubeInteractor;
    private final WeeklyChartDataTransformer weeklyChartDataTransformer;
    private final PdfChartDataTransformer pdfChartDataTransformer;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    WeeklyChartPresenter(final CubeInteractor cubeInteractor,
                         final WeeklyChartDataTransformer weeklyChartDataTransformer,
                         final PdfChartDataTransformer pdfChartDataTransformer) {
        this.cubeInteractor = cubeInteractor;
        this.weeklyChartDataTransformer = weeklyChartDataTransformer;
        this.pdfChartDataTransformer = pdfChartDataTransformer;
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

    void getPdfChartData(final int monthsBefore) {
        final long monthsBeforeTimeStamp = TimeHelper.addMonths(-1 * monthsBefore, System.currentTimeMillis());
        final long start = TimeHelper.getEarliestTimeAtGivenMonth(monthsBeforeTimeStamp);
        final long end = TimeHelper.getLatestTimeAtGivenMonth(monthsBeforeTimeStamp);

        disposables.add(cubeInteractor.listBetween(start, end)
                .toFlowable()
                .flatMapIterable(it -> it)
                .flatMapSingle(pdfChartDataTransformer::transform)
                .toList()
                .flatMap(pdfChartDataTransformer::transformEntries)
                // TODO Robi provide valid data
                .map(bubbleDataSet -> PdfEntryData.builder()
                        .setBubbleDataSet(bubbleDataSet)
                        .setDaysOfMonth(TimeHelper.getDaysBetween(start, end))
                        .setFastingCount(2)
                        .setSickCount(3)
                        .setAverageCount(10)
                        .setVeryHighCount(4)
                        .setHighCount(10)
                        .setNormalCount(13)
                        .setLowCount(10)
                        .setFormattedDate("May 2019")
                        .setUnit("hello")
                        .build())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataSet ->
                        ifViewAttached(attachedView -> attachedView.onPdfDataReady(dataSet))
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
