package com.aptatek.aptatek.view.main;


import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

public class MainActivityPresenter extends MvpBasePresenter<MainActivityView> {

    private final ResourceInteractor resourceInteractor;

    @Inject
    MainActivityPresenter(ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    void initView() {
        ifViewAttached(view -> view.mainText(resourceInteractor.getStringResource(R.string.main_text)));
    }
}
