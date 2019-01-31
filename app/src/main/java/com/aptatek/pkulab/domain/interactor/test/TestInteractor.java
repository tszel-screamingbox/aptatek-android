package com.aptatek.pkulab.domain.interactor.test;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.view.test.TestScreens;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class TestInteractor {

    private final PreferenceManager preferenceManager;

    @Inject
    public TestInteractor(final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public Single<TestScreens> getLastScreen() {
        return Single.fromCallable(preferenceManager::getTestStatus);
    }

    public Completable setLastScreen(final TestScreens testScreens) {
        return Completable.fromAction(() -> preferenceManager.setTestStatus(testScreens));
    }
}
