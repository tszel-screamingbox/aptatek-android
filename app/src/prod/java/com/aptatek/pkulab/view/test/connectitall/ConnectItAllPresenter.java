package com.aptatek.pkulab.view.test.connectitall;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ConnectItAllPresenter extends TestBasePresenter<ConnectItAllView> {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private Disposable disconnectDisposable;

    private Disposable workflowStateDisposable;

    private final TestInteractor testInteractor;
    private final ReaderInteractor readerInteractor;

    @Inject
    public ConnectItAllPresenter(final ResourceInteractor resourceInteractor,
                                 final TestInteractor testInteractor,
                                 final ReaderInteractor readerInteractor) {
        super(resourceInteractor);
        this.testInteractor = testInteractor;
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setNextButtonVisible(false);
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_connectitall_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_connectitall_message));
            attachedView.setDisclaimerViewVisible(true);
            attachedView.setDisclaimerMessage(resourceInteractor.getStringResource(R.string.test_connectitall_disclaimer));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.insert_cassette), true);
        });
    }

    public void cancelWettingNotification() {
        disposables.add(testInteractor.cancelWettingFinishedNotifications()
                .subscribe());
    }

    public void onStart() {
        onStop();

        // go to turn reader on when disconnect from reader is detected
        disconnectDisposable = readerInteractor.getReaderConnectionEvents()
                .filter(event -> event.getConnectionState() != ConnectionState.READY).take(1)
                .ignoreElements()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> ifViewAttached(ConnectItAllView::showTurnReaderOn));

        // wait for next wfs
        workflowStateDisposable = readerInteractor.getWorkflowState("CIAP: onStart")
                .filter(workflowState -> workflowState == WorkflowState.READING_CASSETTE || workflowState == WorkflowState.DETECTING_FLUID || workflowState == WorkflowState.TEST_RUNNING || workflowState == WorkflowState.TEST_COMPLETE)
                .take(1)
                .singleOrError()
                .ignoreElement()
                .timeout(5, TimeUnit.SECONDS)
                .doOnError(error -> Timber.d("--- ConnectItAllPresenter WFS onError %s", error))
                .retry()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> ifViewAttached(ConnectItAllView::showNextScreen),
                        error -> Timber.d("--- ConnectItAllPresenter WFS error: %s", error)
                );
    }

    public void onStop() {
        if (disconnectDisposable != null && !disconnectDisposable.isDisposed()) {
            disconnectDisposable.dispose();
            disconnectDisposable = null;
        }

        if (workflowStateDisposable != null && !workflowStateDisposable.isDisposed()) {
            workflowStateDisposable.dispose();
            workflowStateDisposable = null;
        }
    }

    @Override
    public void detachView() {
        super.detachView();

        disposables.dispose();
    }
}
