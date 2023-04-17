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
import io.reactivex.functions.Predicate;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
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

    private Disposable errorsDisposable;

    private AtomicBoolean stillOnThisScreen;

    private FlowableProcessor<WorkflowState> lastBehaviorSubject;

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
        onRestart();
        disposables = new CompositeDisposable();

        stillOnThisScreen = new AtomicBoolean(true);
        lastBehaviorSubject = BehaviorProcessor.createDefault(WorkflowState.DEFAULT);

        final Completable disconnected = readerInteractor.getReaderConnectionEvents()
                .filter(a -> a.getConnectionState() != ConnectionState.READY)
                .take(1)
                .ignoreElements()
                .onErrorComplete()
                .doOnSubscribe(a -> Timber.d("--- disconnect onSubscribe"))
                .doOnComplete(() -> Timber.d("--- disconnected stream completed"));

        disposables.add(
                readerInteractor.getWorkflowState("TestingPresenter")
                        .subscribe(wfs -> {
                            if (lastBehaviorSubject != null && !lastBehaviorSubject.hasComplete()) {
                                lastBehaviorSubject.onNext(wfs);
                            }
                        })
        );

        final Single<WorkflowState> errors = lastBehaviorSubject
                .filter(state -> state.name().toLowerCase(Locale.getDefault()).endsWith("error"))
                .take(1L)
                .replay()
                .autoConnect()
                .singleOrError()
                .doOnDispose(() -> Timber.d("--- error stream onDispose"))
                .doOnSubscribe(a -> Timber.d("--- error stream onSubscribe"))
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

        // terminal state =
        //      either the device disconnects,
        //      or we receive an error,
        //      or we detect a workflow state that tells us that the test is no longer running.
        final Completable terminal = Completable.ambArray(
                        disconnected,
                        errors.ignoreElement(),
                        readerInteractor.getWorkflowState("Testing terminal").filter(wfs -> !testMightBeRunning.contains(wfs))
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
        errorsDisposable = errors.flatMap(errorState ->
                        readerInteractor.getError()
                                .onErrorReturnItem(Error.create(""))
                                .map(error -> new Pair<>(errorState, error.getMessage()))
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(() -> Timber.d("--- error onDispose"))
                .doOnSubscribe(a -> Timber.d("--- error onSub"))
                .doOnSuccess(a -> Timber.d("--- error onSuccess: %s", a))
                .doOnError(a -> Timber.d("--- error onError: %s", a))
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

        // watch test complete
        disposables.add(
                lastBehaviorSubject
                        // take while we're connected and the test is running
                        .takeUntil(terminal.toSingle(Object::new).toFlowable())
                        // take while we're on this screen
                        .takeUntil((Predicate<? super WorkflowState>) a -> !stillOnThisScreen.get())
                        .filter(wfs -> wfs == WorkflowState.TEST_COMPLETE)
                        .take(1)
                        .singleOrError()
                        .doOnDispose(() -> Timber.d("--- testCompleteWfs onDispose"))
                        .doOnSubscribe(a -> Timber.d("--- testCompleteWfs onSub"))
                        .doOnSuccess(wfs -> Timber.d("--- testCompleteWfs onComplete"))
                        .doOnError(a -> Timber.d("--- testCompleteWfs onError: %s", a))
                        .flatMap(ignored -> readerInteractor.readAndStoreResult())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (testResult) -> {
                                    Timber.d("Test complete: %s", testResult.getId());
                                    analyticsManager.logEvent(new TestingDone(Math.abs(System.currentTimeMillis() - screenDisplayedAtMs)));

                                    ifViewAttached(attachedView -> attachedView.onTestFinished(testResult.getId()));
                                },
                                error -> Timber.w("--- testComplete error %s", error)
                        )
        );

        // watch test progress during test running
        disposables.add(
                lastBehaviorSubject
                        .doOnNext(wfs -> Timber.wtf("--- lastBehavior onNext %s", wfs))
                        // take updates while we're connected and the wfs is test running
                        .takeUntil(terminal.toSingle(Object::new).toFlowable())
                        // take updates while we're on this screen
                        .takeUntil((Predicate<? super WorkflowState>) wfs -> !stillOnThisScreen.get())
                        .doOnComplete(() -> Timber.wtf("--- lastBehavior onComplete!!"))
                        .filter(wfs -> wfs == WorkflowState.TEST_RUNNING)
                        .take(1)
                        .singleOrError()
                        .doOnSuccess(a -> Timber.d("--- test running detected"))
                        // wait for a testProgress that has a recordId that is new to us
                        .flatMap(ignored -> readerInteractor.getTestProgress()
                                .doOnNext(tp -> Timber.d("--- testProgress=%s", tp))
                                .doOnError(error -> Timber.d("--- testProgress error: %s", error))
                                .takeUntil(errors.toFlowable())
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
                                    onStart();
                                }
                        )
        );

        // watch other workflow states that tell us the test is no longer running
        disposables.add(lastBehaviorSubject
                // take updates while we're on this screen
                .takeUntil((Predicate<? super WorkflowState>) wfs -> !stillOnThisScreen.get())
                .filter(wfs -> !testMightBeRunning.contains(wfs))
                .ignoreElements()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> ifViewAttached(av -> av.onTestFinished(null)),
                        error -> Timber.w("--- NOT test workflow state error %s", error)
                )
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

    public void onRestart() {
        Timber.d("--- onRestart");

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

    public void onStop() {
        onRestart();
        if (errorsDisposable != null && !errorsDisposable.isDisposed()) {
            errorsDisposable.dispose();
            errorsDisposable = null;
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
