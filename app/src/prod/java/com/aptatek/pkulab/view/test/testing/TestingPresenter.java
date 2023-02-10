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
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.error.ErrorModel;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

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
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
    private Disposable countdownDisposable;

    private Disposable stillConnectedDisposable;

    private TestProgress firstProgress;

    private Disposable testCompleteDisposable;

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

        // watch for active connection
        stillConnectedDisposable = readerInteractor.getReaderConnectionEvents().filter(a -> a.getConnectionState() != ConnectionState.READY)
                .take(1)
                .ignoreElements()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> ifViewAttached(TestingView::showTurnReaderOn), error -> Timber.d("--- testingPresenter stillConnectedDisposable error: %s", error));

        // watch test progress to display remaining time
        disposables.add(
                // wait until we don't see a brand new test id
                readerInteractor.getTestProgress()
                        .flatMap(tp -> testResultInteractor.getById(tp.getTestId()).toFlowable()
                                .map(result -> false)
                                .onErrorReturnItem(true)
                                .doOnNext(valid -> Timber.d("--- found tp by id in database = %s", !valid ? "TRUE" : "FALSE"))
                                .map(valid -> new Pair<>(tp, valid))
                        )
                        .retryWhen(errors -> {
                            final AtomicInteger counter = new AtomicInteger(0);
                            return errors
                                    .takeWhile(e -> counter.getAndIncrement() != 3)
                                    .flatMap(e -> {
                                        Timber.d("--- getTestProgress retryWhen error = %s", e);
                                        return Flowable.timer(counter.get(), TimeUnit.SECONDS);
                                    }).doOnNext(i -> Timber.d("--- getTestProgress retryWhen error delayed retry"));
                        })
                        .filter(a -> a.second)
                        .map(a -> a.first)
                        // attempt to filter out previous test progress
                        .flatMap(tp -> readerInteractor.getWorkflowState("TestingPres to determine if test is running")
                                .take(1)
                                .filter(wfs -> tp.getPercent() != 100 || !(wfs == WorkflowState.TEST_RUNNING || wfs == WorkflowState.READING_CASSETTE || wfs == WorkflowState.DETECTING_FLUID)).map(
                                        wfs -> tp
                                ))
                        .doOnNext(tp -> Timber.d("--- filtered TestProgress: %s", tp))
                        .doOnComplete(() -> Timber.d("--- filtered testProgress complete !!! should not happen"))
                        .doOnError(e -> Timber.d("--- filtered testProgress error %s !!! should not happen", e))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::onTestProgressReceived,
                                error -> {
                                    Timber.d("Unhandled exception during Test Progress update: %s", error);
                                    onStart();
                                },
                                () -> {
                                    firstProgress = null;

                                    if (countdownDisposable != null && !countdownDisposable.isDisposed()) {
                                        countdownDisposable.dispose();
                                        countdownDisposable = null;
                                    }
                                    Timber.d("Test Progress updates complete");
                                }
                        )
        );

        // watch battery updates
        disposables.add(
                readerInteractor.batteryLevelUpdates()
                        // take battery updates while we're connected and the wfs is test running
                        .takeUntil(Flowable.combineLatest(
                                        readerInteractor.getReaderConnectionEvents().map(event -> event.getConnectionState() == ConnectionState.READY),
                                        readerInteractor.getWorkflowState("TP: watch battery updates").map(workflowState -> workflowState == WorkflowState.TEST_RUNNING || workflowState == WorkflowState.READING_CASSETTE || workflowState == WorkflowState.DETECTING_FLUID),
                                        (aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                                .filter(a -> !a)
                                .doOnNext(a -> Timber.d("--- battery updates takeUntil stop!"))
                                .doOnError(a -> Timber.d("--- battery updates takeUntil error: %s", a))
                        )
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
                readerInteractor.getWorkflowState("TP: watch errors")
                        .filter(state -> state.name().toLowerCase(Locale.getDefault()).endsWith("error"))
                        .take(1L)
                        .singleOrError()
                        .flatMap(errorState ->
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
                        .andThen(Single.just(firstProgress))
                        .observeOn(Schedulers.io())
                        .flatMap(testProgress -> readerInteractor.syncResultsAfterLatest()
                                .ignoreElement()
                                .doOnError(error -> Timber.d("--- syncResultsAfterLatest error: %s", error))
                                .onErrorComplete()
                                .andThen(readerInteractor.getResult(testProgress.getTestId(), true)
                                        .flatMapCompletable(readerInteractor::saveResult)
                                )
                                .andThen(Single.just(testProgress.getTestId()))
                        )
                        .onErrorResumeNext(error -> {
                            Timber.d("--- testProgress getResult flow error %s, fallback to latest!", error);
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
                                error -> Timber.d("Error while getting test result: %s", error)
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

        final Calendar calendar = Calendar.getInstance();
        final long now = System.currentTimeMillis();
        // add 1 minute offset to the remaining time
        calendar.setTimeInMillis(Math.max(0, testProgress.getEnd() + 60 * 1000L - now));
        final String formattedRemaining = simpleDateFormat.format(calendar.getTime());
        final int percent = (int) ((Math.abs(now - testProgress.getStart()) / (float) Math.abs(testProgress.getEnd() - testProgress.getStart())) * 100);
        final TimeRemaining timeRemaining = new TimeRemaining(formattedRemaining, percent);
        ifViewAttached(attachedView -> attachedView.showTimeRemaining(timeRemaining));
    }

    public void onStop() {
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
