package com.aptatek.pkulab.view.test.testing;

import android.util.Pair;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorModelConversionError;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.test.TestingDone;
import com.aptatek.pkulab.domain.manager.analytic.events.test.TestingScreenDisplayed;
import com.aptatek.pkulab.domain.model.reader.Error;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.error.ErrorModel;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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
    private CompositeDisposable disposables;
    private long screenDisplayedAtMs = 0L;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
    private Disposable countdownDisposable;

    @Inject
    public TestingPresenter(final ResourceInteractor resourceInteractor,
                            final ReaderInteractor readerInteractor,
                            final IAnalyticsManager analyticsManager,
                            final ErrorInteractor errorInteractor,
                            final TestInteractor testInteractor) {
        super(resourceInteractor);
        this.readerInteractor = readerInteractor;
        this.analyticsManager = analyticsManager;
        this.errorInteractor = errorInteractor;
        this.testInteractor = testInteractor;

        analyticsManager.logEvent(new TestingScreenDisplayed());
        screenDisplayedAtMs = System.currentTimeMillis();
    }

    public void onStart() {
        disposables = new CompositeDisposable();

        disposables.add(
                readerInteractor.getConnectedReader()
                        .toSingle()
                        .toFlowable()
                        .flatMap(ignored ->
                                readerInteractor.getTestProgress()
                                        .takeUntil(readerInteractor.getWorkflowState()
                                                .filter(workflowState -> workflowState == WorkflowState.TEST_COMPLETE || workflowState == WorkflowState.POST_TEST || workflowState == WorkflowState.READY)
                                                .take(1)
                                        )
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::startRemainingCountdown,
                                error -> {
                                    if (error instanceof NoSuchElementException) {
                                        ifViewAttached(TestingView::showTurnReaderOn);
                                    } else {
                                        Timber.d("Unhandled exception during Test Progress update: %s", error);
                                    }
                                },
                                () -> Timber.d("Test Progress updates complete")
                        )
        );

        disposables.add(
                readerInteractor.getConnectedReader()
                        .toSingle()
                        .toFlowable()
                        .flatMap(ignored ->
                                readerInteractor.batteryLevelUpdates()
                                        .takeUntil(readerInteractor.getWorkflowState()
                                                .filter(workflowState -> workflowState == WorkflowState.TEST_COMPLETE || workflowState == WorkflowState.POST_TEST || workflowState == WorkflowState.READY)
                                                .take(1)
                                        )
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                batteryPercent -> ifViewAttached(attachedView -> attachedView.setBatteryPercentage(batteryPercent)),
                                error -> Timber.d("Error during battery level update: %s", error),
                                () -> Timber.d("Battery level updates complete")
                        )
        );

        disposables.add(
                readerInteractor.getConnectedReader()
                        .toSingle()
                        .toFlowable()
                        .flatMap(ignored ->
                                readerInteractor.getTestProgress()
                                        .takeUntil(readerInteractor.getWorkflowState()
                                                .filter(workflowState -> workflowState == WorkflowState.TEST_COMPLETE || workflowState == WorkflowState.POST_TEST || workflowState == WorkflowState.READY)
                                                .take(1)
                                        )
                        )
                        .lastOrError()
                        .flatMap(testProgress ->
                                readerInteractor.syncResultsAfterLatest()
                                        .ignoreElement()
                                        .andThen(readerInteractor.getResult(testProgress.getTestId())
                                                .flatMapCompletable(readerInteractor::saveResult)
                                        )
                                        .andThen(Single.just(testProgress.getTestId()))
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                resultId -> {
                                    Timber.d("Test complete: %s", resultId);
                                    analyticsManager.logEvent(new TestingDone(Math.abs(System.currentTimeMillis() - screenDisplayedAtMs)));

                                    ifViewAttached(attachedView -> attachedView.onTestFinished(resultId));
                                },
                                error -> Timber.d("Error while getting test result: %s", error)
                        )
        );

        disposables.add(
                readerInteractor.getConnectedReader()
                        .toSingle()
                        .toFlowable()
                        .flatMap(ignored ->
                                readerInteractor.getWorkflowState()
                                        .filter(state -> state.name().toLowerCase(Locale.getDefault()).endsWith("error"))
                                        .take(1L)
                        )
                        .lastOrError()
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
                                        final ErrorModel errorModel = errorInteractor.createErrorModel(errorPair.first, errorPair.second);
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

    private void startRemainingCountdown(final TestProgress testProgress) {
        if (countdownDisposable != null && !countdownDisposable.isDisposed()) {
            countdownDisposable.dispose();
            countdownDisposable = null;
        }

        countdownDisposable = Observable.interval(1L, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tick -> {
                            final Calendar calendar = Calendar.getInstance();
                            final long now = System.currentTimeMillis();
                            calendar.setTimeInMillis(Math.max(0, testProgress.getEnd() - now));
                            final String formattedRemaining = simpleDateFormat.format(calendar.getTime());
                            final int percent = (int) ((Math.abs(now - testProgress.getStart()) / (float) Math.abs(testProgress.getEnd() - testProgress.getStart())) * 100);
                            final TimeRemaining timeRemaining = new TimeRemaining(resourceInteractor.getFormattedString(R.string.test_running_time_remaining_format, formattedRemaining), percent);
                            ifViewAttached(attachedView -> attachedView.showTimeRemaining(timeRemaining));
                        }
                );
    }

    public void onStop() {
        if (disposables != null) {
            disposables.dispose();
            disposables = null;
        }
        if (countdownDisposable != null && !countdownDisposable.isDisposed()) {
            countdownDisposable.dispose();
            countdownDisposable = null;
        }
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_testing_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_testing_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.testing), true);
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
