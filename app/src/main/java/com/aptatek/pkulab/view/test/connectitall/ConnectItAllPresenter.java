package com.aptatek.pkulab.view.test.connectitall;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import javax.inject.Inject;

public class ConnectItAllPresenter extends TestBasePresenter<ConnectItAllView> {

    @Inject
    public ConnectItAllPresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_connectitall_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_connectitall_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.connect_it_all), true);
        });
    }
}
