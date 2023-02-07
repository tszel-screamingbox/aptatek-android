package com.aptatek.pkulab.view.main.continuetest;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.model.ContinueTestResultType;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenterImpl;
import com.aptatek.pkulab.view.test.turnreaderon.TurnReaderOnTestView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TurnReaderOnContinueTestPresenter extends MvpBasePresenter<TurnReaderOnContinueTestView>
        implements TurnReaderOnPresenter<TurnReaderOnContinueTestView> {

    private final TurnReaderOnPresenterImpl wrapped;
    private final ReaderInteractor readerInteractor;
    private final TestResultInteractor testResultInteractor;
    private final TestInteractor testInteractor;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TurnReaderOnContinueTestPresenter(final BluetoothInteractor bluetoothInteractor,
                                             final ReaderInteractor readerInteractor,
                                             final TestResultInteractor testResultInteractor,
                                             final TestInteractor testInteractor,
                                             final IAnalyticsManager analyticsManager,
                                             final ErrorInteractor errorInteractor) {
        this.testResultInteractor = testResultInteractor;
        this.testInteractor = testInteractor;
        wrapped = new TurnReaderOnPresenterImpl(bluetoothInteractor, readerInteractor, testInteractor, analyticsManager, errorInteractor);
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void attachView(final @NonNull TurnReaderOnContinueTestView view) {
        super.attachView(view);
        wrapped.attachView(view);
        wrapped.setWorkflowStateHandler(this::handleExtraWorkflowState);
    }

    private boolean handleExtraWorkflowState(final WorkflowState workflowState) {
        boolean handled = false;
        switch (workflowState) {
            case READING_CASSETTE:
            case DETECTING_FLUID:
            case TEST_RUNNING: {
                handled = true;
                ifViewAttached(view -> view.finishTestContinue(ContinueTestResultType.FINISHED_WITH_TEST_RUNNING));

                break;
            }
            case POST_TEST:
            case TEST_COMPLETE: {
                handled = true;

                ifViewAttached(view -> view.finishTestContinue(ContinueTestResultType.FINISHED_WITH_CORRECT_RESULT));
                break;
            }
        }

        return handled;
    }

    @Override
    public void detachView() {
        wrapped.detachView();

        disposables.dispose();

        super.detachView();
    }

    @Override
    public void onResumed() {
        wrapped.onResumed();
    }

    @Override
    public void onPaused() {
        wrapped.onPaused();
    }

    @Override
    public void connectTo(@NonNull final ReaderDevice readerDevice) {
        wrapped.connectTo(readerDevice);
    }

    @Override
    public void evaluatePermissionResults(final List<PermissionResult> results) {
        wrapped.evaluatePermissionResults(results);
    }

    @Override
    public void checkPermissions() {
        wrapped.checkPermissions();
    }

    void syncData() {
        disposables.add(readerInteractor.syncAllResultsCompletable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::checkLastMeasure,
                        error -> ifViewAttached(view -> view.finishTestContinue(ContinueTestResultType.FINISHED_WITH_WRONG_RESULT))
                ));
    }

    private void checkLastMeasure() {
        disposables.add(Single.zip(testResultInteractor.getLatest(), testInteractor.getAppKilledTimestamp(),
                        (testResult, appKilledTime) -> testResult.getTimestamp() > appKilledTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(correct -> {
                    if (correct) {
                        ifViewAttached(view -> view.finishTestContinue(ContinueTestResultType.FINISHED_WITH_CORRECT_RESULT));
                    } else {
                        getWorkflowState();
                    }
                }));

    }

    private void getWorkflowState() {
        disposables.add(readerInteractor.getWorkflowState()
                .take(1)
                .subscribe(workflowState -> {
                    if (workflowState == WorkflowState.READING_CASSETTE || workflowState == WorkflowState.DETECTING_FLUID || workflowState == WorkflowState.TEST_RUNNING
                            || workflowState == WorkflowState.TEST_COMPLETE || workflowState == WorkflowState.POST_TEST) {
                        ifViewAttached(view -> view.finishTestContinue(ContinueTestResultType.FINISHED_WITH_TEST_RUNNING));
                    } else {
                        ifViewAttached(view -> view.finishTestContinue(ContinueTestResultType.FINISHED_WITH_WRONG_RESULT));
                    }
                }));
    }

    @Override
    public void logScreenDisplayed() {
        wrapped.logScreenDisplayed();
    }

    @Override
    public void disposeTest() {
        wrapped.disposeTest();
    }
}
