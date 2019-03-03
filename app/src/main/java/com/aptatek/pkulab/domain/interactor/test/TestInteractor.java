package com.aptatek.pkulab.domain.interactor.test;

import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.test.TestScreens;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class TestInteractor {

    private final PreferenceManager preferenceManager;

    @Inject
    NotificationManagerCompat notificationManagerCompat;

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

    public Completable resetTest() {
        return Completable.fromAction(() -> preferenceManager.clearPreference(PreferenceManager.PREF_TEST_STATUS));
    }

    public Completable cancelWettingFinishedNotifications() {
        return Completable.fromAction(() -> notificationManagerCompat.cancel(Constants.WETTING_FINISHED_NOTIFICATION_ID));
    }

    public Completable cancelWettingCountdownNotification() {
        return Completable.fromAction(() -> notificationManagerCompat.cancel(Constants.WETTING_COUNTDOWN_NOTIFICATION_ID));
    }
}
