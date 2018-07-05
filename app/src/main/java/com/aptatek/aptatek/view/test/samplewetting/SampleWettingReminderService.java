package com.aptatek.aptatek.view.test.samplewetting;

import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.aptatek.domain.interactor.incubation.IncubationNotRunningError;
import com.aptatek.aptatek.domain.notifications.CountdownNotificationFactory;
import com.aptatek.aptatek.domain.interactor.samplewetting.SampleWettingInteractor;
import com.aptatek.aptatek.domain.model.Countdown;
import com.aptatek.aptatek.injection.component.test.TestServiceComponent;
import com.aptatek.aptatek.util.Constants;
import com.aptatek.aptatek.view.test.service.BaseReminderService;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SampleWettingReminderService extends BaseReminderService {

    private static final int COUNTDOWN_NOTIFICATION_ID = 726;
    private static final int ERROR_NOTIFICATION_ID = 7263;
    private static final int FINISHED_NOTIFICATION_ID = 7264;

    @Inject
    SampleWettingInteractor sampleWettingInteractor;

    @Named("samplewetting")
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
        return sampleWettingInteractor.hasRunningWetting();
    }

    @Override
    protected void startForeground() {
        startForeground(COUNTDOWN_NOTIFICATION_ID, countdownNotificationFactory.createCountdownNotification(
                Countdown.builder()
                        .setRemainingFormattedText("30:00")
                        .setRemainingMillis(Constants.DEFAULT_INCUBATION_PERIOD)
                        .build()));
        startCountdown();
    }

    private void startCountdown() {
        disposables.add(sampleWettingInteractor.getWettingCountdown()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    countdown -> {
                        Timber.d("Sample Wetting Countdown: %s", countdown);
                        notificationManager.notify(COUNTDOWN_NOTIFICATION_ID, countdownNotificationFactory.createCountdownNotification(countdown));
                    },
                    error -> {
                        Timber.d("Sample Wetting Countdown error: %s", error.toString());
                        stopForeground(false);
                        if (!(error instanceof IncubationNotRunningError)) {
                            notificationManager.notify(ERROR_NOTIFICATION_ID, countdownNotificationFactory.createCountdownErrorNotification(error));
                        }
                        stopSelf();
                    },
                    () -> {
                        Timber.d("Sample Wetting Countdown complete");
                        stopForeground(false);
                        notificationManager.notify(FINISHED_NOTIFICATION_ID, countdownNotificationFactory.createCountdownFinishedNotification());
                        stopSelf();
                    })
        );
    }

}
