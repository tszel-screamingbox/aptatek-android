package com.aptatek.pkulab.view.test.testing;

import android.util.Pair;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorModelConversionError;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.test.TestingDone;
import com.aptatek.pkulab.domain.manager.analytic.events.test.TestingScreenDisplayed;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.error.ErrorModel;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TestingPresenter extends TestBasePresenter<TestingView> {

    private final ReaderInteractor readerInteractor;
    private final IAnalyticsManager analyticsManager;
    private final ErrorInteractor errorInteractor;
    private final TestInteractor testInteractor;

    private final TestResultInteractor testResultInteractor;

    private long screenDisplayedAtMs = 0L;
    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("mm:ss");
    private Disposable countdownDisposable;

    private Disposable stillConnectedDisposable;

    private Disposable batteryUpdatesDisposable;

    private Disposable workflowStateDisposable;

    private TestProgress firstProgress;

    private Disposable testCompleteDisposable;

    private Disposable errorsDisposable;

    private Disposable testRunningDisposable;

    private AtomicBoolean stillOnThisScreen;


    @Inject
    public TestingPresenter(final ResourceInteractor resourceInteractor,
                            final ReaderInteractor readerInteractor,
                            final TestResultInteractor testResultInteractor,
                            final IAnalyticsManager analyticsManager,
                            final ErrorInteractor errorInteractor,
                            final TestInteractor testInteractor) {
        super(resourceInteractor);
        this.readerInteractor = readerInteractor;
        this.analyticsManager = analyticsManager;
        this.errorInteractor = errorInteractor;
        this.testInteractor = testInteractor;
        this.testResultInteractor = testResultInteractor;

        analyticsManager.logEvent(new TestingScreenDisplayed());
        screenDisplayedAtMs = System.currentTimeMillis();
    }

    public void onStart() {
        onStop();
        stillOnThisScreen = new AtomicBoolean(true);


        // watch for active connection
        stillConnectedDisposable = readerInteractor.getReaderConnectionEvents()
                .filter(a -> a.getConnectionState() != ConnectionState.READY)
                .take(1)
                .ignoreElements()
                .onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            ifViewAttached(TestingView::showTurnReaderOn);
                            onStop();
                        },
                        error -> Timber.d("--- testingPresenter stillConnectedDisposable error: %s", error)
                );

        // watch workflow state
        workflowStateDisposable = readerInteractor.getWorkflowState("TestingPresenter")
                .takeUntil(a -> !stillOnThisScreen.get())
                .subscribe(
                        this::handleWorkflowState,
                        error -> Timber.wtf("--- getWorkflowState error! %s", error),
                        () -> Timber.wtf("--- stopped workflowState updates")
                );

        // watch battery updates
        batteryUpdatesDisposable = readerInteractor.batteryLevelUpdates()
                // stop updates once we left the screen
                .takeUntil((Predicate<? super Integer>) a -> !stillOnThisScreen.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        batteryPercent -> ifViewAttached(attachedView -> attachedView.setBatteryPercentage(batteryPercent)),
                        error -> Timber.d("Error during battery level update: %s", error),
                        () -> Timber.d("Battery level updates complete")
                );
    }

    private void handleWorkflowState(WorkflowState workflowState) {
        final List<WorkflowState> testMayBeRunning = new ArrayList<>();
        testMayBeRunning.add(WorkflowState.TEST_RUNNING);
        testMayBeRunning.add(WorkflowState.TEST_COMPLETE);
        testMayBeRunning.add(WorkflowState.TEST_VALIDATION);
        testMayBeRunning.add(WorkflowState.POST_TEST);
        testMayBeRunning.add(WorkflowState.READING_CASSETTE);
        testMayBeRunning.add(WorkflowState.DETECTING_FLUID);
        testMayBeRunning.add(WorkflowState.HEATING_CASSETTE);

        if (workflowState.name().toLowerCase(Locale.getDefault()).endsWith("error")) {
            handleError(workflowState);
        } else if (workflowState == WorkflowState.TEST_RUNNING) {
            handleTestRunning();
        } else if (workflowState == WorkflowState.TEST_COMPLETE) {
            handleTestComplete();
        } else if (!testMayBeRunning.contains(workflowState)) {
            Timber.wtf("--- test not running: %s", workflowState);
            ifViewAttached(av -> av.onTestFinished(null));
        } else {
            Timber.wtf("--- other workflow state caught: %s", workflowState);
        }
    }

    private void handleTestComplete() {
        if (testCompleteDisposable != null && !testCompleteDisposable.isDisposed()) {
            testCompleteDisposable.dispose();
        }

        // stop countdown
        if (countdownDisposable != null && !countdownDisposable.isDisposed()) {
            countdownDisposable.dispose();
        }

        testCompleteDisposable = readerInteractor.readAndStoreResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (testResult) -> {
                            Timber.d("Test complete: %s", testResult.getId());
                            analyticsManager.logEvent(new TestingDone(Math.abs(System.currentTimeMillis() - screenDisplayedAtMs)));

                            ifViewAttached(attachedView -> attachedView.onTestFinished(testResult.getId()));
                        },
                        error -> Timber.w("--- testComplete error %s", error)
                );
    }

    private void handleTestRunning() {
        if (testRunningDisposable != null && !testRunningDisposable.isDisposed()) {
            testRunningDisposable.dispose();
        }

        // wait 2 seconds to make sure that the testProgress holds data of the currently running test
        // without this delay, we might see the previous data with progressPercent=100
        testRunningDisposable = Completable.timer(2, TimeUnit.SECONDS)
                // wait for a testProgress that has a recordId that is new to us
                .andThen(readerInteractor.getTestProgress()
                        .doOnNext(tp -> Timber.d("--- testProgress=%s", tp))
                        .doOnError(error -> Timber.d("--- testProgress error: %s", error))
                        .takeUntil((Predicate<? super TestProgress>) a -> stillOnThisScreen.get())
                        .flatMap(tp -> testResultInteractor.getById(tp.getTestId())
                                .map(tr -> false)
                                .onErrorReturn(a -> true)
                                .map(isNew -> new Pair<>(tp, isNew))
                                .toFlowable()
                        )
                        .doOnNext(pair -> Timber.d("--- pair: %s", pair))
                        .filter(pair -> pair.second)
                        .map(pair -> pair.first)
                        .take(1)
                        .singleOrError()
                        .retryWhen(err -> {
                            final AtomicInteger retryCounter = new AtomicInteger(0);
                            return err.takeWhile(a -> retryCounter.incrementAndGet() <= 3 && stillOnThisScreen.get())
                                    .flatMap(a -> {
                                        Timber.w("--- testProgress retryWhen retry: %d", retryCounter.get());
                                        return Flowable.timer(retryCounter.get(), TimeUnit.SECONDS);
                                    });
                        })
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onTestProgressReceived,
                        error -> {
                            Timber.w("--- Unhandled exception during Test Progress update: %s", error);
                            if (!(error instanceof NoSuchElementException)) {
                                onStart();
                            }
                        }
                );
    }

    private void handleError(WorkflowState workflowState) {
        // we don't want to receive any more workflow updates!
        if (workflowStateDisposable != null && !workflowStateDisposable.isDisposed()) {
            workflowStateDisposable.dispose();
        }

        // stop countdown as well
        if (countdownDisposable != null && !countdownDisposable.isDisposed()) {
            stillConnectedDisposable.dispose();
        }

        if (errorsDisposable != null && !errorsDisposable.isDisposed()) {
            errorsDisposable.dispose();
        }

        errorsDisposable = readerInteractor.getError()
                .onErrorReturnItem(Error.create(""))
                .map(error -> new Pair<>(workflowState, error.getMessage()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        errorPair -> {
                            try {
                                final ErrorModel errorModel = errorInteractor.createErrorModel(errorPair.first, errorPair.second, true);
                                Timber.d("Test error: %s -> %s", errorPair, errorModel);
                                ifViewAttached(attachedView -> attachedView.onTestError(errorModel));
                            } catch (ErrorModelConversionError error) {
                                Timber.d("Test error, failed to convert error model: %s", error);
                            }
                        },
                        error -> Timber.d("Error while listening for test error: %s", error)
                );
    }

    private void onTestProgressReceived(final TestProgress testProgress) {
        if (firstProgress == null || !firstProgress.getTestId().equals(testProgress.getTestId())) {
            firstProgress = testProgress;

            Timber.d("---- !! onTestProgressReceived!!  %s", testProgress.getTestId());

            startRemainingCountdown();
        }
    }

    private void startRemainingCountdown() {
        if (countdownDisposable != null && !countdownDisposable.isDisposed()) {
            countdownDisposable.dispose();
            countdownDisposable = null;
        }

        updateRemaining(firstProgress);

        // update progress every 1 seconds
        countdownDisposable = Observable.interval(1L, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tick -> {
                            updateRemaining(firstProgress);
                        },
                        error -> Timber.w("--- startRemainingCountdown error: %s", error),
                        () -> {
                            Timber.d("--- startRemainingCountdown complete... should have changed state by now!");
                            // safeguard to trigger change transition
                            readerInteractor.getConnectedReader()
                                    .toSingle()
                                    .ignoreElement()
                                    .andThen(readerInteractor.getWorkflowState("TP:startRemainingCountdown")
                                            .timeout(2, TimeUnit.SECONDS)
                                            .filter(wfs -> wfs == WorkflowState.TEST_COMPLETE || wfs == WorkflowState.POST_TEST || wfs == WorkflowState.READY || wfs == WorkflowState.SELF_TEST)
                                            .take(1)
                                            .singleOrError()
                                    )
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            ignored -> ifViewAttached(av -> av.onTestFinished(null)),
                                            error -> {
                                                Timber.d("--- startRemainingCountdown complete safeguard error: %s", error);
                                                ifViewAttached(av -> av.onTestError(ErrorModel.builder()
                                                        .setTitle(resourceInteractor.getStringResource(R.string.error_title_generic_2))
                                                        .setAfterChamberScrewedOn(true)
                                                        .setErrorCode("Unknown error")
                                                        .setMessage(resourceInteractor.getStringResource(R.string.error_message_generic_2))
                                                        .build()));
                                            });
                        }
                );
    }

    private void updateRemaining(final TestProgress testProgress) {
        if (testProgress == null) return;

        final DateTime now = new DateTime();
        // add 1 minute offset to the remaining time
        final DateTime start = new DateTime(testProgress.getStart());
        final DateTime end = new DateTime(testProgress.getEnd() + 60 * 1000L);
        final Duration remaining = new Duration(now, end);

        final String formattedRemaining = formatter.print(Math.max(0, remaining.getMillis()));
        final int percent = (int) ((Math.abs(new Duration(start, now).getMillis()) / (float) Math.abs(new Duration(start, end).getMillis())) * 100);
        final TimeRemaining timeRemaining = new TimeRemaining(formattedRemaining, percent);
        ifViewAttached(attachedView -> attachedView.showTimeRemaining(timeRemaining));
    }

    public void onStop() {
        if (stillOnThisScreen != null) {
            stillOnThisScreen.set(false);
        }

        firstProgress = null;

        if (countdownDisposable != null && !countdownDisposable.isDisposed()) {
            countdownDisposable.dispose();
            countdownDisposable = null;
        }
        if (stillConnectedDisposable != null && !stillConnectedDisposable.isDisposed()) {
            stillConnectedDisposable.dispose();
            stillConnectedDisposable = null;
        }
        if (testCompleteDisposable != null && !testCompleteDisposable.isDisposed()) {
            testCompleteDisposable.dispose();
            testCompleteDisposable = null;
        }

        if (errorsDisposable != null && !errorsDisposable.isDisposed()) {
            errorsDisposable.dispose();
            errorsDisposable = null;
        }

        if (batteryUpdatesDisposable != null && !batteryUpdatesDisposable.isDisposed()) {
            batteryUpdatesDisposable.dispose();
            batteryUpdatesDisposable = null;
        }

        if (workflowStateDisposable != null && !workflowStateDisposable.isDisposed()) {
            workflowStateDisposable.dispose();
            workflowStateDisposable = null;
        }
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_testing_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_testing_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.test_in_progress), true);
            attachedView.setDisclaimerMessage(resourceInteractor.getStringResource(R.string.test_running_warning));
            attachedView.setDisclaimerViewVisible(true);
            attachedView.setBatteryIndicatorVisible(true);
            attachedView.setProgressVisible(true);
            attachedView.setProgressPercentage(0);
        });
    }

    public void disposeTest() {
        testInteractor.resetTest().subscribe();
    }
}
