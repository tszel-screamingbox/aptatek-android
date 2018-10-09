package com.aptatek.pkulab.view.test.collectblood;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import javax.inject.Inject;

public class CollectBloodPresenter extends TestBasePresenter<CollectBloodView> {

    @Inject
    public CollectBloodPresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_collectblood_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_collectblood_message));
            attachedView.setDisclaimerViewVisible(true);
            attachedView.setDisclaimerMessage(resourceInteractor.getStringResource(R.string.test_collectblood_disclaimer));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.collect_blood), true);
        });
    }
}
