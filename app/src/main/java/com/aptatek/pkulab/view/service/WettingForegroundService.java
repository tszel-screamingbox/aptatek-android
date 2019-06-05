package com.aptatek.pkulab.view.service;

import androidx.core.app.NotificationManagerCompat;

import com.aptatek.pkulab.device.notifications.WettingNotificationFactory;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingNotRunningError;
import com.aptatek.pkulab.domain.model.Countdown;
import com.aptatek.pkulab.injection.component.ApplicationComponent;
import com.aptatek.pkulab.injection.component.test.DaggerTestServiceComponent;
import com.aptatek.pkulab.injection.component.test.TestServiceComponent;
import com.aptatek.pkulab.injection.module.ServiceModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.util.Constants;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class WettingForegroundService extends BaseForegroundService {

    @Inject
    WettingInteractor wettingInteractor;

    @Inject
    WettingNotificationFactory countdownNotificationFactory;

    @Inject
    NotificationManagerCompat notificationManager;

    @Override
    protected void injectService(final ApplicationComponent component) {
        final TestServiceComponent serviceComponent = DaggerTestServiceComponent.builder()
                .applicationComponent(component)
                .testModule(new TestModule())
                .serviceModule(new ServiceModule(this))
                .build();
        serviceComponent.inject(this);
    }

    @Override
    protected void startForeground() {
        startForeground(Constants.WETTING_COUNTDOWN_NOTIFICATION_ID, countdownNotificationFactory.createCountdownNotification(
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
                            notificationManager.notify(Constants.WETTING_COUNTDOWN_NOTIFICATION_ID, countdownNotificationFactory.createCountdownNotification(countdown));
                        },
                        error -> {
                            Timber.d("Wetting Countdown error: %s", error.toString());
                            stopForeground(false);
                            if (!(error instanceof WettingNotRunningError)) {
                                notificationManager.notify(Constants.WETTING_COUNTDOWN_NOTIFICATION_ID, countdownNotificationFactory.createCountdownErrorNotification(error));
                            }
                            stopSelf();
                        },
                        () -> {
                            Timber.d("Wetting Countdown complete");
                            stopForeground(false);
                            notificationManager.notify(Constants.WETTING_FINISHED_NOTIFICATION_ID, countdownNotificationFactory.createCountdownFinishedNotification());
                            stopSelf();
                        })
        );
    }

}
