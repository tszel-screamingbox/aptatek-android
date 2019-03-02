package com.aptatek.pkulab.view.test.dispose;

import com.aptatek.pkulab.device.PreferenceManager;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

class DisposePresenter extends MvpBasePresenter<DisposeView> {

    private final PreferenceManager preferenceManager;

    @Inject
    DisposePresenter(final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    void done() {
        preferenceManager.setTestFlowStatus(false);
        ifViewAttached(DisposeView::doneFinished);
    }
}
