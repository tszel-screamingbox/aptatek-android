package com.aptatek.pkulab.view.test.wetting;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class WettingPresenter extends TestBasePresenter<WettingView> {

    private static final int EASTER_EGG_SECONDS = 3;

    private final WettingInteractor wettingInteractor;
    private final TestInteractor testingInteractor;
    private CompositeDisposable disposables;
    private Disposable easterEggDisposable;

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

    void startEasterEggTimer() {
        Timber.d("EasterEgg timer has been fired");
        easterEggDisposable = Observable.timer(EASTER_EGG_SECONDS, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> resetWetting());

    }

    void stopEasterEggTimer() {
        Timber.d("EasterEgg timer has been stopped");
        if (easterEggDisposable != null && !easterEggDisposable.isDisposed()) {
            easterEggDisposable.dispose();
        }
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
