package com.aptatek.aptatek.view.test.tutorial;

import android.support.annotation.StringRes;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.view.test.base.TestBasePresenter;
import com.aptatek.aptatek.view.test.base.TestFragmentBaseView;

public abstract class BaseTutorialPresenter extends TestBasePresenter<TestFragmentBaseView> {

    private final ResourceInteractor resourceInteractor;

    protected BaseTutorialPresenter(final ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    @Override
    public void initUi() {
        ifViewAttached(activeView -> {
            activeView.setTitle(resourceInteractor.getStringResource(getTitleRes()));
            activeView.setMessage(resourceInteractor.getStringResource(getMessageRes()));
            activeView.setNavigationButtonText(resourceInteractor.getStringResource(R.string.test_button_next));
            activeView.setNavigationButtonVisible(true);
            activeView.setCircleCancelVisible(true);
            activeView.setCancelBigVisible(false);
        });
    }

    protected abstract @StringRes int getTitleRes();

    protected abstract @StringRes int getMessageRes();

}