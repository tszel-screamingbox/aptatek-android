package com.aptatek.pkulab.view.test.turnreaderon;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenterImpl;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class TurnReaderOnTestPresenter extends TestBasePresenter<TurnReaderOnTestView> implements TurnReaderOnPresenter<TurnReaderOnTestView> {

    private final TurnReaderOnPresenterImpl wrapped;
    private final ReaderInteractor readerInteractor;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TurnReaderOnTestPresenter(final ResourceInteractor resourceInteractor,
                                     final BluetoothInteractor bluetoothInteractor,
                                     final ReaderInteractor readerInteractor,
                                     final TestInteractor testInteractor) {
        super(resourceInteractor);
        wrapped = new TurnReaderOnPresenterImpl(bluetoothInteractor, readerInteractor, testInteractor);
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void attachView(final @NonNull TurnReaderOnTestView view) {
        super.attachView(view);
        wrapped.attachView(view);
        wrapped.setWorkflowStateHandler(this::handleExtraWorkflowState);
    }

    @Override
    public void detachView() {
        wrapped.detachView();

        disposables.dispose();

        super.detachView();
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> attachedView.setNextButtonVisible(false));
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


    private boolean handleExtraWorkflowState(final WorkflowState workflowState) {
        boolean handled = false;
        switch (workflowState) {
            case TEST_RUNNING: {
                handled = true;
                ifViewAttached(TurnReaderOnTestView::showTestingScreen);
                break;
            }
            case POST_TEST:
            case TEST_COMPLETE: {
                handled = true;

                disposables.add(
                        readerInteractor.getTestProgress()
                                .filter(testProgress -> testProgress.getPercent() == 100)
                                .take(1)
                                .map(TestProgress::getTestId)
                                .map(String::valueOf)
                                .flatMapSingle(readerInteractor::getResult)
                                .flatMapCompletable(readerInteractor::saveResult)
                                .subscribe(
                                        () -> ifViewAttached(TurnReaderOnTestView::showTestResultScreen),
                                        error -> Timber.d("Error while getting last result: %s", error)
                                )
                );
                break;
            }
            case READING_CASSETTE: {
                handled = true;
                ifViewAttached(TurnReaderOnTestView::showConnectItAllScreen);
                break;
            }
            case USED_CASSETTE_ERROR: {
                // leave handled false, need to continuously check wf state to proceed!
                ifViewAttached(TurnReaderOnTestView::showUsedCassetteError);
                break;
            }
            default: {
                break;
            }
        }

        return handled;
    }

    public void getBatteryLevel() {
        disposables.add(
                readerInteractor.getReaderConnectionEvents()
                        .filter(connectionEvent -> connectionEvent.getConnectionState() == ConnectionState.READY)
                        .take(1)
                        .flatMapSingle(ignored -> readerInteractor.getBatteryLevel())
                        .subscribe(batteryPercent -> ifViewAttached(attachedView -> {
                            attachedView.setBatteryIndicatorVisible(true);
                            attachedView.setBatteryPercentage(batteryPercent);
                        }))
        );
    }

}
