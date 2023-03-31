package com.aptatek.pkulab.view.test.preparecassette;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class PrepareCassettePresenter extends TestBasePresenter<TestFragmentBaseView> {

    @Inject
    public PrepareCassettePresenter(ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_prepare_cassette_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_prepare_cassette_message));
            attachedView.setNextButtonVisible(true);
        });
    }
}