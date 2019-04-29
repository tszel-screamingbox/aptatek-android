package com.aptatek.pkulab.view.main.continuetest;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.model.ContinueTestResultType;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenterImpl;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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
                                             final TestInteractor testInteractor1) {
        this.testResultInteractor = testResultInteractor;
        this.testInteractor = testInteractor1;
        wrapped = new TurnReaderOnPresenterImpl(bluetoothInteractor, readerInteractor, testInteractor);
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void attachView(final @NonNull TurnReaderOnContinueTestView view) {
        super.attachView(view);
        wrapped.attachView(view);
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
        disposables.add(readerInteractor.syncAllResults()
                .subscribe(
                        ignored -> checkLastMeasure(),
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
                    if (workflowState == WorkflowState.READING_CASSETTE || workflowState == WorkflowState.TEST_RUNNING
                            || workflowState == WorkflowState.TEST_COMPLETE || workflowState == WorkflowState.POST_TEST) {
                        ifViewAttached(view -> view.finishTestContinue(ContinueTestResultType.FINISHED_WITH_TEST_RUNNING));
                    } else {
                        ifViewAttached(view -> view.finishTestContinue(ContinueTestResultType.FINISHED_WITH_WRONG_RESULT));
                    }
                }));
    }
}
