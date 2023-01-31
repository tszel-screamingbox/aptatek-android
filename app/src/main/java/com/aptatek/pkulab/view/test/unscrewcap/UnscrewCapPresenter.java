package com.aptatek.pkulab.view.test.unscrewcap;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class UnscrewCapPresenter extends TestBasePresenter<TestFragmentBaseView> {

    @Inject
    public UnscrewCapPresenter(ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_unscrew_title));
            attachedView.setMessageHtml(resourceInteractor.getStringAsHtml(R.string.test_unscrew_message));
        });
    }
}
