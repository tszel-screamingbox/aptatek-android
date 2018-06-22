package com.aptatek.aptatek.view.test.tutorial.insertsample;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.ResourceInteractor;
import com.aptatek.aptatek.view.test.tutorial.BaseTutorialPresenter;

import javax.inject.Inject;

public class InsertSamplePresenter extends BaseTutorialPresenter {

    @Inject
    public InsertSamplePresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    protected int getTitleRes() {
        return R.string.test_insertsample_title;
    }

    @Override
    protected int getMessageRes() {
        return R.string.test_insertsample_description;
    }

}
