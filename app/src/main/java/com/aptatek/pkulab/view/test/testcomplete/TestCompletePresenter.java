package com.aptatek.pkulab.view.test.testcomplete;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class TestCompletePresenter extends TestBasePresenter<TestCompleteView> {

    private final ReaderInteractor readerInteractor;
    private Disposable disposable = null;

    @Inject
    public TestCompletePresenter(ResourceInteractor resourceInteractor, ReaderInteractor readerInteractor) {
        super(resourceInteractor);
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_testcomplete_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_testcomplete_message));
            attachedView.setBatteryIndicatorVisible(false);
        });
    }

    public void onStart() {
        onStop();
        disposable = readerInteractor.getTestProgress()
                .takeUntil(tp -> tp.getPercent() == 100)
                .map(TestProgress::getTestId)
                .flatMap(testId -> readerInteractor.getWorkflowState()
                        .filter(ws -> ws == WorkflowState.SELF_TEST || ws == WorkflowState.READY)
                        .map(ignored -> testId))
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(testId -> ifViewAttached(av -> av.showResults(testId)));
    }

    public void onStop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }
}
