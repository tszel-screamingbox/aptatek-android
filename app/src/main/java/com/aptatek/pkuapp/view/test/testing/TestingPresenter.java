package com.aptatek.pkuapp.view.test.testing;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class TestingPresenter extends TestBasePresenter<TestingView> {

    private static final long TICK_INTERVAL = 1L;
    private static final long TEST_PERIOD = 15L;

    private CompositeDisposable disposables;

    @Inject
    public TestingPresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void attachView(final TestingView view) {
        super.attachView(view);

        disposables = new CompositeDisposable();

        disposables.add(Flowable.interval(TICK_INTERVAL, TimeUnit.SECONDS)
                .takeUntil(tick -> tick >= TEST_PERIOD)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tick -> {
                    final int percent = (int)((tick / (float) TEST_PERIOD) * 100f);
                    ifViewAttached(attachedView -> attachedView.setProgressPercentage(percent));
                },
                    Timber::e,
                    () -> ifViewAttached(TestingView::onTestFinished)
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
            attachedView.setBatteryPercentageText("100%");
            attachedView.setProgressVisible(true);
            attachedView.setProgressPercentage(0);
        });
    }
}
