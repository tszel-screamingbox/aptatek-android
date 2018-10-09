package com.aptatek.pkulab.view.test.turnreaderon;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.view.test.base.TestBasePresenter;

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
