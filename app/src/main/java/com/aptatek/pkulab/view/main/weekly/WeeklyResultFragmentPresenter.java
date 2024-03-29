package com.aptatek.pkulab.view.main.weekly;

import static com.aptatek.pkulab.domain.model.PkuLevelUnits.MICRO_MOL;
import static com.aptatek.pkulab.view.main.weekly.pdf.PdfExportInterval.LAST_HALF_YEAR;
import static com.aptatek.pkulab.view.main.weekly.pdf.PdfExportInterval.LAST_MONTH;
import static com.aptatek.pkulab.view.main.weekly.pdf.PdfExportInterval.LAST_THREE_MONTHS;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.device.time.TimeHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.weekly.ExportPdfCreated;
import com.aptatek.pkulab.domain.model.MonthPickerDialogModel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.util.ChartUtils;
import com.aptatek.pkulab.view.main.weekly.chart.PdfChartDataTransformer;
import com.aptatek.pkulab.view.main.weekly.csv.CsvExport;
import com.aptatek.pkulab.view.main.weekly.pdf.PdfEntryData;
import com.aptatek.pkulab.view.main.weekly.pdf.PdfExportInterval;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsValueFormatter;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;

public class WeeklyResultFragmentPresenter extends MvpBasePresenter<WeeklyResultFragmentView> {

    private final TestResultInteractor testResultInteractor;
    private final ResourceInteractor resourceInteractor;
    private final PkuRangeInteractor rangeInteractor;
    private final WeeklyChartResourceFormatter weeklyChartResourceFormatter;
    private final List<Integer> weekList = new ArrayList<>();
    private final PdfChartDataTransformer pdfChartDataTransformer;
    private final CsvExport csvExport;
    private final PreferenceManager preferenceManager;
    private final RangeSettingsValueFormatter valueFormatter;
    private final IAnalyticsManager analyticsManager;

    private CompositeDisposable disposables;

    @Inject
    public WeeklyResultFragmentPresenter(final TestResultInteractor testResultInteractor,
                                         final ResourceInteractor resourceInteractor,
                                         final PkuRangeInteractor rangeInteractor,
                                         final WeeklyChartResourceFormatter weeklyChartResourceFormatter,
                                         final PdfChartDataTransformer pdfChartDataTransformer,
                                         final CsvExport csvExport,
                                         final PreferenceManager preferenceManager,
                                         final RangeSettingsValueFormatter valueFormatter,
                                         final IAnalyticsManager analyticsManager) {
        this.testResultInteractor = testResultInteractor;
        this.resourceInteractor = resourceInteractor;
        this.rangeInteractor = rangeInteractor;
        this.weeklyChartResourceFormatter = weeklyChartResourceFormatter;
        this.pdfChartDataTransformer = pdfChartDataTransformer;
        this.csvExport = csvExport;
        this.preferenceManager = preferenceManager;
        this.valueFormatter = valueFormatter;
        this.analyticsManager = analyticsManager;
    }

    @Override
    public void attachView(final @NonNull WeeklyResultFragmentView view) {
        super.attachView(view);

        disposables = new CompositeDisposable();

        disposables.add(rangeInteractor.getInfo()
                .map(rangeInfo ->
                        resourceInteractor.getStringResource(R.string.weekly_chart_label,
                                resourceInteractor.getStringResource(rangeInfo.getPkuLevelUnit() == MICRO_MOL
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

    PkuLevelUnits getDefaultUnit() {
        return preferenceManager.getPkuRangeUnit();
    }

    void subTitle(final int page) {
        final String weeklyChartTitle = weeklyChartResourceFormatter.getWeeklyChartTitle(weekList.get(page));
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
    void loadValidWeeks() {
        disposables.add(testResultInteractor.listAll()
                .take(1)
                .map(testResults -> {
                    Ix.from(testResults).foreach(testResult -> {
                        final int week = TimeHelper.getWeeksBetween(TimeHelper.getEarliestTimeAtGivenWeek(testResult.getTimestamp()), TimeHelper.getEarliestTimeAtGivenWeek(System.currentTimeMillis()));
                        if (!weekList.contains(week)) {
                            weekList.add(week);
                        }
                    });

                    return Ix.from(weekList)
                            .orderBy(Integer::compare)
                            .reverse()
                            .toList();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(validWeeks -> {
                            if (validWeeks.isEmpty()) {
                                return;
                            }
                            weekList.clear();
                            weekList.addAll(validWeeks);
                            ifViewAttached(attachedView ->
                                    attachedView.displayValidWeekList(validWeeks)
                            );
                        }
                )
        );
    }

    void getCsvData() {
        disposables.add(testResultInteractor.listAll()
                .take(1)
                .singleOrError()
                .flatMap(results -> csvExport.generateAttachment(results, weeklyChartResourceFormatter.getFormattedCsvFileName()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(attachment -> ifViewAttached(view -> view.onCsvDataReady(attachment)))
        );
    }

    List<Integer> getValidWeeks() {
        return weekList;
    }

    public void getPageForSelectedMonth(final int year, final int month) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        disposables.add(testResultInteractor.listAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(testResults -> {
                    int week = -1;

                    for (int i = 0; i < testResults.size(); i++) {

                        final Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis(testResults.get(i).getTimestamp());

                        if (calendar1.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                            week = TimeHelper.getWeeksBetween(TimeHelper.getEarliestTimeAtGivenWeek(testResults.get(i).getTimestamp()), TimeHelper.getEarliestTimeAtGivenWeek(System.currentTimeMillis()));
                            break;
                        }
                    }

                    final int finalWeek = week;
                    ifViewAttached(attachedView -> {
                        if (finalWeek > 0) {
                            attachedView.scrollToItem(weekList.indexOf(finalWeek - 1));
                        } else {
                            attachedView.testNotFoundMonthPicker();
                        }
                    });
                }));
    }

    public void showMonthPickerDialog() {
        disposables.add(testResultInteractor.getOldest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(testResult -> {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(testResult.getTimestamp());

                    ifViewAttached(attachedView ->
                            attachedView.showMonthPickerDialog(MonthPickerDialogModel.builder()
                                    .setMinMonth(calendar.get(Calendar.MONTH + 1))
                                    .setMinYear(calendar.get(Calendar.YEAR))
                                    .build())
                    );
                }));
    }

    void getPdfChartData(final PdfExportInterval pdfExportInterval, final PkuLevelUnits units) {
        final List<Single<PdfEntryData>> singles = new ArrayList<>();

        for (int i = 0; i < getPdfExportIntervalInMonth(pdfExportInterval); i++) {
            final long monthsBeforeTimeStamp = TimeHelper.addMonths(i * -1, System.currentTimeMillis());
            final long start = TimeHelper.getEarliestTimeAtGivenMonth(monthsBeforeTimeStamp);
            final long end = TimeHelper.getLatestTimeAtGivenMonth(monthsBeforeTimeStamp);

            singles.add(generatePdfEntryDataForMonth(pdfExportInterval, i, start, end, units));
        }

        disposables.add(Single.concat(singles).toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((pdfEntryDataArrayList, throwable) -> ifViewAttached(view -> {
                    boolean isEmpty = true;

                    for (PdfEntryData pdfEntryData : pdfEntryDataArrayList) {
                        if (pdfEntryData.getBubbleDataSet().getEntryCount() > 0) {
                            isEmpty = false;
                            break;
                        }
                    }

                    if (isEmpty) {
                        view.testNotFoundPdfExport();
                    } else {
                        analyticsManager.logEvent(new ExportPdfCreated(getPdfExportIntervalInMonth(pdfExportInterval)));
                        view.onPdfDataReady(pdfEntryDataArrayList);
                    }
                })));
    }

    private Single<PdfEntryData> generatePdfEntryDataForMonth(final PdfExportInterval pdfExportInterval,
                                                              final int monthsBefore,
                                                              final long start,
                                                              final long end,
                                                              final PkuLevelUnits selectedUnit) {

        final PkuRangeInfo pkuRangeInfo = rangeInteractor.getInfoInUnit(selectedUnit);

        final PdfEntryData.Builder pdfEntryDataBuilder = PdfEntryData.builder()
                .setFormattedDate(weeklyChartResourceFormatter.getPdfMonthFormat(monthsBefore))
                .setFileName(getPdfExportFileName(pdfExportInterval))
                .setUnit(resourceInteractor.getStringResource(pkuRangeInfo.getPkuLevelUnit() == MICRO_MOL
                        ? R.string.rangeinfo_pkulevel_mmol
                        : R.string.rangeinfo_pkulevel_mg))
                .setStandardText(valueFormatter.formatStandardPdfEntry(pkuRangeInfo))
                .setIncreasedText(valueFormatter.formatIncreasedPdfEntry(pkuRangeInfo))
                .setHighText(valueFormatter.formatHighPdfEntry(pkuRangeInfo))
                .setVeryHighText(valueFormatter.formatVeryHighPdfEntry(pkuRangeInfo));

        return testResultInteractor.listBetween(start, end)
                .take(1)
                .map(list -> Ix.from(list)
                        .filter(TestResult::isValid)
                        .toList()
                )
                .map(list -> {
                    int low = 0;
                    int normal = 0;
                    int high = 0;
                    int veryHigh = 0;
                    float fullCount = 0;

                    final float min = searchMin(list, pkuRangeInfo);
                    final float max = searchMax(list, pkuRangeInfo);

                    for (TestResult testResult : list) {
                        if (pkuRangeInfo.getPkuLevelUnit() != testResult.getPkuLevel().getUnit()) {
                            fullCount += PkuLevelConverter.convertTo(testResult.getPkuLevel(), pkuRangeInfo.getPkuLevelUnit()).getValue();
                        } else {
                            fullCount += testResult.getPkuLevel().getValue();
                        }

                        final ChartUtils.State state = ChartUtils.getState(testResult.getPkuLevel(), pkuRangeInfo);

                        if (state == ChartUtils.State.STANDARD) {
                            low++;
                        } else if (state == ChartUtils.State.INCREASED) {
                            normal++;
                        } else if (state == ChartUtils.State.HIGH) {
                            high++;
                        } else if (state == ChartUtils.State.VERY_HIGH) {
                            veryHigh++;
                        }
                    }

                    final float average = list.size() != 0 ? (fullCount / list.size()) : 0;

                    pdfEntryDataBuilder
                            .setAverageCount(pkuRangeInfo.getPkuLevelUnit() == MICRO_MOL
                                    ? String.valueOf((int) average)
                                    : String.format(Locale.US, "%.1f", average))
                            .setMin(pkuRangeInfo.getPkuLevelUnit() == MICRO_MOL
                                    ? String.valueOf((int) min)
                                    : String.format(Locale.US, "%.1f", min))
                            .setMax(pkuRangeInfo.getPkuLevelUnit() == MICRO_MOL
                                    ? String.valueOf((int) max)
                                    : String.format(Locale.US, "%.1f", max))
                            .setLowCount(low)
                            .setNormalCount(normal)
                            .setHighCount(high)
                            .setVeryHighCount(veryHigh)
                            .setDeviation(getDeviation(list));

                    return list;
                })
                .flatMapIterable(it -> it)
                .flatMapSingle(testResult -> pdfChartDataTransformer.transform(testResult, pkuRangeInfo))
                .toList()
                .flatMap(pdfChartDataTransformer::transformEntries)
                .map(bubbleDataSet ->
                        pdfEntryDataBuilder
                                .setBubbleDataSet(bubbleDataSet)
                                .setDaysOfMonth(TimeHelper.getDaysBetween(start, end) + 1)
                                .build()
                );
    }

    private List<Float> resultList(final List<TestResult> table, final PkuRangeInfo rangeInfo) {
        return Ix.from(table)
                .map(result -> {
                    if (rangeInfo.getPkuLevelUnit() != result.getPkuLevel().getUnit()) {
                        return PkuLevelConverter.convertTo(result.getPkuLevel(), rangeInfo.getPkuLevelUnit()).getValue();
                    } else {
                        return result.getPkuLevel().getValue();
                    }
                }).toList();
    }

    private float searchMax(final List<TestResult> table, final PkuRangeInfo rangeInfo) {
        return Ix.from(resultList(table, rangeInfo))
                .orderBy(Float::compare)
                .last(0f);
    }

    private float searchMin(final List<TestResult> table, final PkuRangeInfo rangeInfo) {
        return Ix.from(resultList(table, rangeInfo))
                .orderBy(Float::compare)
                .first(0f);
    }

    private double getDeviation(final List<TestResult> table) {

        final double mean = mean(table);
        double temp = 0;

        for (int i = 0; i < table.size(); i++) {
            final float val = table.get(i).getPkuLevel().getValue();

            final double sqrDiffToMean = Math.pow(val - mean, 2);


            temp += sqrDiffToMean;
        }

        final double meanOfDiffs = temp / (double) (table.size());

        return Math.sqrt(meanOfDiffs);
    }

    private double mean(final List<TestResult> table) {
        int total = 0;

        if (table.isEmpty()) {
            return total;
        }

        for (int i = 0; i < table.size(); i++) {
            final float currentNum = table.get(i).getPkuLevel().getValue();
            total += currentNum;
        }
        return total / table.size();
    }

    private int getPdfExportIntervalInMonth(final PdfExportInterval pdfExportInterval) {
        if (pdfExportInterval == LAST_MONTH) {
            return 1;
        } else if (pdfExportInterval == LAST_THREE_MONTHS) {
            return 3;
        } else if (pdfExportInterval == LAST_HALF_YEAR) {
            return 6;
        } else {
            final TestResult oldest = testResultInteractor.getOldest().blockingGet();
            return TimeHelper.getMonthsBetween(oldest.getTimestamp(), System.currentTimeMillis());
        }
    }

    private String getPdfExportFileName(final PdfExportInterval pdfExportInterval) {
        if (pdfExportInterval == LAST_MONTH) {
            return resourceInteractor.getFormattedString(R.string.pdf_export_last_month_file_name, weeklyChartResourceFormatter.getPdfFileNameDateFormat());
        } else if (pdfExportInterval == LAST_THREE_MONTHS) {
            return resourceInteractor.getFormattedString(R.string.pdf_export_last_three_month_file_name, weeklyChartResourceFormatter.getPdfFileNameDateFormat());
        } else if (pdfExportInterval == LAST_HALF_YEAR) {
            return resourceInteractor.getFormattedString(R.string.pdf_export_last_six_month_file_name, weeklyChartResourceFormatter.getPdfFileNameDateFormat());
        } else {
            return resourceInteractor.getFormattedString(R.string.pdf_export_last_year_file_name, weeklyChartResourceFormatter.getPdfFileNameDateFormat());
        }
    }
}
