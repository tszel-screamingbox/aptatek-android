package com.aptatek.pkulab.view.settings.web;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.model.ReportIssue;
import com.aptatek.pkulab.view.main.weekly.WeeklyChartResourceFormatter;
import com.aptatek.pkulab.view.main.weekly.csv.CsvExport;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class WebPagePresenter extends MvpBasePresenter<WebPageView> {

    private CompositeDisposable disposables;
    private final CsvExport csvExport;
    private final TestResultInteractor testResultInteractor;
    private final WeeklyChartResourceFormatter weeklyChartResourceFormatter;
    private final DeviceHelper deviceHelper;
    private final ResourceInteractor resourceInteractor;

    @Inject
    public WebPagePresenter(final CsvExport csvExport,
                            final TestResultInteractor testResultInteractor,
                            final WeeklyChartResourceFormatter weeklyChartResourceFormatter,
                            final DeviceHelper deviceHelper,
                            final ResourceInteractor resourceInteractor) {
        this.csvExport = csvExport;
        this.testResultInteractor = testResultInteractor;
        this.weeklyChartResourceFormatter = weeklyChartResourceFormatter;
        this.deviceHelper = deviceHelper;
        this.resourceInteractor = resourceInteractor;
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

    void generateAttachment() {
        disposables.add(testResultInteractor.listAll()
                .flatMap(results -> csvExport.generateAttachment(results, weeklyChartResourceFormatter.getFormattedCsvFileName()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(attachment ->
                        ifViewAttached(view -> view.sendIssueEmail(
                                ReportIssue.create(attachment.getCsvFile(), resourceInteractor.getFormattedString(
                                        R.string.faq_report_issue_email_description,
                                        deviceHelper.getDeviceBrand(),
                                        deviceHelper.getDeviceModel(),
                                        deviceHelper.getAppVersion(),
                                        deviceHelper.getDeviceOsVersion(),
                                        deviceHelper.getAvailableBytes()),
                                        "test")
                        ))
                )
        );
    }
}
