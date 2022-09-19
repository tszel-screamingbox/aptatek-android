package com.aptatek.pkulab.view.test.connectitall;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.NoSuchElementException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ConnectItAllPresenter extends TestBasePresenter<ConnectItAllView> {

    private final CompositeDisposable disposables = new CompositeDisposable();
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
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.connect_it_all), true);
        });

        disposables.add(
                readerInteractor.getConnectedReader()
                        .toSingle()
                        .flatMap(device -> readerInteractor.getWorkflowState()
                                .filter(workflowState -> workflowState == WorkflowState.READING_CASSETTE || workflowState == WorkflowState.DETECTING_FLUID || workflowState == WorkflowState.TEST_RUNNING)
                                .take(1)
                                .singleOrError()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                ignored -> ifViewAttached(ConnectItAllView::showNextScreen),
                                error -> {
                                    if (error instanceof NoSuchElementException) { // reader not connected, need to show TurnReaderOn
                                        ifViewAttached(ConnectItAllView::showTurnReaderOn);
                                    }
                                })
        );
    }

    public void cancelWettingNotification() {
        disposables.add(testInteractor.cancelWettingFinishedNotifications()
                .subscribe());
    }

    @Override
    public void detachView() {
        super.detachView();

        disposables.dispose();
    }
}
