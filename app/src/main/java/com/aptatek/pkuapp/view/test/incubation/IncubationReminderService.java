package com.aptatek.pkuapp.view.test.incubation;

import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.pkuapp.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationNotRunningError;
import com.aptatek.pkuapp.domain.notifications.CountdownNotificationFactory;
import com.aptatek.pkuapp.domain.model.Countdown;
import com.aptatek.pkuapp.injection.component.test.TestServiceComponent;
import com.aptatek.pkuapp.util.Constants;
import com.aptatek.pkuapp.view.test.service.BaseReminderService;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class IncubationReminderService extends BaseReminderService {

    private static final int COUNTDOWN_NOTIFICATION_ID = 462;
    private static final int FINISHED_NOTIFICATION_ID = 346;
    private static final int ERROR_NOTIFICATION_ID = 377;

    @Inject
    IncubationInteractor incubationInteractor;

    @Named("incubation")
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
        return incubationInteractor.hasRunningIncubation();
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
        disposables.add(incubationInteractor.getIncubationCountdown()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                countdown -> {
                    Timber.d("Incubation Countdown: %s", countdown);
                    notificationManager.notify(COUNTDOWN_NOTIFICATION_ID, countdownNotificationFactory.createCountdownNotification(countdown));
                },
                error -> {
                    Timber.d("Incubation Countdown error: %s", error.toString());
                    stopForeground(false);
                    if (!(error instanceof IncubationNotRunningError)) {
                        notificationManager.notify(ERROR_NOTIFICATION_ID, countdownNotificationFactory.createCountdownErrorNotification(error));
                    }
                    stopSelf();
                },
                () -> {
                    Timber.d("Incubation Countdown complete");
                    stopForeground(false);
                    notificationManager.notify(FINISHED_NOTIFICATION_ID, countdownNotificationFactory.createCountdownFinishedNotification());
                    stopSelf();
                })
        );
    }

}
