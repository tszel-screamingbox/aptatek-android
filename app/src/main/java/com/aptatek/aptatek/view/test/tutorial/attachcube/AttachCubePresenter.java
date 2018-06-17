package com.aptatek.aptatek.view.test.tutorial.attachcube;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.view.test.tutorial.BaseTutorialPresenter;

import javax.inject.Inject;

public class AttachCubePresenter extends BaseTutorialPresenter {

    @Inject
    public AttachCubePresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    protected int getTitleRes() {
        return R.string.test_attachcube_title;
    }

    @Override
    protected int getMessageRes() {
        return R.string.test_attachcube_description;
    }

}
