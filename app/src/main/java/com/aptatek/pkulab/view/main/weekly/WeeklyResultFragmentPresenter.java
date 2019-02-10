package com.aptatek.pkulab.view.main.weekly;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.util.ChartUtils;
import com.aptatek.pkulab.view.main.weekly.chart.PdfChartDataTransformer;
import com.aptatek.pkulab.view.main.weekly.pdf.PdfEntryData;
import com.aptatek.pkulab.view.main.weekly.pdf.PdfExportInterval;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;

public class WeeklyResultFragmentPresenter extends MvpBasePresenter<WeeklyResultFragmentView> {

    private static final int EMPTY_LIST = -1;

    private final TestResultInteractor testResultInteractor;
    private final ResourceInteractor resourceInteractor;
    private final PkuRangeInteractor rangeInteractor;
    private final WeeklyChartDateFormatter weeklyChartDateFormatter;
    private final List<Integer> weekList = new ArrayList<>();
    private final PdfChartDataTransformer pdfChartDataTransformer;

    private CompositeDisposable disposables;

    @Inject
    public WeeklyResultFragmentPresenter(final TestResultInteractor testResultInteractor,
                                         final ResourceInteractor resourceInteractor,
                                         final PkuRangeInteractor rangeInteractor,
                                         final WeeklyChartDateFormatter weeklyChartDateFormatter,
                                         final PdfChartDataTransformer pdfChartDataTransformer) {
        this.testResultInteractor = testResultInteractor;
        this.resourceInteractor = resourceInteractor;
        this.rangeInteractor = rangeInteractor;
        this.weeklyChartDateFormatter = weeklyChartDateFormatter;
        this.pdfChartDataTransformer = pdfChartDataTransformer;
    }

    @Override
    public void attachView(final @NonNull WeeklyResultFragmentView view) {
        super.attachView(view);

        disposables = new CompositeDisposable();

        disposables.add(rangeInteractor.getInfo()
                .map(rangeInfo ->
                        resourceInteractor.getStringResource(R.string.weekly_chart_label,
                                resourceInteractor.getStringResource(rangeInfo.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL
                                        ? R.string.rangeinfo_pkulevel_mmol
                                        : R.string.rangeinfo_pkulevel_mg)
                        )
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(label ->
                        ifViewAttached(attachedView ->
                                attachedView.displayUnitLabel(label)
                        )
                )
        );
    }

    @Override
    public void detachView() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }

        super.detachView();
    }

    void subTitle(final int page) {
        final String weeklyChartTitle = weeklyChartDateFormatter.getWeeklyChartTitle(weekList.get(page));
        ifViewAttached(view -> view.onSubtitleChanged(weeklyChartTitle));
    }

    void showPage(final int pageNum) {
        ifViewAttached(view -> view.onLoadNextPage(pageNum));
        updateArrows(pageNum);
    }

    void updateArrows(final int page) {
        ifViewAttached(view -> view.onUpdateLeftArrow(page != 0));
        ifViewAttached(view -> view.onUpdateRightArrow(page != weekList.size() - 1));
    }

    // TODO should not get ALL data at once...
    public void loadValidWeeks() {
        disposables.add(testResultInteractor.listAll()
                .map(cubeDataList -> {
                    Ix.from(cubeDataList).foreach(cubeData -> {
                        final int week = TimeHelper.getWeeksBetween(cubeData.getTimestamp(), System.currentTimeMillis());
                        if (!weekList.contains(week) && cubeData.getPkuLevel().getValue() >= 0) {
                            weekList.add(week);
                        }
                    });

                    if (weekList.isEmpty()) {
                        weekList.add(EMPTY_LIST);
                    }
                    Collections.reverse(weekList);

                    return weekList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(validWeeks ->
                        ifViewAttached(attachedView ->
                                attachedView.displayValidWeekList(validWeeks)
                        )
                )
        );
    }

    List<Integer> getValidWeeks() {
        return weekList;
    }

    void getPdfChartData(final PdfExportInterval pdfExportInterval) {
        final ArrayList<Single<PdfEntryData>> singles = new ArrayList<>();

        for (int i = 0; i < getPdfExportIntervalInMonth(pdfExportInterval); i++) {
            final long monthsBeforeTimeStamp = TimeHelper.addMonths(i, System.currentTimeMillis());
            final long start = TimeHelper.getEarliestTimeAtGivenMonth(monthsBeforeTimeStamp);
            final long end = TimeHelper.getLatestTimeAtGivenMonth(monthsBeforeTimeStamp);

            singles.add(generatePdfEntryDataForMonth(pdfExportInterval, i, start, end));
        }

        disposables.add(Single.zip(singles, objects -> {
            final ArrayList<PdfEntryData> data = new ArrayList<>();
            for (Object object : objects) {
                data.add((PdfEntryData) object);
            }
            return data;
        }).subscribeOn(Schedulers.computation()).subscribe((pdfEntryDataArrayList, throwable) -> {
            ifViewAttached(view -> {
                view.onPdfDataReady(pdfEntryDataArrayList);
            });
        }));
    }

    private Single<PdfEntryData> generatePdfEntryDataForMonth(final PdfExportInterval pdfExportInterval, final int monthsBefore, final long start, final long end) {

        final PkuRangeInfo pkuRangeInfo = rangeInteractor.getInfo().blockingGet();

        final PdfEntryData.Builder pdfEntryDataBuilder = PdfEntryData.builder()
                .setFormattedDate(weeklyChartDateFormatter.getPdfMonthFormat(monthsBefore))
                .setFileName(getPdfExportFileName(pdfExportInterval))
                .setUnit(resourceInteractor.getStringResource(pkuRangeInfo.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL
                        ? R.string.rangeinfo_pkulevel_mmol
                        : R.string.rangeinfo_pkulevel_mg))
                .setNormalFloorValue(pkuRangeInfo.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL
                        ? String.valueOf((int) pkuRangeInfo.getNormalFloorValue())
                        : String.format(Locale.getDefault(), "%.2f", pkuRangeInfo.getNormalFloorValue()))
                .setNormalCeilValue(pkuRangeInfo.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL
                        ? String.valueOf((int) pkuRangeInfo.getNormalCeilValue())
                        : String.format(Locale.getDefault(), "%.2f", pkuRangeInfo.getNormalCeilValue()));

        return testResultInteractor.listBetween(start, end)
                .toFlowable()
                .map(list -> {
                    int fastingCount = 0;
                    int sickCount = 0;
                    int low = 0;
                    int normal = 0;
                    int high = 0;
                    int veryHigh = 0;
                    float fullCount = 0;

                    for (TestResult testResult : list) {
                        if (testResult.isFasting()) {
                            fastingCount++;
                        }

                        if (pkuRangeInfo.getPkuLevelUnit() != testResult.getPkuLevel().getUnit()) {
                            fullCount += PkuLevelConverter.convertTo(testResult.getPkuLevel(), pkuRangeInfo.getPkuLevelUnit()).getValue();
                        } else {
                            fullCount += testResult.getPkuLevel().getValue();
                        }

                        final ChartUtils.State state = ChartUtils.getState(testResult.getPkuLevel(), pkuRangeInfo);

                        if (state == ChartUtils.State.LOW) {
                            low++;
                        } else if (state == ChartUtils.State.NORMAL) {
                            normal++;
                        } else if (state == ChartUtils.State.HIGH) {
                            high++;
                        } else if (state == ChartUtils.State.VERY_HIGH) {
                            veryHigh++;
                        }

                        if (testResult.isSick()) {
                            sickCount++;
                        }

                        pdfEntryDataBuilder
                                .setFastingCount(fastingCount)
                                .setLowCount(low)
                                .setNormalCount(normal)
                                .setHighCount(high)
                                .setVeryHighCount(veryHigh)
                                .setSickCount(sickCount);
                    }

                    pdfEntryDataBuilder
                            .setAverageCount((int) (fullCount / list.size()))
                            .setDeviation(getDeviation(list));

                    return list;
                })
                .flatMapIterable(it -> it)
                .flatMapSingle(pdfChartDataTransformer::transform)
                .toList()
                .flatMap(pdfChartDataTransformer::transformEntries)
                .map(bubbleDataSet -> {

                    pdfEntryDataBuilder
                            .setBubbleDataSet(bubbleDataSet)
                            .setDaysOfMonth(TimeHelper.getDaysBetween(start, end));

                    return pdfEntryDataBuilder.build();
                });
    }

    private double getDeviation(final List<TestResult> table) {

        final double mean = mean(table);
        double temp = 0;

        for (int i = 0; i < table.size(); i++) {
            final float val = table.get(i).getPkuLevel().getValue();

            final double squrDiffToMean = Math.pow(val - mean, 2);


            temp += squrDiffToMean;
        }

        final double meanOfDiffs = temp / (double) (table.size());

        return Math.sqrt(meanOfDiffs);
    }

    private double mean(final List<TestResult> table) {
        int total = 0;

        for (int i = 0; i < table.size(); i++) {
            final float currentNum = table.get(i).getPkuLevel().getValue();
            total += currentNum;
        }
        return total / table.size();
    }

    private int getPdfExportIntervalInMonth(final PdfExportInterval pdfExportInterval) {
        if (pdfExportInterval == PdfExportInterval.LAST_MONTH) {
            return 1;
        } else if (pdfExportInterval == PdfExportInterval.LAST_THREE_MONTHS) {
            return 3;
        } else if (pdfExportInterval == PdfExportInterval.LAST_HALF_YEAR) {
            return 6;
        } else {
            return 12;
        }
    }

    private String getPdfExportFileName(final PdfExportInterval pdfExportInterval) {
        if (pdfExportInterval == PdfExportInterval.LAST_MONTH) {
            return resourceInteractor.getFormattedString(R.string.pdf_export_last_month_file_name, weeklyChartDateFormatter.getPdfFileNameDateFormat());
        } else if (pdfExportInterval == PdfExportInterval.LAST_THREE_MONTHS) {
            return resourceInteractor.getFormattedString(R.string.pdf_export_last_three_month_file_name, weeklyChartDateFormatter.getPdfFileNameDateFormat());
        } else if (pdfExportInterval == PdfExportInterval.LAST_HALF_YEAR) {
            return resourceInteractor.getFormattedString(R.string.pdf_export_last_six_month_file_name, weeklyChartDateFormatter.getPdfFileNameDateFormat());
        } else {
            return resourceInteractor.getFormattedString(R.string.pdf_export_last_year_file_name, weeklyChartDateFormatter.getPdfFileNameDateFormat());
        }
    }
}
