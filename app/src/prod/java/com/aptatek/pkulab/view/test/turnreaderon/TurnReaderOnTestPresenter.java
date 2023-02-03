package com.aptatek.pkulab.view.test.turnreaderon;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.BluetoothInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.ErrorModelConversionError;
import com.aptatek.pkulab.domain.interactor.test.ErrorInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.domain.model.reader.WorkflowStateUtils;
import com.aptatek.pkulab.view.connect.permission.PermissionResult;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenter;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnPresenterImpl;
import com.aptatek.pkulab.view.error.ErrorModel;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
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
                                     final TestInteractor testInteractor,
                                     final IAnalyticsManager analyticsManager,
                                     final ErrorInteractor errorInteractor) {
        super(resourceInteractor);
        wrapped = new TurnReaderOnPresenterImpl(bluetoothInteractor, readerInteractor, testInteractor, analyticsManager, errorInteractor);
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
                                .flatMapSingle(testId -> readerInteractor.getResult(testId, true))
                                .flatMapSingle(testResult -> readerInteractor.saveResult(testResult)
                                        .andThen(Single.just(testResult.getId()))
                                )
                                .subscribe(
                                        resultId -> ifViewAttached(turnReaderOnTestView -> turnReaderOnTestView.showTestResultScreen(resultId)),
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

    @Override
    public void logScreenDisplayed() {
        wrapped.logScreenDisplayed();
    }

    public void disposeTest() {
        wrapped.cleanupTest();
    }
}
