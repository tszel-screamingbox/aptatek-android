package com.aptatek.pkulab.view.test.mixsample;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

import javax.inject.Inject;

public class MixSamplePresenter extends TestBasePresenter<MixSampleView> {

    @Inject
    public MixSamplePresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_mixsample_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_mixsample_message));
        });
    }
}
