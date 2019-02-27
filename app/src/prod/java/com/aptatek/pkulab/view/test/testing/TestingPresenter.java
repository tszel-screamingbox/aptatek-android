package com.aptatek.pkulab.view.test.testing;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TestingPresenter extends TestBasePresenter<TestingView> {

    private static final long BATTERY_REFRESH_PERIOD = 10 * 1000L;

    private final ReaderInteractor readerInteractor;
    private CompositeDisposable disposables;

    @Inject
    public TestingPresenter(final ResourceInteractor resourceInteractor,
                            final ReaderInteractor readerInteractor) {
        super(resourceInteractor);
        this.readerInteractor = readerInteractor;
    }

    @Override
    public void attachView(final TestingView view) {
        super.attachView(view);

        disposables = new CompositeDisposable();

        disposables.add(
                readerInteractor.getConnectedReader()
                        .toSingle()
                        .toFlowable()
                        .flatMap(ignored -> readerInteractor.getTestProgress()
                                .takeUntil(testProgress -> testProgress.getPercent() == 100))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                testProgress -> {
                                    Timber.d("Test Progress update: %s", testProgress);
                                    ifViewAttached(attachedView -> attachedView.setProgressPercentage(testProgress.getPercent()));
                                },
                                error -> {
                                    if (error instanceof NoSuchElementException) {
                                        ifViewAttached(TestingView::showTurnReaderOn);
                                    } else {
                                        Timber.d("Unhandled exception during Test: %s", error);
                                    }
                                },
                                () -> Timber.d("Test complete")
                        )
        );

        disposables.add(
                readerInteractor.getReaderConnectionEvents()
                        .filter(connectionEvent -> connectionEvent.getConnectionState() == ConnectionState.READY)
                        .take(1)
                        .flatMap(device -> readerInteractor.getBatteryLevel()
                                .repeatWhen(objects -> objects.flatMap(ignored -> Countdown.countdown(BATTERY_REFRESH_PERIOD, (tick) -> tick >= BATTERY_REFRESH_PERIOD, (tick) -> BATTERY_REFRESH_PERIOD - tick))))
                        .takeUntil(readerInteractor.getTestProgress().filter(testProgress -> testProgress.getPercent() == 100))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                batteryPercent -> ifViewAttached(attachedView -> attachedView.setBatteryPercentage(batteryPercent)),
                                error -> Timber.d("Error during battery level fetch: %s", error)
                        )
        );

        disposables.add(
                readerInteractor.getTestProgress()
                        .filter(testProgress -> testProgress.getPercent() == 100)
                        .take(1)
                        .map(TestProgress::getStart)
                        .map(String::valueOf)
                        .delay(1, TimeUnit.SECONDS)
                        .flatMapSingle(readerInteractor::getResult)
                        .flatMapCompletable(readerInteractor::saveResult)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    Timber.d("Result successfully saved");
                                    ifViewAttached(TestingView::onTestFinished);
                                },
                                error -> Timber.d("Error while getting latest result: %s", error)
                        )
        );
    }

    @Override
    public void detachView() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }

        super.detachView();
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_testing_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_testing_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.testing), true);
            attachedView.setBatteryIndicatorVisible(true);
            attachedView.setProgressVisible(true);
            attachedView.setProgressPercentage(0);
        });
    }
}
