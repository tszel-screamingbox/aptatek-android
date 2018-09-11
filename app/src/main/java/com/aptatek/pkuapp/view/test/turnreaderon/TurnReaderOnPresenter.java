package com.aptatek.pkuapp.view.test.turnreaderon;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class TurnReaderOnPresenter extends TestBasePresenter<TurnReaderOnView> {

    private static final long FAKE_CONNECT_DELAY = 5000L;

    private CompositeDisposable compositeDisposable;

    @Inject
    public TurnReaderOnPresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void attachView(final TurnReaderOnView view) {
        super.attachView(view);

        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(Flowable.timer(FAKE_CONNECT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignore -> ifViewAttached(TurnReaderOnView::onReaderConnected))
        );
    }

    @Override
    public void detachView() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

        super.detachView();
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setBottomBarVisible(true);
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_turnreaderon_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_turnreaderon_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.turn_reader_on), false);
            attachedView.setBatteryIndicatorVisible(true);
            attachedView.setBatteryPercentageText("100%");
        });
    }
}
