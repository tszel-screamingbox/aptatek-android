package com.aptatek.pkuapp.view.test.mixsample;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

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
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.mix_sample), true);
        });
    }
}
