package com.aptatek.aptatek.view.test.samplewetting;

import android.support.annotation.DrawableRes;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.wetting.SampleWettingInteractor;
import com.aptatek.aptatek.view.test.TestActivityView;
import com.aptatek.aptatek.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SampleWettingPresenter extends TestBasePresenter<SampleWettingView> {

    private final ResourceInteractor resourceInteractor;
    private final SampleWettingInteractor sampleWettingInteractor;

    private final CompositeDisposable disposables;

    @Inject
    public SampleWettingPresenter(final ResourceInteractor resourceInteractor,
                                  final SampleWettingInteractor sampleWettingInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.sampleWettingInteractor = sampleWettingInteractor;
        disposables = new CompositeDisposable();
    }

    @Override
    public void initUi() {
        ifViewAttached(activeView -> {
            activeView.setTitle(resourceInteractor.getStringResource(R.string.test_wetting_title));
            activeView.setMessage(resourceInteractor.getStringResource(R.string.test_wetting_description));
            activeView.setNavigationButtonVisible(false);
            activeView.setCircleCancelVisible(false);
            activeView.setCancelBigVisible(true);
        });
    }

    @Override
    public void attachView(final SampleWettingView view) {
        super.attachView(view);

        disposables.add(sampleWettingInteractor.getWettingCountdown()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countdown -> {
                            Timber.d("Countdown: %s", countdown);
                            ifViewAttached(activeView -> activeView.showCountdown(countdown.getRemainingFormattedText()));
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
        disposables.add(sampleWettingInteractor.getWettingStatus()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {
                    final @DrawableRes int drawableRes;
                    switch (status) {
                        case SECOND_THIRD:
                            drawableRes = R.drawable.ic_wetting_2;
                            break;
                        case THIRD_THIRD:
                            drawableRes = R.drawable.ic_wetting_3;
                            break;
                        case FIRST_THIRD:
                        default:
                            drawableRes = R.drawable.ic_wetting_1;
                            break;
                    }
                    ifViewAttached(activeView -> activeView.showImage(drawableRes));
                }));
    }

    @Override
    public void detachView() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }

        super.detachView();
    }
}
