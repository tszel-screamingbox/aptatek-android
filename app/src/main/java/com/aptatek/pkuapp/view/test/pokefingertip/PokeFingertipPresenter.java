package com.aptatek.pkuapp.view.test.pokefingertip;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

import javax.inject.Inject;

public class PokeFingertipPresenter extends TestBasePresenter<PokeFingertipView> {

    @Inject
    public PokeFingertipPresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_pokefingertrip_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_pokefingertrip_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.poke_fingertip), true);
        });
    }
}
