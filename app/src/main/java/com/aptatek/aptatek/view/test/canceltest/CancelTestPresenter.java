package com.aptatek.aptatek.view.test.canceltest;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.aptatek.view.test.TestScreens;
import com.aptatek.aptatek.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.Completable;

public class CancelTestPresenter extends TestBasePresenter<CancelTestView> {

    private final ResourceInteractor resourceInteractor;
    private final IncubationInteractor incubationInteractor;

    @Inject
    CancelTestPresenter(final ResourceInteractor resourceInteractor, final IncubationInteractor incubationInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.incubationInteractor = incubationInteractor;
    }

    @Override
    public void initUi() {
        ifViewAttached(view -> {
            view.setNavigationButtonVisible(true);
            view.setNavigationButtonText(resourceInteractor.getStringResource(R.string.test_cancel_navigation_yes));
            view.setCancelBigVisible(false);
            view.setCircleCancelVisible(true);
            view.setTitle(resourceInteractor.getStringResource(R.string.test_cancel_title));
            view.setMessage(resourceInteractor.getStringResource(R.string.test_cancel_description));
        });
    }

    public void stopTest() {
        incubationInteractor.hasRunningIncubation()
                .flatMapCompletable(value -> {
                    if (value) {
                        return incubationInteractor.stopIncubation();
                    } else {
                        return Completable.complete();
                    }
                })
                .andThen(Completable.fromAction(() -> ifViewAttached(attachedView -> attachedView.showScreen(TestScreens.TAKE_SAMPLE))))
                .subscribe();
    }
}
