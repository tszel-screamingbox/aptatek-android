package com.aptatek.pkuapp.view.test.tutorial.insertsample;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.samplewetting.SampleWettingInteractor;
import com.aptatek.pkuapp.view.test.tutorial.BaseTutorialPresenter;

import javax.inject.Inject;

public class InsertSamplePresenter extends BaseTutorialPresenter {

    private final SampleWettingInteractor sampleWettingInteractor;

    @Inject
    public InsertSamplePresenter(final ResourceInteractor resourceInteractor,
                                 final SampleWettingInteractor sampleWettingInteractor) {
        super(resourceInteractor);
        this.sampleWettingInteractor = sampleWettingInteractor;
    }

    @Override
    protected int getTitleRes() {
        return R.string.test_insertsample_title;
    }

    @Override
    protected int getMessageRes() {
        return R.string.test_insertsample_description;
    }

    public void startSampleWetting() {
        sampleWettingInteractor.startWetting().subscribe();
    }
}