package com.aptatek.pkuapp.view.test.connectitall;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

import javax.inject.Inject;

public class ConnectItAllPresenter extends TestBasePresenter<ConnectItAllView> {

    @Inject
    public ConnectItAllPresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setBottomBarVisible(true);
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_connectitall_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_connectitall_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.connect_it_all), true);
        });
    }
}
