package com.aptatek.pkulab.view.settings.web.fragment;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.model.ReportIssue;
import com.aptatek.pkulab.domain.model.ReportIssueType;
import com.aptatek.pkulab.view.main.weekly.WeeklyChartResourceFormatter;
import com.aptatek.pkulab.view.main.weekly.csv.Attachment;
import com.aptatek.pkulab.view.main.weekly.csv.CsvExport;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WebPagePresenter extends MvpBasePresenter<WebPageView> {

    private CompositeDisposable disposables;
    private final CsvExport csvExport;
    private final TestResultInteractor testResultInteractor;
    private final WeeklyChartResourceFormatter weeklyChartResourceFormatter;
    private final DeviceHelper deviceHelper;
    private final ResourceInteractor resourceInteractor;
    private final ReminderInteractor reminderInteractor;
    private final PkuRangeInteractor pkuRangeInteractor;

    @Inject
    public WebPagePresenter(final CsvExport csvExport,
                            final TestResultInteractor testResultInteractor,
                            final WeeklyChartResourceFormatter weeklyChartResourceFormatter,
                            final DeviceHelper deviceHelper,
                            final ResourceInteractor resourceInteractor,
                            final ReminderInteractor reminderInteractor,
                            final PkuRangeInteractor pkuRangeInteractor) {
        this.csvExport = csvExport;
        this.testResultInteractor = testResultInteractor;
        this.weeklyChartResourceFormatter = weeklyChartResourceFormatter;
        this.deviceHelper = deviceHelper;
        this.resourceInteractor = resourceInteractor;
        this.reminderInteractor = reminderInteractor;
        this.pkuRangeInteractor = pkuRangeInteractor;
    }

    @Override
    public void attachView(final @NonNull WebPageView view) {
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

    void generateAttachment(final ReportIssueType reportIssueType) {
        disposables.add(Single.merge(generateRangeInfoAttachment(), generateRemindersAttachment(), generateTestResultsAttachment())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .toList()
                .subscribe(attachments ->
                        ifViewAttached(view -> view.sendIssueEmail(ReportIssue.create(attachments, resourceInteractor.getFormattedString(
                                R.string.faq_report_issue_email_description,
                                deviceHelper.getDeviceBrand(),
                                deviceHelper.getDeviceModel(),
                                deviceHelper.getAppVersion(),
                                deviceHelper.getDeviceOsVersion(),
                                deviceHelper.getAvailableBytes()),
                                resourceInteractor.getFormattedString(R.string.faq_report_issue_email_title, getEmailTitle(reportIssueType)),
                                resourceInteractor.getStringResource(R.string.faq_report_issue_email_target))))));
    }

    private String getEmailTitle(final ReportIssueType reportIssueType) {
        if (reportIssueType == ReportIssueType.DATA_CORRUPTION) {
            return resourceInteractor.getStringResource(R.string.faq_report_issue_data_corruption);
        } else if (reportIssueType == ReportIssueType.CONNECTION_ERROR) {
            return resourceInteractor.getStringResource(R.string.faq_report_issue_connection_error);
        } else {
            return resourceInteractor.getStringResource(R.string.faq_report_issue_other);
        }
    }

    private Single<Attachment> generateRemindersAttachment() {
        return reminderInteractor.listReminderDays()
                .subscribeOn(Schedulers.computation())
                .flatMap(results ->
                        csvExport.generateRemindersAttachment(results, weeklyChartResourceFormatter.getFormattedRemindersCsvFileName())
                                .subscribeOn(Schedulers.computation()));
    }

    private Single<Attachment> generateRangeInfoAttachment() {
        return pkuRangeInteractor.getInfo()
                .subscribeOn(Schedulers.computation())
                .flatMap(results ->
                        csvExport.generatePkuRangeInfoAttachment(results, weeklyChartResourceFormatter.getFormattedPkuRangeInfoCsvFileName())
                                .subscribeOn(Schedulers.computation()));
    }

    private Single<Attachment> generateTestResultsAttachment() {
        return testResultInteractor.listAll()
                .take(1)
                .singleOrError()
                .subscribeOn(Schedulers.computation())
                .flatMap(results ->
                        csvExport.generateAttachment(results, weeklyChartResourceFormatter.getFormattedCsvFileName())
                                .subscribeOn(Schedulers.computation()));
    }
}
