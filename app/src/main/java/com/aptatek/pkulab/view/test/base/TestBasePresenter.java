package com.aptatek.pkulab.view.test.base;

import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

public abstract class TestBasePresenter<V extends TestFragmentBaseView> extends MvpBasePresenter<V> {

    protected final ResourceInteractor resourceInteractor;

    protected TestBasePresenter(final ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    public abstract void initUi();

    public void initWithDefaults() {
        ifViewAttached(attachedView -> {
            attachedView.setBottomBarVisible(true);
            attachedView.setBatteryIndicatorVisible(false);
            attachedView.setProgressVisible(false);
            attachedView.setDisclaimerViewVisible(false);
        });

        initUi();
    }
}
