package com.aptatek.pkulab.view.test.wetting;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class WettingPresenter extends TestBasePresenter<WettingView> {

    private final WettingInteractor wettingInteractor;
    private final TestInteractor testingInteractor;
    private CompositeDisposable disposables;

    @Inject
    public WettingPresenter(final ResourceInteractor resourceInteractor,
                            final WettingInteractor wettingInteractor,
                            final TestInteractor testingInteractor) {
        super(resourceInteractor);
        this.wettingInteractor = wettingInteractor;
        this.testingInteractor = testingInteractor;
    }

    @Override
    public void attachView(final WettingView view) {
        super.attachView(view);

        disposables = new CompositeDisposable();

        disposables.add(wettingInteractor.getWettingCountdown()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countdown -> ifViewAttached(attachedView -> attachedView.showCountdown(countdown.getRemainingFormattedText())),
                        Timber::e,
                        () -> ifViewAttached(WettingView::showNextScreen)
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
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_wetting_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_wetting_message));
            attachedView.setDisclaimerViewVisible(true);
            attachedView.setDisclaimerMessage(resourceInteractor.getStringResource(R.string.test_wetting_disclaimer));
        });
    }

    public void cancelCountdownNotification() {
        disposables.add(testingInteractor.cancelWettingCountdownNotification().subscribe());
    }

    public void resetWetting() {
        disposables.add(
                wettingInteractor.resetWetting()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> ifViewAttached(WettingView::showNextScreen))
        );
    }
}
