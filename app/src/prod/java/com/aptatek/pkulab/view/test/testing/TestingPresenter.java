package com.aptatek.pkulab.view.test.testing;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.test.TestingDone;
import com.aptatek.pkulab.domain.manager.analytic.events.test.TestingScreenDisplayed;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.NoSuchElementException;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TestingPresenter extends TestBasePresenter<TestingView> {

    private final ReaderInteractor readerInteractor;
    private final IAnalyticsManager analyticsManager;
    private CompositeDisposable disposables;
    private long screenDisplayedAtMs = 0L;

    @Inject
    public TestingPresenter(final ResourceInteractor resourceInteractor,
                            final ReaderInteractor readerInteractor,
                            final IAnalyticsManager analyticsManager) {
        super(resourceInteractor);
        this.readerInteractor = readerInteractor;
        this.analyticsManager = analyticsManager;

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
                        testProgress -> {
                            Timber.d("--- Test Progress update: %s", testProgress);
                            ifViewAttached(attachedView -> attachedView.setProgressPercentage(testProgress.getPercent()));
                        },
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
    }

    public void onStop() {
        if (disposables != null) {
            disposables.dispose();
            disposables = null;
        }
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_testing_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_testing_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.testing), true);
            attachedView.setBatteryIndicatorVisible(true);
            attachedView.setProgressVisible(true);
            attachedView.setProgressPercentage(0);
        });
    }
}
