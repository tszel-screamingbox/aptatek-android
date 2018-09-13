package com.aptatek.pkuapp.view.test.turnreaderon;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.test.base.TestBasePresenter;

import javax.inject.Inject;

public class TurnReaderOnPresenter extends TestBasePresenter<TurnReaderOnView> {

    @Inject
    public TurnReaderOnPresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    public void initUi() {
        ifViewAttached(attachedView -> {
            attachedView.setTitle(resourceInteractor.getStringResource(R.string.test_turnreaderon_title));
            attachedView.setMessage(resourceInteractor.getStringResource(R.string.test_turnreaderon_message));
            attachedView.playVideo(resourceInteractor.getUriForRawFile(R.raw.turn_reader_on), true);
        });
    }
}
