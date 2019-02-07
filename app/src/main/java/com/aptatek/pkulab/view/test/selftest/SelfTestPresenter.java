package com.aptatek.pkulab.view.test.selftest;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.view.test.TestActivityCommonView;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class SelfTestPresenter extends TestBasePresenter<SelfTestView> {

    private static final long MOCK_SELF_TEST_DURATION = 5000L;

    private Disposable disposable;

    @Inject
    SelfTestPresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_selftest_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_selftest_message));
            attachedView.setDisclaimerViewVisible(false);
            attachedView.setBatteryIndicatorVisible(true);
            attachedView.setBatteryPercentage(100); // TODO get real data
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.self_check), true);
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
