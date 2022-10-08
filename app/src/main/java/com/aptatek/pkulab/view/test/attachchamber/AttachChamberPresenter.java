package com.aptatek.pkulab.view.test.attachchamber;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class AttachChamberPresenter extends TestBasePresenter<TestFragmentBaseView> {

    @Inject
    public AttachChamberPresenter(ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_attach_chamber_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_attach_chamber_message));
            attachedView.setDisclaimerViewVisible(true);
            attachedView.setDisclaimerMessage(resourceInteractor.getStringResource(R.string.test_attach_chamber_disclaimer));
        });
    }
}