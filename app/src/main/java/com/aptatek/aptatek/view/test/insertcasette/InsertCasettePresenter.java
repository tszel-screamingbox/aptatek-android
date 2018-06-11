package com.aptatek.aptatek.view.test.insertcasette;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.view.test.base.TestBasePresenter;

import javax.inject.Inject;

public class InsertCasettePresenter extends TestBasePresenter<InsertCasetteView> {

    private final ResourceInteractor resourceInteractor;

    @Inject
    public InsertCasettePresenter(final ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    @Override
    public void initUi() {
        ifViewAttached(activeView -> {
            activeView.setTitle(resourceInteractor.getStringResource(R.string.test_insertcasette_title));
            activeView.setMessage(resourceInteractor.getStringResource(R.string.test_insertcasette_description));
            activeView.setNavigationButtonText(resourceInteractor.getStringResource(R.string.test_insertcasette_button_next));
            activeView.setNavigationButtonVisible(true);
            activeView.setCircleCancelVisible(true);
            activeView.setCancelBigVisible(false);
        });
    }

}
