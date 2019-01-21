package com.aptatek.pkulab.view.main.weekly;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.cube.CubeInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.model.CubeData;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.util.ChartUtils;
import com.aptatek.pkulab.view.main.weekly.chart.PdfChartDataTransformer;
import com.aptatek.pkulab.view.main.weekly.pdf.PdfEntryData;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;
import timber.log.Timber;

public class WeeklyResultFragmentPresenter extends MvpBasePresenter<WeeklyResultFragmentView> {

    private static final int EMPTY_LIST = -1;

    private final CubeInteractor cubeInteractor;
    private final ResourceInteractor resourceInteractor;
    private final PkuRangeInteractor rangeInteractor;
    private final WeeklyChartDateFormatter weeklyChartDateFormatter;
    private final List<Integer> weekList = new ArrayList<>();
    private final PdfChartDataTransformer pdfChartDataTransformer;

    private CompositeDisposable disposables;

    @Inject
    public WeeklyResultFragmentPresenter(final CubeInteractor cubeInteractor,
                                         final ResourceInteractor resourceInteractor,
                                         final PkuRangeInteractor rangeInteractor,
                                         final WeeklyChartDateFormatter weeklyChartDateFormatter,
                                         final PdfChartDataTransformer pdfChartDataTransformer) {
        this.cubeInteractor = cubeInteractor;
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
        disposables.add(cubeInteractor.listAll()
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

    public void getPageForSelectedMonth(final int year, final int month) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        final int week = TimeHelper.getWeeksBetween(System.currentTimeMillis(), calendar.getTimeInMillis());
        Timber.d("");
    }

    void getPdfChartData(final int monthsBefore) {
        final long monthsBeforeTimeStamp = TimeHelper.addMonths(-1 * monthsBefore, System.currentTimeMillis());
        final long start = TimeHelper.getEarliestTimeAtGivenMonth(monthsBeforeTimeStamp);
        final long end = TimeHelper.getLatestTimeAtGivenMonth(monthsBeforeTimeStamp);

        final PkuRangeInfo pkuRangeInfo = rangeInteractor.getInfo().blockingGet();

        final PdfEntryData.Builder pdfEntryDataBuilder = PdfEntryData.builder()
                .setFormattedDate(weeklyChartDateFormatter.getPdfMonthFormat(weekList.size() - monthsBefore - 1))
                .setFileName(resourceInteractor.getStringResource(R.string.pdf_export_file_name, weeklyChartDateFormatter.getPdfFileNameDateFormat()))
                .setUnit(resourceInteractor.getStringResource(pkuRangeInfo.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL
                        ? R.string.rangeinfo_pkulevel_mmol
                        : R.string.rangeinfo_pkulevel_mg))
                .setNormalFloorValue(pkuRangeInfo.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL
                        ? String.valueOf((int) pkuRangeInfo.getNormalFloorValue())
                        : String.format(Locale.getDefault(), "%.2f", pkuRangeInfo.getNormalFloorValue()))
                .setNormalCeilValue(pkuRangeInfo.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL
                        ? String.valueOf((int) pkuRangeInfo.getNormalCeilValue())
                        : String.format(Locale.getDefault(), "%.2f", pkuRangeInfo.getNormalCeilValue()));

        disposables.add(cubeInteractor.listBetween(start, end)
                .toFlowable()
                .map(list -> {
                    int fastingCount = 0;
                    int sickCount = 0;
                    int low = 0;
                    int normal = 0;
                    int high = 0;
                    int veryHigh = 0;
                    float fullCount = 0;

                    for (CubeData cubeData : list) {
                        if (cubeData.isFasting()) {
                            fastingCount++;
                        }

                        if (pkuRangeInfo.getPkuLevelUnit() != cubeData.getPkuLevel().getUnit()) {
                            fullCount += PkuLevelConverter.convertTo(cubeData.getPkuLevel(), pkuRangeInfo.getPkuLevelUnit()).getValue();
                        } else {
                            fullCount += cubeData.getPkuLevel().getValue();
                        }

                        final ChartUtils.State state = ChartUtils.getState(cubeData.getPkuLevel(), pkuRangeInfo);

                        if (state == ChartUtils.State.LOW) {
                            low++;
                        } else if (state == ChartUtils.State.NORMAL) {
                            normal++;
                        } else if (state == ChartUtils.State.HIGH) {
                            high++;
                        } else if (state == ChartUtils.State.VERY_HIGH) {
                            veryHigh++;
                        }

                        if (cubeData.isSick()) {
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

                    return pdfEntryDataBuilder;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataSet ->
                        ifViewAttached(attachedView -> attachedView.onPdfDataReady(dataSet.build()))
                ));
    }

    List<Integer> getValidWeeks() {
        return weekList;
    }

    private double getDeviation(final List<CubeData> table) {

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

    private double mean(final List<CubeData> table) {
        int total = 0;

        for (int i = 0; i < table.size(); i++) {
            final float currentNum = table.get(i).getPkuLevel().getValue();
            total += currentNum;
        }
        return total / table.size();
    }
}
