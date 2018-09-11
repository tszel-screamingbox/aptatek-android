package com.aptatek.pkuapp.view.test.wetting;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.samplewetting.SampleWettingInteractor;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class WettingPresenter extends TestBasePresenter<WettingView> {

    private final SampleWettingInteractor wettingInteractor;
    private CompositeDisposable disposables;

    @Inject
    public WettingPresenter(final ResourceInteractor resourceInteractor,
                            final SampleWettingInteractor wettingInteractor) {
        super(resourceInteractor);
        this.wettingInteractor = wettingInteractor;
    }

    @Override
    public void attachView(final WettingView view) {
        super.attachView(view);

        disposables = new CompositeDisposable();

        disposables.add(wettingInteractor.getWettingCountdown()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countdown ->
                    ifViewAttached(attachedView -> attachedView.showCountdown(countdown.getRemainingFormattedText())),
                    Timber::e,
                    () -> ifViewAttached(WettingView::onCountdownFinished)
                )
        );
    }

    @Override
    public void detachView() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
            disposables = null;
        }

        super.detachView();
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setBottomBarVisible(true);
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_wetting_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_wetting_message));
            attachedView.setAlertViewVisible(true);
            attachedView.setAlertMessage(resourceInteractor.getStringResource(R.string.test_wetting_disclaimer));
        });
    }
}
