package com.aptatek.pkuapp.view.test.incubation;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.pkuapp.domain.model.AlertDialogModel;
import com.aptatek.pkuapp.view.test.TestActivityView;
import com.aptatek.pkuapp.view.test.TestScreens;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.Completable;
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
                attachedView.showAlertDialog(AlertDialogModel.builder()
                        .setTitle(resourceInteractor.getStringResource(R.string.test_incubation_alertdialog_title))
                        .setMessage(resourceInteractor.getStringResource(R.string.test_incubation_alertdialog_message))
                        .setPositiveButtonText(resourceInteractor.getStringResource(R.string.alertdialog_button_yes))
                        .setNegativeButtonText(resourceInteractor.getStringResource(R.string.alertdialog_button_no))
                        .setCancelable(true)
                        .build()
                )
        );
    }

    public void stopIncubation() {
        disposables.add(incubationInteractor.resetIncubation()
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

    public void skipIncubation() {
        disposables.add(incubationInteractor.skipIncubation()
                .andThen(Completable.fromAction(() ->
                    ifViewAttached(attachedView -> attachedView.showScreen(TestScreens.INSERT_CASSETTE))))
                .subscribe());
    }
}
