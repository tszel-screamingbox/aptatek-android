package com.aptatek.aptatek.view.test.takesample;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class TakeSamplePresenter extends MvpBasePresenter<TakeSampleView> {

    private final ResourceInteractor resourceInteractor;
    private boolean showAdult;

    @Inject
    TakeSamplePresenter(final ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
        showAdult = false;
    }

    void initUi() {
        ifViewAttached(view -> {
            view.setTitle(resourceInteractor.getStringResource(R.string.test_takesample_title));
            view.setMessage(resourceInteractor.getStringResource(R.string.test_takesample_description));
            view.setNavigationButtonVisible(true);
            view.setNavigationButtonText(resourceInteractor.getStringResource(R.string.test_takesample_button_start));
            view.setCancelBigVisible(false);
            view.setCircleCancelVisible(true);
        });
        onChangeAge();
    }

    void onChangeAge() {
        showAdult = !showAdult;
        ifViewAttached(view -> {
            view.loadVideo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
            view.showAgeSwitcherText(resourceInteractor.getStringResource(R.string.test_takesample_age_switch,
                    resourceInteractor.getStringResource(showAdult ? R.string.test_takesample_age_child : R.string.test_takesample_age_adult)));
        });
    }
}
