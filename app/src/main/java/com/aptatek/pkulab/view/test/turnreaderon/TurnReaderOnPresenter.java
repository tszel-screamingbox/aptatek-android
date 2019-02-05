package com.aptatek.pkulab.view.test.turnreaderon;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.view.test.TestActivityCommonView;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class TurnReaderOnPresenter extends TestBasePresenter<TurnReaderOnView> {

    private static final long MOCK_SELF_TEST_DURATION = 5000L;

    private Disposable disposable;

    @Inject
    public TurnReaderOnPresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_turnreaderon_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_turnreaderon_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.turn_reader_on), true);
            attachedView.setBatteryIndicatorVisible(true);
            attachedView.setBatteryPercentage(100);
        });

        disposeSubscription();
        disposable = Countdown.countdown(MOCK_SELF_TEST_DURATION, (tick) -> tick >= MOCK_SELF_TEST_DURATION, (tick) -> tick)
                .subscribe(ignored -> ifViewAttached(TestActivityCommonView::showNextScreen));

    }

    private void disposeSubscription() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    public void detachView() {
        super.detachView();

        disposeSubscription();
    }
}
