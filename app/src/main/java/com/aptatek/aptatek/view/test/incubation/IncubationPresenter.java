package com.aptatek.aptatek.view.test.incubation;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.aptatek.util.Constants;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class IncubationPresenter extends MvpBasePresenter<IncubationView> {

    private final ResourceInteractor resourceInteractor;
    private final IncubationInteractor incubationInteractor;
    private final SimpleDateFormat timeFormatter;

    private Disposable disposable;

    @Inject
    public IncubationPresenter(final ResourceInteractor resourceInteractor,
                               final IncubationInteractor incubationInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.incubationInteractor = incubationInteractor;
        this.timeFormatter = new SimpleDateFormat(resourceInteractor.getStringResource(R.string.test_incubation_countdown_format), Locale.getDefault());
    }

    public void initUi() {
        ifViewAttached(view -> {
            view.setCancelBigVisible(true);
            view.setCircleCancelVisible(false);
            view.setNavigationButtonVisible(false);
            view.setTitle(resourceInteractor.getStringResource(R.string.test_incubation_title));
            view.setMessage(resourceInteractor.getStringResource(R.string.test_incubation_description));
        });
    }

    @Override
    public void attachView(@NonNull final IncubationView view) {
        super.attachView(view);

        disposable = incubationInteractor.hasRunningIncubation()
                .flatMapCompletable(value -> {
                    if (value) {
                        return incubationInteractor.getIncubationStart()
                                .flatMapCompletable(startTime ->
                                    Flowable.interval(Constants.COUNTDOWN_REFRESH_PERIOD, TimeUnit.MILLISECONDS)
                                        .map(tick -> System.currentTimeMillis() - startTime)
                                        .takeUntil(elapsed -> elapsed > Constants.DEFAULT_INCUBATION_PERIOD)
                                        .map(elapsed -> Constants.DEFAULT_INCUBATION_PERIOD - elapsed)
                                        .map(remaining -> timeFormatter.format(new Date(remaining)))
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .flatMapCompletable(countdownText ->
                                                Completable.fromAction(() -> ifViewAttached(activeView -> activeView.showCountdownText(countdownText))))
                                );
                    } else {
                        return Completable.fromAction(() -> ifViewAttached(IncubationView::navigateBack))
                                .delay(Constants.COUNTDOWN_REFRESH_PERIOD, TimeUnit.MILLISECONDS);
                    }
                })
                .andThen(Completable.fromAction(incubationInteractor::stopIncubation))
                .observeOn(AndroidSchedulers.mainThread())
                .andThen(Completable.fromAction(() -> ifViewAttached(IncubationView::navigateForward)))
                .onErrorResumeNext(this::handleThrowable)
                .subscribe();
    }

    private Completable handleThrowable(@NonNull final Throwable throwable) {
        return incubationInteractor.stopIncubation()
                .andThen(Completable.fromAction(() -> ifViewAttached(IncubationView::navigateBack)));
    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
