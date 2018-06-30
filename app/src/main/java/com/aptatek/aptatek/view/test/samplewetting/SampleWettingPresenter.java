package com.aptatek.aptatek.view.test.samplewetting;

import android.support.annotation.DrawableRes;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.samplewetting.SampleWettingInteractor;
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

    private CompositeDisposable disposables;

    @Inject
    public SampleWettingPresenter(final ResourceInteractor resourceInteractor,
                                  final SampleWettingInteractor sampleWettingInteractor) {
        this.resourceInteractor = resourceInteractor;
        this.sampleWettingInteractor = sampleWettingInteractor;
    }

    @Override
    public void initUi() {
        ifViewAttached(activeView -> {
            activeView.setTitle(resourceInteractor.getStringResource(R.string.test_samplewetting_title));
            activeView.setMessage(resourceInteractor.getStringResource(R.string.test_samplewetting_description));
            activeView.setNavigationButtonVisible(false);
            activeView.setCircleCancelVisible(false);
            activeView.setCancelBigVisible(true);
        });
    }

    @Override
    public void attachView(final SampleWettingView view) {
        super.attachView(view);

        disposables = new CompositeDisposable();

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

        disposables.add(sampleWettingInteractor.getWettingProgress()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(progress -> {
                    final @DrawableRes int drawableRes;

                    final int thirds = (1 - progress) / 33;

                    switch (thirds) {
                        case 1:
                            drawableRes = R.drawable.ic_wetting_2;
                            break;
                        case 2:
                            drawableRes = R.drawable.ic_wetting_3;
                            break;
                        case 0:
                        default:
                            drawableRes = R.drawable.ic_wetting_1;
                            break;
                    }
                    ifViewAttached(activeView -> activeView.showImage(drawableRes));
                }));
    }

    @Override
    public void detachView() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }

        super.detachView();
    }
}
