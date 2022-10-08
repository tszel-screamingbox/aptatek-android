package com.aptatek.pkulab.view.test.wetting;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.test.SampleWettingDone;
import com.aptatek.pkulab.domain.manager.analytic.events.test.SampleWettingSkipped;
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
    private final IAnalyticsManager analyticsManager;
    private CompositeDisposable disposables;
    private Disposable easterEggDisposable;
    private long screenDisplayedMs = 0L;

    @Inject
    public WettingPresenter(final ResourceInteractor resourceInteractor,
                            final WettingInteractor wettingInteractor,
                            final TestInteractor testingInteractor,
                            final IAnalyticsManager analyticsManager) {
        super(resourceInteractor);
        this.wettingInteractor = wettingInteractor;
        this.testingInteractor = testingInteractor;
        this.analyticsManager = analyticsManager;
    }

    @Override
    public void attachView(final WettingView view) {
        super.attachView(view);

        if (screenDisplayedMs == 0L) {
            screenDisplayedMs = System.currentTimeMillis();
        }

        disposables = new CompositeDisposable();

        disposables.add(wettingInteractor.getWettingCountdown()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countdown -> ifViewAttached(attachedView -> attachedView.showCountdown(countdown.getRemainingFormattedText())),
                        Timber::e,
                        () -> {
                            analyticsManager.logEvent(new SampleWettingDone(Math.abs(System.currentTimeMillis() - screenDisplayedMs)));
                            ifViewAttached(attachedView -> {
                                attachedView.setDisclaimerViewVisible(false);
                                attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_wetting_complete_title));
                                attachedView.setMessage("");
                                attachedView.setNextButtonVisible(true);
                            });
                        }
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
                        .subscribe(() -> {
                            analyticsManager.logEvent(new SampleWettingSkipped(Math.abs(System.currentTimeMillis() - screenDisplayedMs)));
                            ifViewAttached(WettingView::showNextScreen);
                        })
        );
    }
}
