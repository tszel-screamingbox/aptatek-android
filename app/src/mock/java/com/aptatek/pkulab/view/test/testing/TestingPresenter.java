package com.aptatek.pkulab.view.test.testing;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.interactor.reader.ReaderInteractor;
import com.aptatek.pkulab.domain.model.reader.ConnectionState;
import com.aptatek.pkulab.domain.model.reader.TestProgress;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TestingPresenter extends TestBasePresenter<TestingView> {

    private static final long BATTERY_REFRESH_PERIOD = 5 * 1000L;
    private static final long TEST_REFRESH_INTERVAL = 1000L;
    private static final long TEST_PERIOD = 10 * TEST_REFRESH_INTERVAL;

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
                Countdown.countdown(TEST_REFRESH_INTERVAL,
                        tick -> tick > TEST_PERIOD / TEST_REFRESH_INTERVAL,
                        tick -> tick * TEST_REFRESH_INTERVAL)
                        .map(elapsed -> (int) (elapsed / (float) TEST_PERIOD * 100))
                        .flatMap(percent -> {
                            if (percent == 100) {
                                return readerInteractor.getResult(UUID.randomUUID().toString())
                                        .toFlowable()
                                        .flatMap(result -> readerInteractor.saveResult(result)
                                                .andThen(Flowable.just(percent)));
                            }

                            return Flowable.just(percent);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(percent -> ifViewAttached(attachedView -> {
                            attachedView.setProgressPercentage(percent);
                            if (percent == 100) {
                                attachedView.onTestFinished();
                            }
                        }))
        );

        disposables.add(
                readerInteractor.getBatteryLevel()
                        .repeatWhen(objects -> objects.delay(BATTERY_REFRESH_PERIOD, TimeUnit.MILLISECONDS))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                batteryPercent -> ifViewAttached(attachedView -> attachedView.setBatteryPercentage(batteryPercent)),
                                error -> Timber.d("Error during battery level fetch: %s", error)
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
