package com.aptatek.aptatek.view.test.incubation;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.aptatek.view.test.TestActivityView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class IncubationPresenter extends MvpBasePresenter<IncubationView> {

    private final ResourceInteractor resourceInteractor;
    private final IncubationInteractor incubationInteractor;

    private Disposable disposable;

    @Inject
    public IncubationPresenter(final ResourceInteractor resourceInteractor,
                               final IncubationInteractor incubationInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.incubationInteractor = incubationInteractor;
    }

    public void initUi() {
        ifViewAttached(view -> {
            view.setCancelBigVisible(false);
            view.setCircleCancelVisible(true);
            view.setNavigationButtonVisible(true);
            view.setNavigationButtonText(resourceInteractor.getStringResource(R.string.test_incubation_button_next));
            view.setTitle(resourceInteractor.getStringResource(R.string.test_incubation_title));
            view.setMessage(resourceInteractor.getStringResource(R.string.test_incubation_description));
        });
    }

    @Override
    public void attachView(@NonNull final IncubationView view) {
        super.attachView(view);

        disposable = incubationInteractor.getIncubationCountdown()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countdown -> {
                            Timber.d("Countdown: %s", countdown);
                            ifViewAttached(activeView -> activeView.showCountdownText(countdown.getRemainingFormattedText()));
                        },
                        error -> {
                            Timber.d("Countdown error: %s", error.toString());
                            ifViewAttached(TestActivityView::navigateBack);
                        },
                        () -> {
                            Timber.d("Countdown finished!");
                            ifViewAttached(TestActivityView::navigateForward);
                        }
                );
    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
