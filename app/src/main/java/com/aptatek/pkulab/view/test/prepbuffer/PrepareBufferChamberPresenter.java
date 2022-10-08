package com.aptatek.pkulab.view.test.prepbuffer;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class PrepareBufferChamberPresenter extends TestBasePresenter<TestFragmentBaseView> {

    @Inject
    public PrepareBufferChamberPresenter(ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_prep_buffer_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_prep_buffer_message));
        });
    }
}
