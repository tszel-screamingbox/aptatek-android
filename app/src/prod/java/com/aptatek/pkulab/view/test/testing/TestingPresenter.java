package com.aptatek.pkulab.view.test.testing;

import android.util.Log;
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
import com.aptatek.pkulab.domain.model.reader.TestResult;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TestingPresenter extends TestBasePresenter<TestingView> {

    private final ReaderInteractor readerInteractor;
    private final IAnalyticsManager analyticsManager;
    private final ErrorInteractor errorInteractor;
    private final TestInteractor testInteractor;

    private final TestResultInteractor testResultInteractor;

    private CompositeDisposable disposables;
    private long screenDisplayedAtMs = 0L;
    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("mm:ss");
    private Disposable countdownDisposable;

    private Disposable stillConnectedDisposable;

    private TestProgress firstProgress;

    private Disposable testCompleteDisposable;

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
        disposables = new CompositeDisposable();

        stillOnThisScreen = new AtomicBoolean(true);

        final Completable disconnected = readerInteractor.getReaderConnectionEvents()
                .share()
                .filter(a -> a.getConnectionState() != ConnectionState.READY)
                .take(1)
                .ignoreElements()
                .onErrorComplete()
                .doOnComplete(() -> Timber.d("--- disconnected stream completed"));

        final Flowable<WorkflowState> wfsShared = readerInteractor.getWorkflowState("TestingPresenter")
                .doOnNext(wfs -> Timber.d("--- wfs = " + wfs))
                .doOnError(e -> Timber.w("--- wfs error = " + e))
                .doOnComplete(() -> Timber.w("--- wfs onComplete!!!"));

        final Single<WorkflowState> errors = wfsShared
                .filter(state -> state.name().toLowerCase(Locale.getDefault()).endsWith("error"))
                .take(1L)
                .singleOrError()
                .doOnSuccess(wfs -> Timber.d("--- error stream completed with " + wfs))
                .doOnError(e -> Timber.w("--- error stream error: " + e));

        // watch for active connection
        stillConnectedDisposable = disconnected
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> ifViewAttached(TestingView::showTurnReaderOn),
                        error -> Timber.d("--- testingPresenter stillConnectedDisposable error: %s", error)
                );

        // we can safely read the TestProgress char while in this state
        final List<WorkflowState> testRunningWithSafeTestProgress = new ArrayList<>();
        testRunningWithSafeTestProgress.add(WorkflowState.TEST_RUNNING);
        testRunningWithSafeTestProgress.add(WorkflowState.TEST_COMPLETE);
        testRunningWithSafeTestProgress.add(WorkflowState.TEST_VALIDATION);
        testRunningWithSafeTestProgress.add(WorkflowState.POST_TEST);

        // test process have been started, but testProgress may contain previous data
        final List<WorkflowState> testMightBeRunning = new ArrayList<>(testRunningWithSafeTestProgress);
        testMightBeRunning.add(WorkflowState.READING_CASSETTE);
        testMightBeRunning.add(WorkflowState.HEATING_CASSETTE);
        testMightBeRunning.add(WorkflowState.DETECTING_FLUID);

        final Completable terminal = Completable.ambArray(
                        disconnected,
                        errors.ignoreElement(),
                        wfsShared.filter(wfs -> !testMightBeRunning.contains(wfs))
                                .ignoreElements()
                                .doOnComplete(() -> Timber.w("--- wfs terminated"))
                )
                .observeOn(AndroidSchedulers.mainThread());

        // watch battery updates
        disposables.add(
                readerInteractor.batteryLevelUpdates()
                        // take battery updates while we're connected and the wfs is test running
                        .takeUntil(terminal.toSingle(Object::new).toFlowable())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                batteryPercent -> ifViewAttached(attachedView -> attachedView.setBatteryPercentage(batteryPercent)),
                                error -> Timber.d("Error during battery level update: %s", error),
                                () -> Timber.d("Battery level updates complete")
                        )
        );

        // watch for errors
        disposables.add(
                errors.flatMap(errorState ->
                                readerInteractor.getError()
                                        .onErrorReturnItem(Error.create(""))
                                        .map(error -> new Pair<>(errorState, error.getMessage()))
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                errorPair -> {
                                    try {
                                        final ErrorModel errorModel = errorInteractor.createErrorModel(errorPair.first, errorPair.second, true);
                                        Timber.d("Test error: %s -> %s", errorPair, errorModel);
                                        ifViewAttached(attachedView -> {
                                            attachedView.onTestError(errorModel);
                                        });
                                    } catch (ErrorModelConversionError error) {
                                        Timber.d("Test error, failed to convert error model: %s", error);
                                    }
                                },
                                error -> Timber.d("Error while listening for test error: %s", error)
                        )
        );

        // watch test progress
        disposables.add(
                wfsShared.filter(testRunningWithSafeTestProgress::contains)
                        .take(1)
                        .timeout(10, TimeUnit.SECONDS)
                        .retryWhen(err -> {
                            final AtomicInteger retryCount = new AtomicInteger(0);
                            return err.takeWhile(e -> retryCount.getAndIncrement() != 3 && stillOnThisScreen != null && stillOnThisScreen.get())
                                    .flatMap(e -> {
                                        Timber.d("--- getTestProgress retryWhen error = %s", e);
                                        return Flowable.timer(retryCount.get(), TimeUnit.SECONDS);
                                    }).doOnNext(i -> Timber.d("--- getTestProgress retryWhen error delayed retry"));
                        })
                        .ignoreElements()
                        // it takes some time until testProgress gets updated ...
                        .delay(5, TimeUnit.SECONDS)
                        .andThen(readerInteractor.getTestProgress()
                                .filter(tp -> tp.getPercent() < 100)
                                .timeout(10, TimeUnit.SECONDS)
                                .retryWhen(err -> {
                                    final AtomicInteger retryCount = new AtomicInteger(0);
                                    return err.takeWhile(e -> retryCount.getAndIncrement() != 3 && stillOnThisScreen != null && stillOnThisScreen.get())
                                            .flatMap(e -> {
                                                Timber.d("--- getTestProgress <100 retryWhen error = %s", e);
                                                return Flowable.timer(retryCount.get(), TimeUnit.SECONDS);
                                            }).doOnNext(i -> Timber.d("--- getTestProgress <100 retryWhen error delayed retry"));
                                })
                        )
                        .take(1)
                        .singleOrError()
                        .onErrorResumeNext(readerInteractor.getTestProgress().take(1).singleOrError())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::onTestProgressReceived,
                                error -> {
                                    Timber.w("Unhandled exception during Test Progress update: %s", error);
                                    onStart();
                                }
                        )

                );
    }

    private void onTestProgressReceived(final TestProgress testProgress) {
        if (firstProgress == null || !firstProgress.getTestId().equals(testProgress.getTestId())) {
            firstProgress = testProgress;

            Timber.d("---- !! onTestProgressReceived!!  %s", testProgress.getTestId());

            startRemainingCountdown();
            startWatchingTestComplete();
        }
    }

    private void startWatchingTestComplete() {
        if (testCompleteDisposable != null && !testCompleteDisposable.isDisposed()) {
            testCompleteDisposable.dispose();
            testCompleteDisposable = null;
        }

        testCompleteDisposable = readerInteractor.getWorkflowState("TP:startWatchingTestComplete")
                .filter(workflowState -> workflowState == WorkflowState.TEST_COMPLETE || workflowState == WorkflowState.POST_TEST || workflowState == WorkflowState.READY)
                .take(1)
                .ignoreElements()
                .observeOn(Schedulers.io())
                .andThen(readerInteractor.syncResultsAfterLatest()
                        .retryWhen(err -> {
                            final AtomicInteger retryCounter = new AtomicInteger();
                            return err.takeWhile(e -> retryCounter.getAndIncrement() != 3 && stillOnThisScreen != null && stillOnThisScreen.get())
                                    .flatMap(e -> {
                                        Timber.w("--- testComplete syncAfter error = %s", e);
                                        return Flowable.timer(retryCounter.get(), TimeUnit.SECONDS);
                                    }).doOnNext(i -> Timber.d("--- testComplete syncAfter retryWhen error delayed retry"));
                        })
                        .ignoreElement()
                        .doOnError(error -> Timber.w("--- testComplete syncResultsAfterLatest error: %s", error))
                        .onErrorComplete()
                )
                .andThen(readerInteractor.getTestProgress().take(1).singleOrError())
                .flatMap(testProg -> readerInteractor.getResult(testProg.getTestId(), true)
                    .onErrorResumeNext(err -> readerInteractor.getResult(firstProgress.getTestId(), true))
                    .map(TestResult::getId)
                )
                .onErrorResumeNext(error -> {
                    Timber.w("--- testProgress getResult flow error %s, fallback to latest!", error);
                    return testResultInteractor.getLatest().map(TestResult::getId).doOnSuccess(a -> Timber.d("--- testProgress getResult fallback -> %s", a));
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        resultId -> {
                            Timber.d("Test complete: %s", resultId);
                            analyticsManager.logEvent(new TestingDone(Math.abs(System.currentTimeMillis() - screenDisplayedAtMs)));

                            ifViewAttached(attachedView -> attachedView.onTestFinished(resultId));
                        },
                        error -> Timber.w("Error while getting test result: %s", error)
                );
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
                                    .andThen(readerInteractor.getWorkflowState("TP:startRemainingCountdown").filter(wfs -> wfs == WorkflowState.TEST_COMPLETE || wfs == WorkflowState.POST_TEST || wfs == WorkflowState.READY || wfs == WorkflowState.SELF_TEST)
                                            .take(1)
                                            .singleOrError()
                                            .ignoreElement())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> ifViewAttached(av -> av.onTestFinished(firstProgress.getTestId())), error -> {
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

        if (disposables != null) {
            disposables.dispose();
            disposables = null;
        }
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
        disposables.add(
                testInteractor.resetTest().subscribe()
        );
    }
}
