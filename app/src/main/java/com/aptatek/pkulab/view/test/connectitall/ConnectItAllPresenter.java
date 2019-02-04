package com.aptatek.pkulab.view.test.connectitall;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class ConnectItAllPresenter extends TestBasePresenter<ConnectItAllView> {

    private Disposable disposable;
    private final TestInteractor testInteractor;

    @Inject
    public ConnectItAllPresenter(final ResourceInteractor resourceInteractor,
                                 final TestInteractor testInteractor) {
        super(resourceInteractor);
        this.testInteractor = testInteractor;
    }

    @Override
    public void initUi() {
        disposeSubscription();

        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_connectitall_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_connectitall_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.connect_it_all), true);
        });
    }

    public void cancelWettingNotification() {
        disposable = testInteractor.cancelWettingFinishedNotifications()
                .subscribe();
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
