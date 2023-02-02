package com.aptatek.pkulab.view.test.cleanfintertip;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class CleanFingertipPresenter extends TestBasePresenter<TestFragmentBaseView> {

    @Inject
    public CleanFingertipPresenter(ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_cleanfingertip_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_cleanfingertip_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.clean_fingertip), true);
        });
    }
}
