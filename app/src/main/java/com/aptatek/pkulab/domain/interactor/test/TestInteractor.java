package com.aptatek.pkulab.domain.interactor.test;

import androidx.core.app.NotificationManagerCompat;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.test.TestScreens;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class TestInteractor {

    private final PreferenceManager preferenceManager;

    @Inject
    NotificationManagerCompat notificationManagerCompat;

    @Inject
    public TestInteractor(final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    public Completable storeAppKilledTimestamp(final long timestamp) {
        return Completable.fromAction(() -> preferenceManager.setAppKilledTimestamp(timestamp));
    }

    public Single<Long> getAppKilledTimestamp(){
        return Single.fromCallable(preferenceManager::getAppKilledTimestamp);
    }

    public Completable setTestContinueStatus(final boolean status) {
        return Completable.fromAction(() -> preferenceManager.setTestContinueStatus(status));
    }

    public Single<Boolean> isTestContinueNeed() {
        return Single.fromCallable(preferenceManager::isTestContinueNeed);
    }

    public Single<TestScreens> getLastScreen() {
        return Single.fromCallable(preferenceManager::getTestStatus);
    }

    public Completable setLastScreen(final TestScreens testScreens) {
        return Completable.fromAction(() -> preferenceManager.setTestStatus(testScreens));
    }

    public Completable resetTest() {
        return Completable.fromAction(() -> {
            preferenceManager.clearPreference(PreferenceManager.PREF_TEST_STATUS);
            preferenceManager.clearPreference(PreferenceManager.PREF_TEST_START);
        });
    }

    public Completable cancelWettingFinishedNotifications() {
        return Completable.fromAction(() -> notificationManagerCompat.cancel(Constants.WETTING_FINISHED_NOTIFICATION_ID));
    }

    public Completable cancelWettingCountdownNotification() {
        return Completable.fromAction(() -> notificationManagerCompat.cancel(Constants.WETTING_COUNTDOWN_NOTIFICATION_ID));
    }

    public Completable cancelTestNotifications() {
        return Flowable.fromArray(Constants.BT_READER_READY_NOTIFICATION_ID, Constants.BT_READER_TEST_COMPLETE_NOTIFICATION_ID, Constants.BT_PERMISSION_NOTIFICATION_ID, Constants.BT_MULTIPLE_READERS_NOTIFICATION_ID)
                .flatMapCompletable(notificationId -> Completable.fromAction(() -> notificationManagerCompat.cancel(notificationId)));
    }
}
