package com.aptatek.aptatek.view.test.tutorial.insertcasette;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.ResourceInteractor;
import com.aptatek.aptatek.view.test.tutorial.BaseTutorialPresenter;

import javax.inject.Inject;

public class InsertCasettePresenter extends BaseTutorialPresenter {

    @Inject
    public InsertCasettePresenter(final ResourceInteractor resourceInteractor) {
        super(resourceInteractor);
    }

    @Override
    protected int getTitleRes() {
        return R.string.test_insertcasette_title;
    }

    @Override
    protected int getMessageRes() {
        return R.string.test_insertcasette_description;
    }

}
