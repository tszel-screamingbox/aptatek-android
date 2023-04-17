package com.aptatek.pkulab.view.test.testcomplete;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class TestCompletePresenter extends TestBasePresenter<TestCompleteView> {

    private static final long INACTIVITY_TIMEOUT_MIN = 1L;

    private final ReaderInteractor readerInteractor;
    private final TestResultInteractor testResultInteractor;
    private Disposable disposable = null;

    private Disposable watchDeviceConnectedDisposable = null;

    private Disposable inactivityDisposable = null;

    private Disposable readyStateDisposable = null;

    private Disposable syncProgressDisposable = null;

    private String testId;

    @Inject
    public TestCompletePresenter(ResourceInteractor resourceInteractor, ReaderInteractor readerInteractor, TestResultInteractor testResultInteractor) {
        super(resourceInteractor);
        this.readerInteractor = readerInteractor;
        this.testResultInteractor = testResultInteractor;
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_testcomplete_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_testcomplete_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.test_complete), true);
            attachedView.setBatteryIndicatorVisible(false);
        });
    }

    public void onStart() {
        onStop();

        syncProgressDisposable = readerInteractor.syncProgressFlowable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sp -> ifViewAttached(av -> av.showSyncProgressDialog(sp)),
                        error -> Timber.w("--- syncProgress error: %s", error)
                );

        disposable = readerInteractor.syncMissingRecords()
                .andThen(testResultInteractor.getLatest().map(TestResult::getId))
                .subscribe(testId -> {
                            ifViewAttached(TestCompleteView::dismissProgressDialog);
                            onTestIdReceived(testId);
                        },
                        error -> Timber.w("--- syncMissingRecords error")
                );

        // If we detect disconnect, instantly show next button
        watchDeviceConnectedDisposable = readerInteractor.getReaderConnectionEvents()
                .filter(event -> event.getConnectionState() != ConnectionState.READY)
                .take(1)
                // wait 1 minute top!
                .timeout(INACTIVITY_TIMEOUT_MIN, TimeUnit.MINUTES)
                .ignoreElements()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> ifViewAttached(av -> av.setNextButtonVisible(true)),
                        error -> Timber.d("--- onTestIdReceived error waiting for connection events: %s", error)
                );
    }

    private void onTestIdReceived(final String testId) {
        this.testId = testId;

        // wait until SELF_CHECK / READY Wfs
        final Single<WorkflowState> readyStateSingle = readerInteractor.getWorkflowState("TCP:onTestIdReceived = " + testId)
                .share()
                .filter(wfs -> WorkflowState.SELF_TEST == wfs || WorkflowState.READY == wfs)
                .take(1)
                .lastOrError();

        readyStateDisposable = readyStateSingle.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ignored -> ifViewAttached(av -> av.showResults(testId)),
                        error -> Timber.w("--- readyState error: " + error)
                );

        inactivityDisposable = readyStateSingle
                // wait 1 minute
                .timeout(INACTIVITY_TIMEOUT_MIN, TimeUnit.MINUTES)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> {
                    ifViewAttached(av -> av.showResults(testId));
                }, error -> {
                    Timber.d("--- onTestIdReceived %s, error while waiting for WFS %s", testId, error);
                    // no worries, just timeout. enable next button
                    if (error instanceof TimeoutException) {
                        ifViewAttached(av -> av.setNextButtonVisible(true));
                    }
                });
    }

    public void showResult() {
        if (testId != null) {
            ifViewAttached(av -> av.showResults(testId));
        } else {
            onStart();
        }
    }

    public void onStop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }

        if (readyStateDisposable != null && !readyStateDisposable.isDisposed()) {
            readyStateDisposable.dispose();
            readyStateDisposable = null;
        }

        if (watchDeviceConnectedDisposable != null && !watchDeviceConnectedDisposable.isDisposed()) {
            watchDeviceConnectedDisposable.dispose();
            watchDeviceConnectedDisposable = null;
        }

        if (inactivityDisposable != null && !inactivityDisposable.isDisposed()) {
            inactivityDisposable.dispose();
            inactivityDisposable = null;
        }

        if (syncProgressDisposable != null && !syncProgressDisposable.isDisposed()) {
            syncProgressDisposable.dispose();
            syncProgressDisposable = null;
        }
    }
}
