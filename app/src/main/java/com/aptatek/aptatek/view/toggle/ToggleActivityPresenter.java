package com.aptatek.aptatek.view.toggle;


import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class ToggleActivityPresenter extends MvpBasePresenter<ToggleActivityView> {


    @Inject
    ToggleActivityPresenter() {
    }

    void initView() {

        // TODO: check bluetooth availibility
        if (true) {
            ifViewAttached(ToggleActivityView::cubeAvailable);
        } else {
            ifViewAttached(ToggleActivityView::cubeNotAvailable);
        }
    }
}