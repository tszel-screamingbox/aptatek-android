package com.aptatek.pkulab.view.test.canceltest;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

public class CancelTestPresenter extends TestBasePresenter<CancelTestView> {

    private final WettingInteractor wettingInteractor;
    private final TestInteractor testInteractor;
    private final PreferenceManager preferenceManager;

    private Disposable disposable;

    @Inject
    CancelTestPresenter(final ResourceInteractor resourceInteractor,
                        final WettingInteractor wettingInteractor,
                        final TestInteractor testInteractor,
                        final PreferenceManager preferenceManager) {
        super(resourceInteractor);
        this.wettingInteractor = wettingInteractor;
        this.testInteractor = testInteractor;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void initUi() {
        ifViewAttached(view -> {
            view.setBottomBarVisible(false);
            view.setTitle(resourceInteractor.getStringResource(R.string.test_cancel_title));
            view.setMessage(resourceInteractor.getStringResource(R.string.test_cancel_description));
        });
    }

    public void stopTest() {
        disposable = wettingInteractor.resetWetting()
                .andThen(testInteractor.setLastScreen(TestScreens.TURN_READER_ON))
                .doAfterTerminate(() -> preferenceManager.setTestFlowStatus(false))
                .andThen(Completable.fromAction(() -> ifViewAttached(CancelTestView::finishActivity)))
                .subscribe();
    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
