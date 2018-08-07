package com.aptatek.pkuapp.view.test.tutorial.insertcasette;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.test.tutorial.BaseTutorialPresenter;

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
