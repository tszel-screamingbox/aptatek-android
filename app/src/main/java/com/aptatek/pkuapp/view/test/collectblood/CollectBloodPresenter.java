package com.aptatek.pkuapp.view.test.collectblood;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

import javax.inject.Inject;

public class CollectBloodPresenter extends TestBasePresenter<CollectBloodView> {

    @Inject
    public CollectBloodPresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setBottomBarVisible(true);
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_collectblood_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_collectblood_message));
            attachedView.setAlertViewVisible(true);
            attachedView.setAlertMessage(resourceInteractor.getStringResource(R.string.test_collectblood_disclaimer));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.collect_blood), true);
        });
    }
}
