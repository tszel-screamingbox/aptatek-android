package com.aptatek.aptatek.view.test.incubation;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.aptatek.domain.model.AlertDialogModel;
import com.aptatek.aptatek.view.test.TestActivityView;
import com.aptatek.aptatek.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class IncubationPresenter extends TestBasePresenter<IncubationView> {

    private final ResourceInteractor resourceInteractor;
    private final IncubationInteractor incubationInteractor;

    private CompositeDisposable disposables;

    @Inject
    public IncubationPresenter(final ResourceInteractor resourceInteractor,
                               final IncubationInteractor incubationInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.incubationInteractor = incubationInteractor;
    }

    @Override
    public void initUi() {
        ifViewAttached(view -> {
            view.setCancelBigVisible(false);
            view.setCircleCancelVisible(true);
            view.setNavigationButtonVisible(true);
            view.setNavigationButtonText(resourceInteractor.getStringResource(R.string.test_button_next));
            view.setTitle(resourceInteractor.getStringResource(R.string.test_incubation_title));
            view.setMessage(resourceInteractor.getStringResource(R.string.test_incubation_description));
        });
    }

    public void onClickNext() {
        ifViewAttached(attachedView ->
            attachedView.showAlertDialog(AlertDialogModel.create(
                    resourceInteractor.getStringResource(R.string.test_incubation_alertdialog_title),
                    resourceInteractor.getStringResource(R.string.test_incubation_alertdialog_message
                )
            ))
        );
    }

    public void stopIncubation() {
        disposables.add(incubationInteractor.stopIncubation()
                .subscribe());
    }

    @Override
    public void attachView(@NonNull final IncubationView view) {
        super.attachView(view);

        disposables = new CompositeDisposable();
        disposables.add(incubationInteractor.getIncubationCountdown()
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
                ));
    }

    @Override
    public void detachView() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }

        super.detachView();
    }
}
