package com.aptatek.pkuapp.view.test.wetting;

import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.pkuapp.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkuapp.domain.interactor.wetting.WettingNotRunningError;
import com.aptatek.pkuapp.domain.interactor.wetting.WettingStatus;
import com.aptatek.pkuapp.domain.model.Countdown;
import com.aptatek.pkuapp.domain.notifications.CountdownNotificationFactory;
import com.aptatek.pkuapp.injection.component.test.TestServiceComponent;
import com.aptatek.pkuapp.util.Constants;
import com.aptatek.pkuapp.view.test.service.BaseReminderService;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class WettingReminderService extends BaseReminderService {

    private static final int COUNTDOWN_NOTIFICATION_ID = 726;
    private static final int ERROR_NOTIFICATION_ID = 7263;
    private static final int FINISHED_NOTIFICATION_ID = 7264;

    @Inject
    WettingInteractor wettingInteractor;

    @Named("wetting")
    @Inject
    CountdownNotificationFactory countdownNotificationFactory;

    @Inject
    NotificationManagerCompat notificationManager;

    @Override
    protected void injectService(final TestServiceComponent component) {
        component.inject(this);
    }

    @Override
    protected Single<Boolean> shouldStart() {
        return wettingInteractor.getWettingStatus()
                .map(wettingStatus -> WettingStatus.RUNNING == wettingStatus);
    }

    @Override
    protected void startForeground() {
        startForeground(COUNTDOWN_NOTIFICATION_ID, countdownNotificationFactory.createCountdownNotification(
                Countdown.builder()
                        .setRemainingFormattedText("30:00")
                        .setRemainingMillis(Constants.DEFAULT_WETTING_PERIOD)
                        .build()));
        startCountdown();
    }

    private void startCountdown() {
        disposables.add(wettingInteractor.getWettingCountdown()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    countdown -> {
                        Timber.d("Wetting Countdown: %s", countdown);
                        notificationManager.notify(COUNTDOWN_NOTIFICATION_ID, countdownNotificationFactory.createCountdownNotification(countdown));
                    },
                    error -> {
                        Timber.d("Wetting Countdown error: %s", error.toString());
                        stopForeground(false);
                        if (!(error instanceof WettingNotRunningError)) {
                            notificationManager.notify(ERROR_NOTIFICATION_ID, countdownNotificationFactory.createCountdownErrorNotification(error));
                        }
                        stopSelf();
                    },
                    () -> {
                        Timber.d("Wetting Countdown complete");
                        stopForeground(false);
                        notificationManager.notify(FINISHED_NOTIFICATION_ID, countdownNotificationFactory.createCountdownFinishedNotification());
                        stopSelf();
                    })
        );
    }

}
