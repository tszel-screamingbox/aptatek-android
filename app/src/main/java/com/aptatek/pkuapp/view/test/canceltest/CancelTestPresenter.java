package com.aptatek.pkuapp.view.test.canceltest;

import android.util.Pair;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationStatus;
import com.aptatek.pkuapp.domain.interactor.samplewetting.SampleWettingInteractor;
import com.aptatek.pkuapp.domain.interactor.samplewetting.WettingStatus;
import com.aptatek.pkuapp.view.test.TestScreens;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class CancelTestPresenter extends TestBasePresenter<CancelTestView> {

    private final ResourceInteractor resourceInteractor;
    private final IncubationInteractor incubationInteractor;
    private final SampleWettingInteractor sampleWettingInteractor;

    private Disposable disposable;

    @Inject
    CancelTestPresenter(final ResourceInteractor resourceInteractor,
                        final IncubationInteractor incubationInteractor,
                        final SampleWettingInteractor sampleWettingInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.incubationInteractor = incubationInteractor;
        this.sampleWettingInteractor = sampleWettingInteractor;
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
        disposable = incubationInteractor.resetIncubation()
            .andThen(sampleWettingInteractor.resetWetting())
            .andThen(Completable.fromAction(() -> ifViewAttached(attachedView -> attachedView.showScreen(TestScreens.TAKE_SAMPLE))))
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
